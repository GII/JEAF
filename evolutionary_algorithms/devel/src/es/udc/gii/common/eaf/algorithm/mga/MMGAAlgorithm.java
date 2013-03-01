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
 * MMGAAlgorithm.java
 *
 * Created on December 8, 2007, 7:14 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.mga;

import es.udc.gii.common.eaf.util.MOUtil;
import es.udc.gii.common.eaf.algorithm.operator.replace.mmga.ParetoFrontReplaceOperator;
import es.udc.gii.common.eaf.algorithm.operator.replace.mmga.PopulationMemoryReplaceOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.exception.OperatorException;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class is an implementation of a micro-genetic algorithm for problems with
 * multiple objectives. This algorithm uses an external memory for saving the
 * Pareto-optimal solutions and a population memory which is meant to provide
 * the necessary diversity. See "Multiobjective optimization using a genetic algorithm",
 * Carlos A. Coello Coello, Gregorio Toscano Pulido, for further details.<p/>
 *
 * The user has to provide the following:
 * - a stoptest defining nominal convergence
 * - the number of individuals that are preserved among micro-generations (elitism)
 * - a replace operator for replacing the external memory (Pareto-front)
 * - a replace operator for replacing the population memory
 * - the size of the population memory<p>
 * 
 * Configuration example:
 *
 * <pre>
 * &lt;ParetoFrontReplaceOperator&gt;
 *    &lt;Class&gt; es.udc.gii.common.eaf.algorithm.operator.replace.mmga.ParetoFrontReplaceOperator &lt;/Class&gt;
 *    ...
 * &lt;/ParetoFrontReplaceOperator&gt;
 * &lt;PopulationMemorySize&gt; 33 &lt;/PopulationMemorySize&gt;
 * &lt;PopulationMemoryReplaceOperator&gt;
 *    &lt;Class&gt; es.udc.gii.common.eaf.algorithm.operator.replace.mmga.PopulationMemoryReplaceOperator &lt;/Class&gt;
 *    ...
 * &lt;/PopulationMemoryReplaceOperator&gt;
 *
 * </pre>
 *
 * Other configuration parameters depend on the concrete operators used and on
 * those inherit by {@link es.udc.gii.common.eaf.algorithm.mga.AbstractMGAAlgorithm}. <p/>
 *
 * The {@code Class} tags show the default values. Omitting those tags will
 * create instances of those classes. The {@code PopulationMemorySize} might also
 * be omitted. In such a case, the population size will be a third of the
 * maximum Pareto front size configured for the replace operator of the Pareto
 * front. <p/>
 * 
 * The user might provide some initial solutions that will be used as a
 * startpoint for the search (not available for configuration with xml files).
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 * @see AbstractMGAAlgorithm
 */
public class MMGAAlgorithm extends AbstractMGAAlgorithm {

    /**
     * Replace operator for the external memory, i.e. the Pareto-front.
     */
    private ParetoFrontReplaceOperator paretoFrontReplaceOperator = null;
    /**
     * Replace operator for the population memory.
     */
    private PopulationMemoryReplaceOperator populationMemoryReplaceOperator = null;
    /**
     * The population memory.
     */
    private Population populationMemory = null;
    /**
     * The current Pareto-front.
     */
    private Population paretoFront = null;
    /**
     * The size of the population memory.
     */
    private int populationMemorySize = 0;

    /** Creates a new instance of MMGAAlgorithm */
    public MMGAAlgorithm() {
    }

    /**
     * Performs the operations need after a micro-evolution.
     */
    @Override
    protected void afterMicroEvolution() {

        /* Replace the Pareto-front with the new one. */
        try {

            getParetoFront().setIndividuals(
                    getParetoFrontReplaceOperator().operate(
                    this, getParetoFront().getIndividuals()));
        } catch (OperatorException ex) {
            ex.printStackTrace();
            System.exit(0);
        }

        /* Update the population memory. */
        try {

            getPopulationMemory().setIndividuals(
                    getPopulationMemoryReplaceOperator().operate(
                    this, getPopulationMemory().getIndividuals()));
        } catch (OperatorException ex) {
            ex.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Performs the operations needed before each micro-evolution.
     */
    @Override
    protected void beforeMicroEvolution() {

        /* If we are at the begining of the algorithm */
        if (getGenerations() == 0) {

            /* Generate the population memory. */
            getPopulation().generate();
            populationMemory = new Population();
            populationMemory.addIndividual(getPopulation().getIndividual(0));
            populationMemory.setSize(1);

            int numberOfInitialSolutions = 0;

            if (getInitialSolutions() != null) {
                numberOfInitialSolutions = getInitialSolutions().size();
            }

            populationMemory.modifyPopulationSize(populationMemorySize
                    - numberOfInitialSolutions);

            /* Evaluate the initial population memory. */
            state = INIT_EVALUATE_STATE;
            evaluate(getProblem(), populationMemory);

            /* Add the initial solutions if any. */
            if (getInitialSolutions() != null) {
                populationMemory.addIndividuals(getInitialSolutions());
            }

            /* Generate a new Pareto-front. */
            paretoFront = new Population();
            paretoFront.addIndividuals(
                    MOUtil.findNonDominatedIndividuals(
                    populationMemory.getIndividuals(), MOUtil.FIND_ALL));

        }

        /* Construct the new population for the next micro-evolution. */
        state = REPLACE_STATE;
        int populationSize = getPopulation().getSize();
        List<Individual> newPopulation = new ArrayList<Individual>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            int ind = EAFRandom.nextInt(populationMemorySize);
            Individual selectedFromPopulationMemory =
                    populationMemory.getIndividual(ind);
            newPopulation.set(i, selectedFromPopulationMemory);
        }

        getPopulation().setIndividuals(newPopulation);
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Configures the algorithm.
     */
    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);

            if (conf.containsKey("ParetoFrontReplaceOperator.Class")) {
                setParetoFrontReplaceOperator(
                        (ParetoFrontReplaceOperator) Class.forName(
                        conf.getString("ParetoFrontReplaceOperator.Class")).newInstance());
            } else {
                (new ConfWarning("MMGAAlgorithm.ParetoFrontReplaceOperator",
                        "ParetoFrontReplaceOperator")).warn();
                setParetoFrontReplaceOperator(new ParetoFrontReplaceOperator());
            }

            getParetoFrontReplaceOperator().configure(
                    conf.subset("ParetoFrontReplaceOperator"));

            if (conf.containsKey("PopulationMemorySize")) {
                setPopulationMemorySize(conf.getInt("PopulationMemorySize"));
            } else {
                int memSize =
                        getParetoFrontReplaceOperator().getMaximumParetoFrontSize() / 3;

                (new ConfWarning("MMGAAlgorithm.PopulationMemorySize", memSize)).warn();
                setPopulationMemorySize(memSize);
            }

            if (conf.containsKey("PopulationMemoryReplaceOperator.Class")) {
                setPopulationMemoryReplaceOperator(
                        (PopulationMemoryReplaceOperator) Class.forName(
                        conf.getString("PopulationMemoryReplaceOperator.Class")).newInstance());
            } else {
                (new ConfWarning("MMGAAlgorithm.PopulationMemoryReplaceOperator",
                        "PopulationMemoryReplaceOperator")).warn();
                setPopulationMemoryReplaceOperator(new PopulationMemoryReplaceOperator());
            }

            getPopulationMemoryReplaceOperator().setPopMemSize(
                    getPopulationMemorySize());
            getPopulationMemoryReplaceOperator().configure(
                    conf.subset("PopulationMemoryReplaceOperator"));

        } catch (Exception ex) {
            throw new ConfigurationException(this.getClass(), ex);
        }
    }

    public ParetoFrontReplaceOperator getParetoFrontReplaceOperator() {
        return paretoFrontReplaceOperator;
    }

    public void setParetoFrontReplaceOperator(ParetoFrontReplaceOperator paretoFrontReplaceOperator) {
        this.paretoFrontReplaceOperator = paretoFrontReplaceOperator;
    }

    public PopulationMemoryReplaceOperator getPopulationMemoryReplaceOperator() {
        return populationMemoryReplaceOperator;
    }

    public void setPopulationMemoryReplaceOperator(PopulationMemoryReplaceOperator populationMemoryReplaceOperator) {
        this.populationMemoryReplaceOperator = populationMemoryReplaceOperator;
    }

    public Population getPopulationMemory() {
        return populationMemory;
    }

    public void setPopulationMemory(Population populationMemory) {
        this.populationMemory = populationMemory;
    }

    public Population getParetoFront() {
        return paretoFront;
    }

    public void setParetoFront(Population paretoFront) {
        this.paretoFront = paretoFront;
    }

    public int getPopulationMemorySize() {
        return populationMemorySize;
    }

    public void setPopulationMemorySize(int populationMemorySize) {
        this.populationMemorySize = populationMemorySize;
    }

    @Override
    public String toString() {
        return "Micro-genetic algorithm (multiobjective)";
    }

    @Override
    public String getAlgorithmID() {
        return "MMGA";
    }
}
