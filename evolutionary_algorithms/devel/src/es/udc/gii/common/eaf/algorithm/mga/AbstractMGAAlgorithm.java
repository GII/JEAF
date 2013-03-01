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
 * AbstractMGAAlgorithm.java
 *
 * Created on December 10, 2007, 11:36 AM
 *
 */
package es.udc.gii.common.eaf.algorithm.mga;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.GeneticAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.stoptest.mga.MicroGenerationsConvergence;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements the generic code for a micro-genetic algorithm.<p>
 * For an implementation of a micro-genetic algorithm for singleobjective problems
 * see {@link es.udc.gii.common.eaf.algorithm.mga.MGAAlgorithm MGAAlgorithm}.<p>
 * For an implementation of a micro-genetic algorithm for multiobjective problems
 * see {@link es.udc.gii.common.eaf.algorithm.mga.MMGAAlgorithm MMGAAlgorithm}.
 *
 * Configuration example:
 *
 * <pre>
 * &lt;Elitism&gt;1&lt;/Elitism&gt;
 * &lt;NominalConvergence&gt;
 *    &lt;Class&gt; ... &lt;/Class&gt;
 *    ...
 * &lt;/NominalConvergence&gt;
 * </pre>
 *
 * The {@code Elitism} parameter defines the elitism for this algorithm. Each
 * subclass determines the concrete meaning of this parameter. <p/>
 *
 * The {@code NominalConvergence} The stop test that states that nominal
 * convergence has been reached for the current micro-evolution. The class name
 * is given by the {@code Class} tag.<p/>
 *
 * The user can provide some initial solutions, that must have been already
 * evaluated, for starting the search providing them to the
 * {@code setInitialSolutions()} method. If the number of initial solutions is
 * less than the population size, the rest of the indivuals are new generated.
 * This means that setting some initial solution effectivelly overrides the
 * {@code useCustomInitPopulation}
 * ({@link EvolutionaryAlgorithm#setUseCustomInitPopulation(boolean)})
 * property since this algorithm always creates new individuals as needed.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class AbstractMGAAlgorithm extends GeneticAlgorithm {

    /**
     * The user can provide some initial solutions that
     * must have been already evaluated.     
     */
    private List<Individual> initialSolutions = null;
    /**
     * Defines the elitism. Each subclass gives the concrete meaning of this parameter.
     *
     * (Configuration parameter).
     */
    private int elitism = 0;
    /**
     * Stores the current generations of the micro-evolution.
     */
    private int microgenerations = 0;
    /**
     * The stop test that states that nominal convergence has been reached
     * for the current micro-evolution.
     *
     * (Configuration parameter).
     */
    private StopTest nominalConvergence = null;

    /** Creates a new instance of AbstractMGAAlgorithm */
    public AbstractMGAAlgorithm() {
    }

    /**
     * Initialization.
     */
    @Override
    protected void init() {
        super.init();
        generations = 0;
        setMicrogenerations(0);
        setFEs(0);
        setFinish(false);
    }

    /**
     * Performs the algorithm.
     */
    @Override
    public void resolve(StopTest objective) {

        /* Initialize algorithm. */
        state = INIT_STATE;
        setStopTest(objective);
        init();

        setMaxGenerations(objective);

        /* While the global objective is not reached */
        while (!objective.isReach(this) && !getFinish()) {

            /* Perform the operations needed before micro-evolution. */
            beforeMicroEvolution();

            /* Create the new population that will be generated. */
            Population newPopulation = new Population();

            /* While neither nominal covergence nor the global objective has
             * been reached
             */
            setMaxGenerations(objective);
            setMicrogenerations(0);
            while (!getNominalConvergence().isReach(this) && !objective.isReach(this) && !getFinish()) {

                /* Select the new population. */
                this.state = SELECT_STATE;
                this.select(newPopulation);

                /* Perform reproduction on the selected population. */
                this.state = REPRODUCTION_STATE;
                this.reproduce(newPopulation);

                /* Evaluate the new generated population. */
                this.state = EVALUATE_STATE;
                this.evaluate(getProblem(), newPopulation);

                this.state = REPLACE_STATE;
                this.replace(newPopulation);

                /* Reset all objective functions. */
                getProblem().resetObjectiveFunctions();

                /* Next micro-evolution reproduce. */
                setMicrogenerations(getMicrogenerations() + 1);
            }

            /* Perform the operations needed after the micro-evolution.*/
            afterMicroEvolution();

            /* Next global evolution. */
            this.generations++;
        }

        /* Algorithm's end. */
        state = FINAL_STATE;
        setChanged();
        notifyObservers();
    }

    /**
     * Performs the operations needed for the next micro-evolution.
     */
    protected abstract void beforeMicroEvolution();

    /**
     * Performs the operations needed after a micro-evolution.
     */
    protected abstract void afterMicroEvolution();

    /**
     * Configures the algorithm.
     */
    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);

            if (conf.containsKey("Elitism")) {
                this.setElitism(conf.getInt("Elitism"));
            } else {
                this.setElitism(1);
                (new ConfWarning("AbstractMGAAlgorithm.Elitism", 1)).warn();
            }

            if (conf.containsKey("NominalConvergence.Class")) {
                this.setNominalConvergence(
                        (StopTest) Class.forName(
                        conf.getString("NominalConvergence.Class")).newInstance());
            } else {
                (new ConfWarning("AbstractMGAAlgorithm.NominalConvergence",
                        "MicroGenerationsConvergence")).warn();
                this.setNominalConvergence(new MicroGenerationsConvergence(3));
            }

            this.getNominalConvergence().configure(conf.subset("NominalConvergence"));
        } catch (Exception ex) {
            throw new ConfigurationException(this.getClass(), ex);
        }
    }

    /* ---- Getters and setters follow: ---- */
    /**
     * @return The initial solutions for the next micro evolution.
     */
    public List<Individual> getInitialSolutions() {
        return initialSolutions;
    }

    /**
     * Sets the initial solutions for the next micro evolution.
     * @param initialSolutions The new initial solutions.
     */
    public void setInitialSolutions(List<Individual> initialSolutions) {
        this.initialSolutions = initialSolutions;
    }

    /**
     * @return The elitism.
     */
    public int getElitism() {
        return elitism;
    }

    /**
     * Sets the elitism for this algorithm.
     * @param elitism The new elitism.
     */
    public void setElitism(int elitism) {
        this.elitism = elitism;
    }

    /**
     * @return The current amount of micro generations.
     */
    public int getMicrogenerations() {
        return microgenerations;
    }

    /**
     * Sets The current amount of micro generations.
     * @param microgenerations The new amount of micro generations.
     */
    public void setMicrogenerations(int microgenerations) {
        this.microgenerations = microgenerations;
    }

    /**
     * @return The stop test for nominal convergence.
     */
    public StopTest getNominalConvergence() {
        return nominalConvergence;
    }

    /**
     * Sets the stop test for nominal convergence.
     * @param nominalConvergence
     */
    public void setNominalConvergence(StopTest nominalConvergence) {
        this.nominalConvergence = nominalConvergence;
    }
}
