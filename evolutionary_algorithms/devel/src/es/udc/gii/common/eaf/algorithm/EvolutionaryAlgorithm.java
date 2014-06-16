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
package es.udc.gii.common.eaf.algorithm;

import es.udc.gii.common.eaf.algorithm.evaluate.EvaluationStrategy;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.operator.OperatorChain;
import es.udc.gii.common.eaf.algorithm.operator.evaluate.EvaluationOperator;
import es.udc.gii.common.eaf.algorithm.operator.replace.ReplaceOperator;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.ReproductionOperator;
import es.udc.gii.common.eaf.algorithm.operator.selection.SelectionOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.WorstIndividualSpecification;
import es.udc.gii.common.eaf.algorithm.restart.RestartStrategy;
import es.udc.gii.common.eaf.config.Configurable;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.stoptest.CompositeStopTest;
import es.udc.gii.common.eaf.stoptest.EvolveGenerationsStopTest;
import es.udc.gii.common.eaf.stoptest.FEsStopTest;
import es.udc.gii.common.eaf.stoptest.StopTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/*
 * This class represents an evolutionary algorithm. This algorithm it will be used to resolve a
 * problem. <p/>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) @since
 * 1.0
 */
public abstract class EvolutionaryAlgorithm extends Observable implements Configurable {
    //Estado inicial: en el que se genera la poblacion inicial.

    public final static int INIT_STATE = 0;
    // Rafa: Cambio los atributos de clase a final para que sean constantes.
    //Evaluacion de la poblacion inicial:
    public final static int INIT_EVALUATE_STATE = 1;    //Antes de la seleccion:
    public final static int SELECT_STATE = 2;
    public final static int REPRODUCTION_STATE = 3;
    public final static int EVALUATE_STATE = 4;
    public final static int REPLACE_STATE = 5;
    public final static int FINAL_STATE = 6;
    public final static int CLOSE_LOGS_STATE = 7;
    public final static int PRE_EVALUATION_STATE = 8;
    protected int state;
    private Population population;
    private Population new_population;
    private EvaluationStrategy evaluationStrategy;
    private RestartStrategy restartStrategy;
    private OperatorChain<SelectionOperator> selectionChain;
    private OperatorChain<ReproductionOperator> reproductionChain;
    private OperatorChain<ReplaceOperator> replaceChain;
    private OperatorChain<EvaluationOperator> evalChain;
    private Problem problem;
    protected int generations;
    private int FEs = 1;
    private boolean finish = false;
    private int maxGenerations = 0;
    private int maxEvaluations = 0;
    private FitnessComparator<Individual> comparator = null;
    private StopTest stopTest;
    private StopTest restartTest;
    private String userTag = "";
    // This allows the user to use a custom init population instead of a
    // random one
    private boolean useCustomInitPopulation = false;
    private List<Individual> fitness_history;
    private int fitness_history_capacity = -1;

    //27-02-2010: se implementa el método best_ever para cuando haya 
    //técnica de reemplazo que se guarde
    //el mejor individuo que se haya encontrado hasta ese momento. 
    //getBestIndividual sigue devolviendo
    //el mejor de la población, pero bestEver devuelve el mejor.
    private Individual best_ever_individual;

    //04-09-2013: "debug" boolean parameter to indicate if debug messages are printed or not.
    //false by default
    private boolean debug = false;

    public EvolutionaryAlgorithm() {
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public EvaluationStrategy getEvaluationStrategy() {
        return evaluationStrategy;
    }

    public void setEvaluationStrategy(EvaluationStrategy evaluationStrategy) {
        this.evaluationStrategy = evaluationStrategy;
    }

    public RestartStrategy getRestartStrategy() {
        return restartStrategy;
    }

    public void setRestartStrategy(RestartStrategy restartStrategy) {
        this.restartStrategy = restartStrategy;
    }

    public OperatorChain<SelectionOperator> getSelectionChain() {
        return selectionChain;
    }

    public void setSelectionChain(
            OperatorChain<SelectionOperator> selectionChain) {
        this.selectionChain = selectionChain;
    }

    public OperatorChain<ReproductionOperator> getReproductionChain() {
        return reproductionChain;
    }

    public void setReproductionChain(
            OperatorChain<ReproductionOperator> reproductionChain) {
        this.reproductionChain = reproductionChain;
    }

    public OperatorChain<ReplaceOperator> getReplaceChain() {
        return this.replaceChain;
    }

    public void setReplaceChain(OperatorChain<ReplaceOperator> replaceChain) {
        this.replaceChain = replaceChain;
    }

    public OperatorChain<EvaluationOperator> getEvalChain() {
        return evalChain;
    }

    public void setEvalChain(OperatorChain<EvaluationOperator> evalChain) {
        this.evalChain = evalChain;
    }

    public int getGenerations() {
        return generations;
    }

    public int getMaxGenerations() {

        return this.maxGenerations;
    }

    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    protected void setMaxEvaluations(StopTest objective) {

        List<StopTest> stopTests;
        this.maxEvaluations = -Integer.MAX_VALUE;

        if (objective instanceof CompositeStopTest) {

            stopTests = ((CompositeStopTest) objective).getStopTests();

        } else {

            stopTests = (new ArrayList<StopTest>());
            stopTests.add(objective);
        }

        for (int i = 0; i < stopTests.size(); i++) {

            if (stopTests.get(i) instanceof FEsStopTest) {

                this.maxEvaluations = Math.max(this.maxEvaluations,
                        ((FEsStopTest) stopTests.get(i)).getFEs(this));

            }

        }

        if (this.maxEvaluations == -Integer.MAX_VALUE) {
            this.maxEvaluations = Math.abs(this.maxEvaluations);
        }

    }

    protected void setMaxGenerations(StopTest objective) {

        List<StopTest> stopTests;
        this.maxGenerations = -Integer.MAX_VALUE;

        if (objective instanceof CompositeStopTest) {

            stopTests = ((CompositeStopTest) objective).getStopTests();

        } else {

            stopTests = (new ArrayList<StopTest>());
            stopTests.add(objective);
        }

        for (int i = 0; i < stopTests.size(); i++) {

            if (stopTests.get(i) instanceof EvolveGenerationsStopTest) {

                this.maxGenerations = Math.max(this.maxGenerations,
                        ((EvolveGenerationsStopTest) stopTests.get(i)).getGenerations());

            }

        }

        if (this.maxGenerations == -Integer.MAX_VALUE) {
            this.maxGenerations = Math.abs(this.maxGenerations);
        }

    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public int getState() {
        return this.state;
    }

    public String getStateString() {

        switch (this.state) {
            case EvolutionaryAlgorithm.INIT_STATE:
                return "INIT_STATE";
            case EvolutionaryAlgorithm.INIT_EVALUATE_STATE:
                return "INIT_EVALUATE_STATE";
            case EvolutionaryAlgorithm.SELECT_STATE:
                return "SELECT_STATE";
            case EvolutionaryAlgorithm.REPRODUCTION_STATE:
                return "REPRODUCTION_STATE";
            case EvolutionaryAlgorithm.PRE_EVALUATION_STATE:
                return "PRE_EVALUATION_STATE";
            case EvolutionaryAlgorithm.EVALUATE_STATE:
                return "EVALUATE_STATE";
            case EvolutionaryAlgorithm.REPLACE_STATE:
                return "REPLACE_STATE";
            case EvolutionaryAlgorithm.FINAL_STATE:
                return "FINAL_STATE";
            case EvolutionaryAlgorithm.CLOSE_LOGS_STATE:
                return "CLOSE_LOGS_STATE";
            default:
                return "";
        }

    }

    public void resolve(StopTest objective, int maxGenerations) {

        this.maxGenerations = maxGenerations;

        this.resolve(objective);

    }

    public void resolve(StopTest objective) {

        this.generations = 0;
        this.FEs = 0;
        this.state = EvolutionaryAlgorithm.INIT_STATE;
        //TODO: Cambiar esto para que se encargue la factoria de establecerlo:
        this.stopTest = objective;

        this.next();

        this.next();

        while (objective == null || !objective.isReach(this) /*&& !this.finish*/) {
            this.step();

        }

        this.next();
        this.next();
        this.finish = true;
    }

    /*
     * This method allow to execute one step of a generation according to the current state of the
     * algorithm. If state == EvolutionaryAlgorithm.INIT_STATE: init() If state
     * ==EvolutionaryAlgorithm.INIT_EVALUATE_STATE: evaluate() If state ==
     * EvolutionaryAlgorithm.SELECT_STATE: select() If state ==
     * EvolutionaryAlgorithm.REPRODUCTION_STATE: reproduce() If state ==
     * EvolutionaryAlgorithm.EVALUATE_STATE: evaluate() If state
     * ==EvolutionaryAlgorithm.REPLACE_STATE: replace() If state ==
     * EvolutionaryAlgorithm.FINAL_STATE: notify to the observers. If state ==
     * EvolutionaryAlgorithm.CLOSE_LOGS_STATE: notify to the observers.
     */
    public void next() {

        switch (this.state) {

            case EvolutionaryAlgorithm.INIT_STATE:
                this.init();
                this.notifyChanges();
                this.state = EvolutionaryAlgorithm.INIT_EVALUATE_STATE;
                this.notifyChanges();
                break;
            case EvolutionaryAlgorithm.INIT_EVALUATE_STATE:
                this.evaluate(this.problem, this.getPopulation());
                this.new_population = new Population();
                this.notifyChanges();
                this.state = EvolutionaryAlgorithm.SELECT_STATE;
                break;
            case EvolutionaryAlgorithm.SELECT_STATE:
                this.select(this.new_population);
                this.notifyChanges();
                this.state = EvolutionaryAlgorithm.REPRODUCTION_STATE;
                break;
            case EvolutionaryAlgorithm.REPRODUCTION_STATE:
                this.reproduce(this.new_population);
                this.notifyChanges();
                this.state = EvolutionaryAlgorithm.PRE_EVALUATION_STATE;
                this.notifyChanges();
                this.state = EvolutionaryAlgorithm.EVALUATE_STATE;
                break;
            case EvolutionaryAlgorithm.EVALUATE_STATE:
                this.evaluate(this.problem, new_population);
                this.notifyChanges();
                this.state = EvolutionaryAlgorithm.REPLACE_STATE;
                break;
            case EvolutionaryAlgorithm.REPLACE_STATE:
                this.replace(this.new_population);
                this.notifyChanges();
                //Después de replace hay que comprobar el test de parada
                this.generations++;
                this.problem.resetObjectiveFunctions();

                //01 - 07 - 2011 - Añadidas las estrategias de restart, en esta 
                //última fase de la generación se comprueban si se cumple alguna 
                //si se cumple se aplica la estrategia de restart que es la 
                //encargada de poner el nuevo estado del algoritmo.
                if (this.restartStrategy != null
                        && this.restartTest != null && this.restartTest.isReach(this)) {

                    this.restartStrategy.restart(this);

                } else if (this.stopTest == null || !this.stopTest.isReach(this)) {
                    this.state = EvolutionaryAlgorithm.SELECT_STATE;
                } else {
                    this.state = EvolutionaryAlgorithm.FINAL_STATE;
                    //this.finish = true;
                }
                break;
            case EvolutionaryAlgorithm.FINAL_STATE:
                this.notifyChanges();
                this.state = EvolutionaryAlgorithm.CLOSE_LOGS_STATE;
                break;
            case EvolutionaryAlgorithm.CLOSE_LOGS_STATE:
                this.notifyChanges();
                break;
        }
    }

    /*
     * This method allows to execute one complete generation.
     */
    public void step() {

        if (this.state == EvolutionaryAlgorithm.INIT_STATE) {
            //One step for the init state and the other one for the init_evaluate:
            this.next();
            this.next();
        }

        do {
            this.next();
        } while (this.state != EvolutionaryAlgorithm.SELECT_STATE
                && this.state != EvolutionaryAlgorithm.FINAL_STATE);

    }

    private void notifyChanges() {
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    }

    protected void init() {
        if (this.maxGenerations == 0) {
            this.setMaxGenerations(this.stopTest);
        }
        if (this.maxEvaluations == 0) {
            this.setMaxEvaluations(this.stopTest);
        }
        if (!this.useCustomInitPopulation) {
            this.getPopulation().generate();
        }

    }

    protected void evaluate(Problem problem, Population population) {
        if (getEvalChain() != null && getEvalChain().getOperators() != null
                && !getEvalChain().getOperators().isEmpty()) {
            population.setIndividuals(getEvalChain().execute(this, population));
            this.setFEs(this.getFEs() + population.getSize());
        } else if (this.getEvaluationStrategy() != null) {
            //21 - 06 - 2011 : Se añade la posibilidad de que no exista 
            //evaluación en el algoritmo.
            this.getEvaluationStrategy().evaluate(this,
                    population.getIndividuals(),
                    problem.getObjectiveFunctions(),
                    problem.getConstraints());

            this.setFEs(this.getFEs() + population.getSize());
        }
        //16 - 02 - 2012: Se añade el histórico de calidad, es una FIFO, 
        //si existe se inserta la calidad del mejor individuo:
        if (this.fitness_history != null) {

            if (this.fitness_history.size() == this.fitness_history_capacity) {
                this.fitness_history.set(this.getGenerations()
                        % this.fitness_history_capacity,
                        this.getBestIndividual());
            } else {
                this.fitness_history.add(this.getBestIndividual());
            }
        }
    }

    protected void select(Population toPopulation) {
        if (this.getSelectionChain() != null) {
            toPopulation.setIndividuals(this.getSelectionChain().execute(
                    this, this.getPopulation()));
        } else {
            toPopulation.setIndividuals(this.getPopulation().getIndividualsCopy());
        }
    }

    protected void reproduce(Population population) {
        population.setIndividuals(
                this.getReproductionChain().execute(this, population));
    }

    protected void replace(Population toPopulation) {
        if (this.getReplaceChain() != null) {
            this.getPopulation().setIndividuals(this.getReplaceChain().execute(this, toPopulation));
        } else {
            this.getPopulation().setIndividuals(toPopulation.getIndividuals());
        }
        updateBestEver();
    }

    public void evaluate(Individual individual) {

        this.evaluationStrategy.evaluate(
                this, individual, this.problem.getObjectiveFunctions(),
                this.problem.getConstraints());
        this.setFEs(this.getFEs() + 1);

    }

    public void evaluate(List<Individual> individuals) {
        this.evaluationStrategy.evaluate(this, individuals, this.problem.getObjectiveFunctions(),
                this.problem.getConstraints());
        this.setFEs(this.getFEs() + individuals.size());
    }

    public int getFEs() {
        return FEs;
    }

    public void setFEs(int FEs) {
        this.FEs = FEs;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    @Override
    public void configure(Configuration conf) {
        try {
            setComparator((FitnessComparator) Class.forName(conf.getString("Comparator")).newInstance());

            setUseCustomInitPopulation(conf.containsKey("UseCustomRandomInitPopulation"));

            setDebug(conf.containsKey("Debug"));

            if (conf.containsKey("FitnessHistoryCapacity")) {
                this.fitness_history_capacity = conf.getInt("FitnessHistoryCapacity");
                this.fitness_history = new ArrayList<Individual>();

            } else {
                this.fitness_history = null;
            }

        } catch (Exception ex) {

            System.exit(0);
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isUseCustomInitPopulation() {
        return useCustomInitPopulation;
    }

    public void setUseCustomInitPopulation(boolean useCustomInitPopulation) {
        this.useCustomInitPopulation = useCustomInitPopulation;
    }

    public boolean getFinish() {
        return this.finish;
    }

    public void updateParameters() {
    }

    public String getAlgorithmID() {
        return "EA";
    }

    public FitnessComparator<Individual> getComparator() {
        return comparator;
    }

    public void setComparator(FitnessComparator<Individual> comparator) {
        this.comparator = comparator;
    }

    public StopTest getStopTest() {
        return stopTest;
    }

    public void setStopTest(StopTest stopTest) {
        this.stopTest = stopTest;
    }

    public StopTest getRestartTest() {
        return restartTest;
    }

    public void setRestartTest(StopTest restartTest) {
        this.restartTest = restartTest;
    }

    public Individual getBestIndividual() {

        BestIndividualSpecification spec
                = new BestIndividualSpecification();

        return spec.get(this.population.getIndividuals(), 1, this.comparator).get(0);

    }

    public Individual getWorstIndividual() {
        WorstIndividualSpecification spec
                = new WorstIndividualSpecification();

        return spec.get(this.population.getIndividuals(), 1, this.comparator).get(0);
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Individual> getFitnessHistory() {
        return this.fitness_history;
    }

    public void setFitnessHistory(List<Individual> fitness_history) {
        this.fitness_history = fitness_history;
    }

    public void setFitnessHistoryCapacity(int fitnessHistoryCapacity) {
        this.fitness_history_capacity = fitnessHistoryCapacity;
    }

    public void setPopulationSize(int new_pop_size) {
        this.getPopulation().modifyPopulationSize(new_pop_size);
    }

    private void updateBestEver() {
        if (this.best_ever_individual == null) {
            this.best_ever_individual = this.getBestIndividual();
        } else {
            if (this.comparator.compare(this.best_ever_individual, this.getBestIndividual()) >= 1) {
                this.best_ever_individual = (Individual) this.getBestIndividual().clone();
            }
        }
    }

    public Individual getBestEverIndividual() {
        return this.best_ever_individual;
    }

    public Population getNewPopulation() {
        return this.new_population;
    }
}
