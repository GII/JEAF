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
 * ParallelEvolutionaryAlgorithm.java
 *
 * Created on February 26, 2008, 12:06 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.evaluate.EvaluationStrategy;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.operator.OperatorChain;
import es.udc.gii.common.eaf.algorithm.operator.replace.ReplaceOperator;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.ReproductionOperator;
import es.udc.gii.common.eaf.algorithm.operator.selection.SelectionOperator;
import es.udc.gii.common.eaf.algorithm.parallel.evaluation.DistributedEvaluation;
import es.udc.gii.common.eaf.algorithm.parallel.migration.MigrationOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.stoptest.StopTest;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.apache.commons.configuration.Configuration;

/**
 * This class represents a parallel evolutionary algorithm (PGA). It encapsulates
 * a serial evolutionary algorithm and makes it posible to run the encapsulated
 * algorithm in a distributed/parallel environment.
 *  
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 * @see MigrationOperator
 * @see DistributedEvaluation
 */
public class ParallelEvolutionaryAlgorithm extends EvolutionaryAlgorithm implements Observer {

    private EvolutionaryAlgorithm ea = null;
    private Observable currentObservable = null;

    /** Creates a new instance of ParallelEvolutionaryAlgorithm */
    public ParallelEvolutionaryAlgorithm() {
    }

    @Override
    public String toString() {
        return "Parallel Evolutionary Algorithm (" + ea + ")";
    }

    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);
            if (getEvaluationStrategy() instanceof DistributedEvaluation) {
                ((DistributedEvaluation) getEvaluationStrategy()).addObserver(this);
            }

            if (isAMaster()) {
                ea = (EvolutionaryAlgorithm) Class.forName(conf.getString("EA")).newInstance();

                ea.configure(conf);
                ea.setEvaluationStrategy(super.getEvaluationStrategy());
                ea.setEvalChain(super.getEvalChain());
                ea.setProblem(super.getProblem());
                ea.setReplaceChain(super.getReplaceChain());
                ea.setReproductionChain(super.getReproductionChain());
                ea.setSelectionChain(super.getSelectionChain());              
                setEa(ea);
            } else {
                setEa(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        currentObservable = o;
        this.setChanged();
        this.notifyObservers(arg);
    }

    @Override
    public void resolve(StopTest objective) {
        if (this.isAMaster()) {
            getEa().resolve(objective);

            if (getReplaceChain() != null) {
                List<ReplaceOperator> ops = getReplaceChain().getOperators();

                for (ReplaceOperator elem : ops) {
                    if (elem instanceof MigrationOperator) {
                        ((MigrationOperator) elem).finish();
                    }
                }
            }

            if (getEvaluationStrategy() instanceof DistributedEvaluation) {
                ((DistributedEvaluation) getEvaluationStrategy()).finish();
            }
        } else {
            this.state = EVALUATE_STATE;
            this.setChanged();
            this.notifyObservers();
            this.clearChanged();

            ((DistributedEvaluation) getEvaluationStrategy()).setComparator(getComparator());

            ((DistributedEvaluation) getEvaluationStrategy()).evaluate(this, (List<Individual>) null,
                    getProblem().getObjectiveFunctions(), getProblem().getConstraints());
            ((DistributedEvaluation) getEvaluationStrategy()).finish();

            this.state = FINAL_STATE;
            this.setChanged();
            this.notifyObservers();
            this.clearChanged();

            this.state = CLOSE_LOGS_STATE;
            this.setChanged();
            this.notifyObservers();
            this.clearChanged();
        }
    }

    // Default implemetation: do nothing because the internal EA should do this
    @Override
    protected void reproduce(Population population) {
    }

    // Default implemetation: do nothing because the internal EA should do this
    @Override
    protected void select(Population toPopulation) {
    }

    // Default implemetation: do nothing because the internal EA should do this
    @Override
    protected void replace(Population toPopulation) {
    }

    // Default implemetation: do nothing because the internal EA should do this
    @Override
    protected void evaluate(Problem problem, Population population) {
    }

    // Default implemetation: do nothing because the internal EA should do this
    @Override
    protected void init() {
    }

    @Override
    public int getFEs() {
        if (ea != null) {
            return ea.getFEs();
        } else {
            return super.getFEs();
        }
    }

    @Override
    public boolean getFinish() {
        if (ea != null) {
            return ea.getFinish();
        } else {
            return super.getFinish();
        }
    }

    @Override
    public int getGenerations() {
        if (ea != null) {
            return ea.getGenerations();
        } else {
            return super.getGenerations();
        }
    }

    @Override
    public int getMaxGenerations() {
        if (ea != null) {
            return ea.getMaxGenerations();
        } else {
            return super.getMaxGenerations();
        }
    }

    @Override
    public Population getPopulation() {
        if (ea != null) {
            return ea.getPopulation();
        } else {
            return super.getPopulation();
        }
    }

    @Override
    public int getState() {
        if (ea != null) {
            return ea.getState();
        } else {
            return super.getState();
        }
    }

    @Override
    public EvaluationStrategy getEvaluationStrategy() {
        return super.getEvaluationStrategy();
    }

    @Override
    public FitnessComparator<Individual> getComparator() {
        if (ea != null) {
            return ea.getComparator();
        } else {
            return super.getComparator();
        }
    }

    @Override
    public Problem getProblem() {
        if (ea != null) {
            return ea.getProblem();
        } else {
            return super.getProblem();
        }
    }

    @Override
    public OperatorChain<ReplaceOperator> getReplaceChain() {
        if (ea != null) {
            return ea.getReplaceChain();
        } else {
            return super.getReplaceChain();
        }
    }

    @Override
    public OperatorChain<ReproductionOperator> getReproductionChain() {
        if (ea != null) {
            return ea.getReproductionChain();
        } else {
            return super.getReproductionChain();
        }
    }

    @Override
    public OperatorChain<SelectionOperator> getSelectionChain() {
        if (ea != null) {
            return ea.getSelectionChain();
        } else {
            return super.getSelectionChain();
        }
    }

    public Observable getCurrentObservable() {
        return currentObservable;
    }

    @Override
    public void setEvaluationStrategy(EvaluationStrategy evaluationStrategy) {
        if (ea != null) {
            ea.setEvaluationStrategy(evaluationStrategy);
        }

        if (evaluationStrategy instanceof DistributedEvaluation) {
            ((DistributedEvaluation) evaluationStrategy).addObserver(this);
        }

        super.setEvaluationStrategy(evaluationStrategy);
    }

    @Override
    public void setFEs(int FEs) {
        if (ea != null) {
            ea.setFEs(FEs);
        }
        super.setFEs(FEs);
    }

    @Override
    public void setFinish(boolean finish) {
        if (ea != null) {
            ea.setFinish(finish);
        }
        super.setFinish(finish);
    }

    @Override
    public void setPopulation(Population population) {
        if (ea != null) {
            ea.setPopulation(population);
        }
        super.setPopulation(population);
    }

    @Override
    public void setProblem(Problem problem) {
        if (ea != null) {
            ea.setProblem(problem);
        }
        super.setProblem(problem);
    }

    @Override
    public void setReplaceChain(OperatorChain<ReplaceOperator> replaceChain) {
        if (ea != null) {
            ea.setReplaceChain(replaceChain);
        }
        super.setReplaceChain(replaceChain);

        if (replaceChain != null) {
            List<ReplaceOperator> ops = replaceChain.getOperators();

            for (ReplaceOperator elem : ops) {
                if (elem instanceof MigrationOperator) {
                    elem.addObserver(this);
                }
            }
        }
    }

    @Override
    public void setReproductionChain(OperatorChain<ReproductionOperator> reproductionChain) {
        if (ea != null) {
            ea.setReproductionChain(reproductionChain);
        }
        super.setReproductionChain(reproductionChain);
    }

    @Override
    public void setSelectionChain(OperatorChain<SelectionOperator> selectionChain) {
        if (ea != null) {
            ea.setSelectionChain(selectionChain);
        }
        super.setSelectionChain(selectionChain);
    }

    public EvolutionaryAlgorithm getEa() {
        return ea;
    }

    public void setEa(EvolutionaryAlgorithm ea) {
        this.ea = ea;
        if (ea != null) {
            ea.addObserver(this);
        }
    }

    public boolean isAMaster() {
        if (getEvaluationStrategy() instanceof DistributedEvaluation) {
            return ((DistributedEvaluation) getEvaluationStrategy()).isMaster();
        } else {
            return true;
        }
    }

    @Override
    public String getAlgorithmID() {
        if (this.ea != null) {
            return "PGA + " + ea.getAlgorithmID();
        }
        return "PGA";
    }
}
