/*
* Copyright (C) 2010 Grupo Integrado de Ingeniería
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/ 


/*
 * DistributedEvaluation.java
 *
 * Created on February 27, 2008, 4:24 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel.evaluation;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.evaluate.EvaluationStrategy;
import es.udc.gii.common.eaf.algorithm.evaluate.SerialEvaluationStrategy;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.parallel.topology.evaluation.EvaluationObject;
import es.udc.gii.common.eaf.algorithm.parallel.topology.evaluation.EvaluationTopology;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import mpi.MPI;
import org.apache.commons.configuration.Configuration;

/**
 * This clas is an evaluation strategy for a distributed environment.<p>
 *
 * An instance of this class has an instance of the {@link EvaluationTopology} class
 * which is in charge of the underlying communications between master and slaves.
 * It also has an instance of a implementation of {@link SerialEvaluationStrategy} which
 * asumes the real evaluation of the individuals.<p>
 *
 * Both masters and slaves perfom evaluation of individuals. That is, the master
 * scatters the individuals and elavuates some individuals. The slaves perfom only
 * evaluations.<p>
 *
 * Each master has two threads. One thread is responsible for evaluating a chunk
 * of the population and the other thread exchanges chunks of individuals between
 * the master and the slaves. When the evaluation thread has evaluated a chunk
 * of individuals it tries to get another chunk and repeats its procedure. When
 * a slave has finished evaluating a received chunk, it sends the chunk back to the
 * master and waits for another. As soon as the master receives the evaluated chunk
 * within its communication thread, this thread sends another free chunk to the
 * slave who is waiting. The process goes on until no more individuals remain to
 * be evaluated.<p>
 *
 * The master stops its execution when no more individuals are pending for evaluation,
 * but the slaves wait until the {@link #finish} method is called by the master. Be sure to
 * call this method in order to terminate the whole process appropiately in your algorithm.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class DistributedEvaluation extends Observable implements EvaluationStrategy, Serializable {

    public static final int SENT = 0;
    public static final int RECEIVED = 5;
    public static final int TO_EVALUATE = 10;
    public static final int EVALUATED = 15;
    public static final int CURRENT_EVALUATION_ENDED = 20;
    public static final int CURRENT_EVALUATION_NOT_STARTED = 25;
    public static final int CURRENT_EVALUATION_STARTED = 30;
    public static final int EVALUATION_NOT_INITIALIZED = -1;
    /* Current state of evaluation. */
    private int state = EVALUATION_NOT_INITIALIZED;
    private FitnessComparator comparator;
    /**
     * Evaluation strategy that is in charge for the real evaluation of the
     * individuals.
     */
    private SerialEvaluationStrategy evaluationStrategy = null;
    /**
     * The master-slave topology defining the communication functions between
     * master and slaves.
     */
    private EvaluationTopology topology = null;
    /**
     * <code>true</code> if the evaluation process is finished.
     */
    protected boolean finished = false;
    /**
     * The maximum amount of individuals sent each time to a node for evaluating
     * them. The sent individuals might be fewer if there aren't enough available.
     */
    private int chunkSize = 0;
    /**
     * The problem's objective functions.
     */
    private List<ObjectiveFunction> functions = null;
    /**
     * the problem's constraints.
     */
    private List<Constraint> constraints = null;
    /**
     * The amount of individuals for evaluation.
     */
    private int popSize = 0;
    /**
     * Barrier for synchronizing the threads.
     */
    private CyclicBarrier barrier = null;
    /**
     * <code>true</code> if statistics about the evaluations should be collected.
     */
    private boolean collectStatistics = false;
    /** Some statistics: eval time, idle time, total evals, etc. */
    private DistributedEvaluationStatistics statistics = null;
    /** Stores the first index of a chunk for each process. */
    private int[] chunkIndex = null;
    /** The individuals to be evaluated. */
    private List<Individual> individualsToEvaluate = null;
    /** Pointer to the first not evaluated individual. */
    private int firtstInd = 0;
    /** Special value for process with no chunk in <code>chunkIndex</code>. */
    private static final int NO_CHUNK = -1;
    /** Number of evaluated individuals so far. */
    private int evaluatedIndividuals = 0;
    /** The communication thread. */
    private transient Thread communicationThread = null;
    private transient boolean commThreadMustWait = true;

    /**
     * Creates a new instance of DistributedEvaluation
     */
    public DistributedEvaluation() {
    }

    /**
     * Creates a new instance of DistributedEvaluation.<p>
     *
     * The argument <code>chunkSize</code> is the amount of individuals sent each
     * time to a node for evaluating them. If is set to 0, then a chunk is choosen so that:
     * let S be the size of the population to be evaluated, let n be the number
     * of proceses (master and slaves), then the chunk size will be S/n
     * (integer division). <p>
     *
     * For instance: if there are a master, two slaves and <code>chunkSize</code>
     * is 2, then the master tries to retain the first 2 individuals, tries to send the next 2
     * to the first slave and the next 2 to the second slave. After that, two threads
     * of execution exist in the master: one for evaluating and one for comunication.<p>
     * 
     * The evaluation thread tries to get new chunks and evaluates them until no
     * more chunk can be retrived. The comunication thread listens for messages
     * from slaves and sends them new chunks when slaves are ready until no more
     * chunk might be retrived. There's no way to control which thread runs, the
     * s.o. is responsible for doing that, and they interleave with each other.
     */
    public DistributedEvaluation(SerialEvaluationStrategy evaluationStrategy,
            EvaluationTopology topology, int chunkSize) {

        this.evaluationStrategy = evaluationStrategy;
        this.topology = topology;

        if (!topology.isInitialized()) {
            topology.initialize();
        }

        setChunkSize(chunkSize);
        this.chunkIndex = new int[getTopology().getSize()];
    }

    synchronized public void notifyObservers(int state, Object arg) {
        setState(state);
        this.setChanged();
        this.notifyObservers(arg);
    }

    /* Statistics */
    public class DistributedEvaluationStatistics implements Serializable {

        /* All times in nanoseconds */
        private long totalSendTimeMaster = 0;
        private long[] totalSendTimeSlave = null;
        private long totalIdleTimeMaster = 0;
        private long[] totalIdleTimeSlave = null;
        private int totalEvalsMaster = 0;
        private int[] totalEvalsSlave = null;
        private long totalEvaluationTimeMaster = 0;
        private long[] totalEvaluationTimeSlave = null;
        private long totalTimeMaster = 0;
        private long[] totalTimeSlave = null;

        DistributedEvaluationStatistics() {
            reset();
        }

        public void reset() {
            int slaves = getTopology().getSize() - 1;
            this.totalEvalsMaster = 0;
            this.totalIdleTimeMaster = 0;
            this.totalSendTimeMaster = 0;
            this.totalEvaluationTimeMaster = 0;
            this.totalTimeMaster = 0;
            this.totalEvalsSlave = new int[slaves];
            this.totalIdleTimeSlave = new long[slaves];
            this.totalSendTimeSlave = new long[slaves];
            this.totalEvaluationTimeSlave = new long[slaves];
            this.totalTimeSlave = new long[slaves];
        }

        void incTotalSendTimeMaster(long inc) {
            this.totalSendTimeMaster += inc;
        }

        void incTotalSendTimeSlave(int slave, long inc) {
            this.totalSendTimeSlave[slave - 1] += inc;
        }

        void incTotalIdleTimeMaster(long inc) {
            this.totalIdleTimeMaster += inc;
        }

        void incTotalIdleTimeSlave(int slave, long inc) {
            this.totalIdleTimeSlave[slave - 1] += inc;
        }

        void incTotalEvalsMaster(int evals) {
            this.totalEvalsMaster += evals;
        }

        void incTotalEvalsSlave(int slave, int evals) {
            this.totalEvalsSlave[slave - 1] += evals;
        }

        void incTotalEvalTimeMaster(long inc) {
            this.totalEvaluationTimeMaster += inc;
        }

        void incTotalEvalTimeSlave(int slave, long inc) {
            this.totalEvaluationTimeSlave[slave - 1] += inc;
        }

        void incTotalTimeMaster(long inc) {
            this.totalTimeMaster += inc;
        }

        void incTotalTimeSlave(int slave, long inc) {
            this.totalTimeSlave[slave - 1] += inc;
        }

        public int getTotalEvalsSlave(int slave) {
            return totalEvalsSlave[slave - 1];
        }

        public long getTotalIdleTimeSlave(int slave) {
            return totalIdleTimeSlave[slave - 1];
        }

        public long getTotalSendTimeSlave(int slave) {
            return totalSendTimeSlave[slave - 1];
        }

        public int getTotalEvalsMaster() {
            return totalEvalsMaster;
        }

        public long getTotalIdleTimeMaster() {
            return totalIdleTimeMaster;
        }

        public long getTotalSendTimeMaster() {
            return totalSendTimeMaster;
        }

        public long getTotalEvaluationTimeMaster() {
            return totalEvaluationTimeMaster;
        }

        public long getTotalEvaluationTimeSlave(int slave) {
            return totalEvaluationTimeSlave[slave - 1];
        }

        public long getTotalTimeMaster() {
            return totalTimeMaster;
        }

        public long getTotalTimeSlave(int slave) {
            return totalTimeSlave[slave - 1];
        }

        void collectStatistics() {
            Object[] sb = new Object[1];
            Object[] rb = new Object[getTopology().getSize()];
            sb[0] = statistics;
            if (isMaster()) {
                getTopology().getCommunicator().Gather(sb, 0, 1, MPI.OBJECT,
                        rb, 0, 1, MPI.OBJECT, getTopology().getTopologyRank());

                for (int i = 0; i < getTopology().getSize(); i++) {
                    if (i != getTopology().getTopologyRank()) {
                        DistributedEvaluationStatistics des =
                                (DistributedEvaluationStatistics) rb[i];

                        incTotalEvalsSlave(i, des.getTotalEvalsSlave(i));
                        incTotalIdleTimeSlave(i, des.getTotalIdleTimeSlave(i));
                        incTotalSendTimeSlave(i, des.getTotalSendTimeSlave(i));
                        incTotalEvalTimeSlave(i, des.getTotalEvaluationTimeSlave(i));
                        incTotalTimeSlave(i, des.getTotalTimeSlave(i));
                    }
                }

            } else {
                getTopology().getCommunicator().Gather(sb, 0, 1, MPI.OBJECT,
                        rb, 0, 1, MPI.OBJECT, getTopology().getMaster());
            }
        }

        @Override
        public String toString() {
            String ret = "total send time master = " + getTotalSendTimeMaster();
            ret += "\ntotal idle time master = " + getTotalIdleTimeMaster();
            ret += "\ntotal evals master = " + getTotalEvalsMaster();
            ret += "\ntotal eval time master = " + getTotalEvaluationTimeMaster();
            ret += "\ntotal time master = " + getTotalTimeMaster();

            for (int i = 1; i <= getTopology().getSize() - 1; i++) {
                ret += "\ntotal send time slave[" + i + "] = " + getTotalSendTimeSlave(i);
                ret += "\ntotal idle time slave[" + i + "] = " + getTotalIdleTimeSlave(i);
                ret += "\ntotal evals slave[" + i + "] = " + getTotalEvalsSlave(i);
                ret += "\ntotal eval time slave[" + i + "] = " + getTotalEvaluationTimeSlave(i);
                ret += "\ntotal time slave[" + i + "] = " + getTotalTimeSlave(i);
            }

            return ret;
        }
    }

    /* The thread that is used for communication, i.e. for sending and receiving
     * individuals from slaves.
     */
    private class CommunicationThread implements Runnable {

        @Override
        public void run() {

            List<Individual> toEvaluate = null;

            /* Remember the number of sents for receiving later. */
            int sent = 0;
            int[] recv = getTopology().getReceivers();
            int myRank = getTopology().getTopologyRank();

            while (!finished) {
                /* Send an initial chunk to each slave if posible. */
                for (int i = 0; i < recv.length; i++) {
                    toEvaluate = getChunk(recv[i]);
                    if (!toEvaluate.isEmpty()) {
                        EvaluationObject eo = new EvaluationObject(toEvaluate,
                                myRank, recv[i]);

                        if (isCollectStatistics()) {
                            long sendTime = System.nanoTime();
                            getTopology().send(eo);
                            sendTime = System.nanoTime() - sendTime;
                            statistics.incTotalSendTimeMaster(sendTime);
                        } else {
                            getTopology().send(eo);
                        }

                        /* Notify observers what we've send. */
                        notifyObservers(SENT, eo);

                        sent++;
                    } else { /* no more chunks */
                        break;
                    }
                }

                /* While some slave is evaluating a previously sent chunk */
                while (sent > 0) {

                    /* Wait until receiving an evaluated chunk. */
                    EvaluationObject received = getTopology().receive();
                    sent--;

                    /* Store the evaluated individuals. */
                    setChunk(received.getSource(), received.getIndividuals());

                    /* Get another chunk if available. */
                    toEvaluate = getChunk(received.getSource());

                    /* If there was a chunk available, send it to the slave which
                     * has returned the last evaluated chunk since it can go on
                     * evaluating another one. */
                    if (!toEvaluate.isEmpty()) {
                        EvaluationObject eo = new EvaluationObject(toEvaluate,
                                myRank, received.getSource());

                        if (isCollectStatistics()) {
                            long sendTime = System.nanoTime();
                            getTopology().send(eo);
                            sendTime = System.nanoTime() - sendTime;
                            statistics.incTotalSendTimeMaster(sendTime);
                        } else {
                            getTopology().send(eo);
                        }

                        /* Notify observers what we've received. */
                        notifyObservers(RECEIVED, received);

                        /* Notify observers what we've sent. */
                        notifyObservers(SENT, eo);

                        sent++;
                    }
                }

                /* Synchronize the two threads. Both have to be done before continuing to
                 * the next generation. */
                if (!barrier.isBroken()) {
                    try {
                        barrier.await();
                    } catch (BrokenBarrierException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                /* Wait for next generation to start or wait for alg's finish. */
                synchronized (communicationThread) {
                    while (commThreadMustWait) {
                        try {
                            communicationThread.wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                    commThreadMustWait = true;
                }
            }
        }
    }

    /* The master thread that runs the evaluations will call this method. */
    private void evaluationThread(EvolutionaryAlgorithm algorithm) {
        boolean resume = false;
        List<Individual> toEvaluate = new ArrayList<Individual>();
        int myRank = getTopology().getTopologyRank();

        /* While not done */
        while (!resume) {
            /* Try to get a free chunk (not evaluated individuals). */
            toEvaluate = getChunk(myRank);
            if (toEvaluate.isEmpty()) {
                resume = true;
                break;
            }

            /* If we've got a free chunk */
            if (!toEvaluate.isEmpty()) {

                /* Notify observers what we have to evaluate. */
                notifyObservers(TO_EVALUATE, toEvaluate);

                /* Evaluate it. */
                if (isCollectStatistics()) {
                    long evalTime = System.nanoTime();
                    getEvaluationStrategy().evaluate(algorithm, toEvaluate, functions, constraints);
                    evalTime = System.nanoTime() - evalTime;
                    this.statistics.incTotalEvalsMaster(toEvaluate.size());
                    this.statistics.incTotalEvalTimeMaster(evalTime);
                } else {
                    getEvaluationStrategy().evaluate(algorithm, toEvaluate, functions, constraints);
                }

                /* Notify observers the evaluated individuals. */
                notifyObservers(EVALUATED, toEvaluate);

                /* Store the evaluated individuals. */
                setChunk(myRank, toEvaluate);

                /* If all individuals have been evaluated, we are done. */
                if (evaluatedIndividuals >= popSize) {
                    resume = true;
                    break;
                }
            }
        }

        long idleTime = System.nanoTime();
        /* Synchronize the two threads. Both have to be done before continuing. */
        if (!barrier.isBroken()) {
            try {
                barrier.await();
            } catch (BrokenBarrierException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        idleTime = System.nanoTime() - idleTime;

        if (isCollectStatistics()) {
            this.statistics.incTotalIdleTimeMaster(idleTime);
        }
    }

    /**
     * Retrives a chunk from the not evaluated part of the individual's list.
     * @param forRank - rank of the process which is going to evaluate the chunk.
     * @return An empty list if there is no more individual to evaluate or a list with
     * individuals not evaluated yet. The size of the list is less or equal
     * the chunk size.
     */
    private synchronized List<Individual> getChunk(int forRank) {
        int chunk = 0;
        int _chunkSize = getChunkSize();
        int firstIndex = firtstInd;
        List<Individual> toEvaluate = new ArrayList<Individual>();

        while ((chunk < _chunkSize) && (firtstInd < popSize)) {
            Individual ind = this.individualsToEvaluate.get(firtstInd++);
            ind.setSerializeGenotype(true);
            ind.setSerializeEvalResults(false);
            toEvaluate.add(ind);
//            toEvaluate.add(this.individualsToEvaluate.get(firtstInd++));
            chunk++;
        }

        if (!toEvaluate.isEmpty()) {
            this.chunkIndex[forRank] = firstIndex;
        }

        return toEvaluate;
    }

    /**
     * Sets an evaluated chunk in the place where it belongs within the 
     * individuals list.
     * @param fromRank - rank of the process who has evaluated the chunk
     * @param evaluated - the evaluated individuals
     */
    private synchronized void setChunk(int fromRank, List<Individual> evaluated) {
        int firstIndex = this.chunkIndex[fromRank];

        for (int i = firstIndex; i < evaluated.size() + firstIndex; i++) {
            evaluated.get(i - firstIndex).copyEvalResults(this.individualsToEvaluate.get(i));
//            this.individualsToEvaluate.set(i, evaluated.get(i - firstIndex));
        }

        this.chunkIndex[fromRank] = NO_CHUNK;
        this.evaluatedIndividuals += evaluated.size();
    }

    protected void master(EvolutionaryAlgorithm algorithm, List<Individual> individuals,
            List<ObjectiveFunction> functions, List<Constraint> constraints) {

        /* Initialize the global state. */
        this.functions = functions;
        this.constraints = constraints;
        this.popSize = individuals.size();

        this.individualsToEvaluate = individuals;
        this.firtstInd = 0;
        this.evaluatedIndividuals = 0;

        if (this.barrier == null) {
            this.barrier = new CyclicBarrier(2);
        }

        boolean setChunkSizeToCero = false;
        if (getChunkSize() == 0) {
            setChunkSizeToCero = true;

            int size = individuals.size();
            int tSize = getTopology().getSize();

            if (size < tSize) {
                setChunkSize(1);
            } else {
                setChunkSize(size / tSize);
            }
        }

        notifyObservers(CURRENT_EVALUATION_STARTED, this);

        /* Initialize the communication thread. */
        if (communicationThread == null) {
            communicationThread = new Thread(new CommunicationThread(), "CommThread");
            communicationThread.setPriority(Thread.MAX_PRIORITY);
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            commThreadMustWait = true;
            communicationThread.start();
        } else {
            synchronized (this.communicationThread) {
                commThreadMustWait = false;
                this.communicationThread.notify();
            }
        }

        /* Run the evaluation thread (current thread, no especial thread is created) */
        evaluationThread(algorithm);

        if (setChunkSizeToCero) {
            setChunkSize(0);
        }

        notifyObservers(CURRENT_EVALUATION_ENDED, this);
        setState(CURRENT_EVALUATION_NOT_STARTED);
    }

    private void updateComparator(EvaluationObject eo) {
        if (eo != null && eo.getIndividuals() != null) {
            for (Individual i : eo.getIndividuals()) {
                i.setComparator(comparator);
            }
        }
    }

    protected void slave(EvolutionaryAlgorithm algorithm, List<ObjectiveFunction> functions,
            List<Constraint> constraints) {

        while (!isFinished()) {
            EvaluationObject evalObj = null;

            if (isCollectStatistics()) {
                long recvTime = System.nanoTime();
                evalObj = getTopology().receive();
                recvTime = System.nanoTime() - recvTime;
                this.statistics.incTotalIdleTimeSlave(
                        getTopology().getTopologyRank(), recvTime);
            } else {
                evalObj = getTopology().receive();
            }

            /* Notify observers what we received. */
            notifyObservers(RECEIVED, evalObj);

            /* The evaluation is finished when a slave receives a null list. */
            if (evalObj.getIndividuals() == null) {
                finished = true;
            } else {

                /* Evaluate individuals. */
                updateComparator(evalObj);

                if (isCollectStatistics()) {
                    long evalTime = System.nanoTime();
                    getEvaluationStrategy().evaluate(algorithm, evalObj.getIndividuals(),
                            functions, constraints);
                    evalTime = System.nanoTime() - evalTime;
                    this.statistics.incTotalEvalsSlave(
                            getTopology().getTopologyRank(),
                            evalObj.getIndividuals().size());
                    this.statistics.incTotalEvalTimeSlave(
                            getTopology().getTopologyRank(), evalTime);
                } else {
                    getEvaluationStrategy().evaluate(algorithm, evalObj.getIndividuals(),
                            functions, constraints);
                }

                evalObj.setSource(getTopology().getTopologyRank());
                evalObj.setDest(getTopology().getMaster());

                /* Notify observers what we send. */
                notifyObservers(SENT, evalObj);

                /* Send only phenotypes */
                for (Individual ind : evalObj.getIndividuals()) {
                    ind.setSerializeGenotype(false);
                    ind.setSerializeEvalResults(true);
                }

                /* Send evaluated individuals. */
                if (isCollectStatistics()) {
                    long sendTime = System.nanoTime();
                    getTopology().send(evalObj);
                    sendTime = System.nanoTime() - sendTime;
                    this.statistics.incTotalSendTimeSlave(
                            getTopology().getTopologyRank(), sendTime);
                } else {
                    getTopology().send(evalObj);
                }
            }

            notifyObservers(CURRENT_EVALUATION_ENDED, this);
            setState(CURRENT_EVALUATION_NOT_STARTED);
        }

    }

    public void finish() {
        if (isMaster()) {
            for (int i = 0; i < getTopology().getReceivers().length; i++) {
                EvaluationObject eo = new EvaluationObject(null,
                        getTopology().getTopologyRank(),
                        getTopology().getReceivers()[i]);

                getTopology().send(eo);
            }

            synchronized (communicationThread) {
                this.finished = true;
                commThreadMustWait = false;
                communicationThread.notify();
            }
            notifyObservers(CURRENT_EVALUATION_ENDED, this);
        }

        /* Terminate topology. */
        getTopology().finish();

        if (isCollectStatistics()) {
            this.statistics.collectStatistics();
        }
    }

    /**
     * Configures this class.<p>
     *
     * Configuration example:<p>
     *
     * <pre>
     *    ...
     *    &lt;EvaluationStrategy&gt;
     *       &lt;Class&gt;es.udc.gii.common.eaf.algorithm.parallel.evaluation.DistributedEvaluation&lt;Class&gt;
     *       &lt;ChunkSize&gt;3&lt;/ChunkSize&gt;
     *       &lt;Races&gt;2&lt;/Races&gt;
     *       &lt;CollectStatistics/&gt;
     *    &lt;/EvaluationStrategy&gt;     
     *    ...
     * </pre><p>
     *
     * This configures a master-slave model with 2 masters (races). The master
     * sends 3 individuals each time a slave asks for individuals (chunk size).
     * Statistics about the whole process are collected (<code>CollectStatistics</code>). <p>
     * 
     * If <code>ChunkSize</code> is set to 0, then a chunk is choosen so that:
     * let S be the size of the population to be evaluated, let n be the number
     * of proceses (master and slaves), then the chunk size will be S/n
     * (integer division).
     *
     * @param conf Configuration.
     */
    @Override
    public void configure(Configuration conf) {
        evaluationStrategy = new SerialEvaluationStrategy();
        evaluationStrategy.configure(conf);

        topology = new EvaluationTopology();
        if (conf.containsKey("Races")) {
            topology.setRaces(conf.getInt("Races"));
        } else {
            topology.setRaces(1); // all nodes
        }
        topology.initialize();

        setChunkSize(conf.getInt("ChunkSize"));
        setCollectStatistics(conf.containsKey("CollectStatistics"));

        this.chunkIndex = new int[getTopology().getSize()];
    }

    /**
     * Evaluates a list of individiduals.
     */
    @Override
    public void evaluate(EvolutionaryAlgorithm algorithm, List<Individual> individuals,
            List<ObjectiveFunction> functions, List<Constraint> constraints) {

        if (isMaster()) {
            if (isCollectStatistics()) {
                long totTime = System.nanoTime();
                master(algorithm, individuals, functions, constraints);
                totTime = System.nanoTime() - totTime;
                this.statistics.incTotalTimeMaster(totTime);
            } else {
                master(algorithm, individuals, functions, constraints);
            }
        } else {
            if (isCollectStatistics()) {
                long totTime = System.nanoTime();
                slave(algorithm, functions, constraints);
                totTime = System.nanoTime() - totTime;
                this.statistics.incTotalTimeSlave(getTopology().getTopologyRank(), totTime);
            } else {
                slave(algorithm, functions, constraints);
            }
        }
    }

    /**
     * Evaluates an individual. This will be always done by a master.
     */
    @Override
    public void evaluate(EvolutionaryAlgorithm algorithm, Individual individual,
            List<ObjectiveFunction> functions, List<Constraint> constraints) {
        long evalTime = System.nanoTime();
        getEvaluationStrategy().evaluate(algorithm, individual, functions, constraints);
        evalTime = System.nanoTime() - evalTime;

        if (isCollectStatistics()) {
            this.statistics.incTotalEvalsMaster(1);
            this.statistics.incTotalEvalTimeMaster(evalTime);
        }
    }

    /**
     * Resets this object. After doing this, this object can be used again in a
     * new evaluation process.
     */
    public void reset() {
        finished = false;
        this.state = CURRENT_EVALUATION_NOT_STARTED;
        this.functions = null;
        this.constraints = null;
        this.popSize = 0;
        this.barrier = null;
        if (this.statistics != null) {
            this.statistics.reset();
        }
    }

    /* ---- Getters and setter follow ---- */
    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public EvaluationTopology getTopology() {
        return topology;
    }

    public void setTopology(EvaluationTopology topology) {
        this.topology = topology;
        if (!this.topology.isInitialized()) {
            this.topology.initialize();
        }
    }

    public SerialEvaluationStrategy getEvaluationStrategy() {
        return evaluationStrategy;
    }

    public void setEvaluationStrategy(SerialEvaluationStrategy evaluationStrategy) {
        this.evaluationStrategy = evaluationStrategy;
    }

    public boolean isMaster() {
        return getTopology().isMaster();
    }

    public boolean isFinished() {
        return finished;
    }

    public int getState() {
        return state;
    }

    protected void setState(int state) {
        this.state = state;
    }

    public FitnessComparator getComparator() {
        return comparator;
    }

    public void setComparator(FitnessComparator comparator) {
        this.comparator = comparator;
    }

    public boolean isCollectStatistics() {
        return collectStatistics;
    }

    public void setCollectStatistics(boolean collectStatistics) {
        this.collectStatistics = collectStatistics;
        this.statistics = new DistributedEvaluationStatistics();
    }

    public DistributedEvaluationStatistics getStatistics() {
        return statistics;
    }

    public List<Individual> getIndividualsToEvaluate() {
        return individualsToEvaluate;
    }
}
