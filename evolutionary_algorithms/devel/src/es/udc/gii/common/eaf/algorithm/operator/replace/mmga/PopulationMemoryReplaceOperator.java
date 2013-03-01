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
 * PopulationMemoryReplaceOperator.java
 *
 * Created on December 10, 2007, 12:03 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.operator.replace.mmga;

import es.udc.gii.common.eaf.util.MOUtil;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.mga.MMGAAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.replace.ReplaceOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a replace operator for replacing the population memory
 * of {@link MMGAAlgorithm}. It only replaces a part of the population memory.
 * The rest is kept unchanged for providing diversity.<p/>
 *
 * Configuration example:
 * 
 * <pre>
 * {@code
 * <HypercubeDivisions>25</HypercubeDivisions>
 * <ReplacementCycle>4</ReplacementCycle>
 * <ReplaceablePart>0.666</ReplaceablePart>
 * }
 * </pre>
 *
 * The values showed are the default values for these parameters. If some
 * parameter is not explicitly set, it takes the default value.<p/>
 *
 * The {@code HypercubeDivisions} parameter states how many divisions per
 * dimension has the hypercube used for crowding.<p/>
 *
 * The {@code ReplacementCycle} parameter sets the frequency in generations
 * with which a replacement with solutions from the Pareto front is performed
 * instead with those of the converged population (nominal solutions).<p/>
 *
 * The {@code PopulationMemorySize} is the total population size of the population
 * memory.<p/>
 *
 * The {@code ReplaceablePart} is a number between 0 and 1 that sets the portion
 * of the population memory that should be replaced.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class PopulationMemoryReplaceOperator extends ReplaceOperator {

    private int hypercubeDivisions = 0;
    private int replaceablePartSize = 0;
    private int replacementCycle = 0;
    private int currentIndex = -1;
    private int popMemSize;
    
    /** Creates a new instance of PopulationMemoryReplaceOperator */
    public PopulationMemoryReplaceOperator() {
        currentIndex = -1;
    }

    @Override
    protected List<Individual> replace(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation) {

        MMGAAlgorithm mmga = (MMGAAlgorithm) algorithm;

        /* Choose if we replace with the Pareto-front or with the converged
         * population (nominal solutions). */
        if (mmga.getGenerations() % getReplacementCycle() != 0) {
            return replaceWithNominalSolutions(mmga, toPopulation);
        } else {
            return replaceWithParetoFront(mmga, toPopulation);
        }
    }

    @Override
    public void configure(Configuration conf) {
        /* Defaults */
        int hypDiv = 25;
        int repCycle = 4;
        double repPart = 0.666;

        if (conf.containsKey("HypercubeDivisions")) {
            hypDiv = conf.getInt("HypercubeDivisions");
        } else {
            (new ConfWarning("PopulationMemoryReplaceOperator.Hypercube"
                    + "Divisions", hypDiv)).warn();
        }

        if (conf.containsKey("ReplacementCycle")) {
            repCycle = conf.getInt("ReplacementCycle");
        } else {
            (new ConfWarning("PopulationMemoryReplaceOperator.Replacement"
                    + "Cycle", repCycle)).warn();
        }

        if (conf.containsKey("ReplaceablePart")) {
            repPart = conf.getDouble("ReplaceablePart");
        } else {
            (new ConfWarning("PopulationMemoryReplaceOperator.Replaceable"
                    + "Part", repPart)).warn();
        }

        setHypercubeDivisions(hypDiv);
        setReplacementCycle(repCycle);
        setReplaceablePartSize((int) ((double) popMemSize * repPart));
    }

    private int nextIndex() {
        currentIndex = (currentIndex + 1) % replaceablePartSize;
        return currentIndex;
    }

    private List<Individual> replaceWithNominalSolutions(MMGAAlgorithm mmga,
            List<Individual> toPopulation) {

        /* Find a number of non-dominated solutions in the converged population.*/
        List<Individual> nonDominatedIndividuals =
                MOUtil.findNonDominatedIndividuals(mmga.getPopulation().getIndividuals(),
                mmga.getElitism());

        /* For each non-dominated solution */
        for (int i = 0; i < nonDominatedIndividuals.size(); i++) {
            int index = nextIndex();

            Individual ind = nonDominatedIndividuals.get(i);
            Individual pmInd = toPopulation.get(index);

            /* Check if it dominates his match in the population memory, and if
             * so, replace the individual in the population memory. */
            if (MOUtil.checkDominance(ind, pmInd) == MOUtil.FIRST_DOMINATES) {
                toPopulation.set(index, ind);
            }
        }

        return toPopulation;
    }

    private List<Individual> replaceWithParetoFront(MMGAAlgorithm mmga,
            List<Individual> toPopulation) {

        /* Get the Pareto-front. */
        List<Individual> paretoFront = mmga.getParetoFront().getIndividuals();

        /* Do nothing if empty. */
        if (paretoFront.isEmpty()) {
            return toPopulation;
        }

        /* If the Pareto-front is too small */
        if (paretoFront.size() <= getReplaceablePartSize()) {
            /* Copy the entire Pareto-front. */
            for (int i = 0; i < paretoFront.size(); i++) {
                toPopulation.set(nextIndex(), paretoFront.get(i));
            }
        } else { /* If we have not enough place for all Pareto-solutions */

            /* Construct a hypercube and add the Pareto-front to it.*/
            Hypercube hypercube =
                    new Hypercube(paretoFront, mmga.getProblem().getObjectiveFunctions().size(),
                    getHypercubeDivisions());

            /* Get the cells which have some individuals. */
            List<Hypercube.Cell> crowdedCells = hypercube.getCrowdedCells();

            /* This array stores an index for each cell. We will pick up an
             * individual from each cell until the replaceable part of the
             * population memory is full. */
            int[] indexInCell = new int[crowdedCells.size()];

            /* Fill the replaceable part of the population memory. Note that we
             * can choose the same cell several times. We can also take the same
             * individual of a single cell more than once. Therefore we go through
             * this lists in a circular manner (employement of the modulo operator).
             */
            for (int currentCell = 0, replaced = 0; replaced < getReplaceablePartSize();
                    currentCell = (currentCell + 1) % crowdedCells.size(), replaced++) {

                Hypercube.Cell cell = crowdedCells.get(currentCell);
                Individual ind = cell.getIndividual(indexInCell[currentCell]);
                toPopulation.set(replaced, ind);
                indexInCell[currentCell] =
                        (indexInCell[currentCell] + 1) % cell.getIndividualsCount();
            }
        }

        return toPopulation;
    }

    /**
     * @return The the replacement frequency (in generations) of the population
     * memory.
     */
    public int getReplacementCycle() {
        return replacementCycle;
    }

    /**
     * Establishes the replacement frequency (in generations) of the population
     * memory.
     * 
     * @param replacementCycle The new frequency. As an example, setting this
     * frequency to 5 means that every 5 generations a replacement will take
     * place.
     */
    public void setReplacementCycle(int replacementCycle) {
        this.replacementCycle = replacementCycle;
    }

    /**
     * @return The number of divisions per dimension of the hypercube.
     */
    public int getHypercubeDivisions() {
        return hypercubeDivisions;
    }

    /**
     * Estabishes the number of divisions per dimension of the hypercube.
     *
     * @param hypercubeDivisions The new number of divisions per dimension
     * of the hypercube.
     */
    public void setHypercubeDivisions(int hypercubeDivisions) {
        this.hypercubeDivisions = hypercubeDivisions;
    }

    /**
     * @return The number of individuals that are to be replaced from the
     * population memory.
     */
    public int getReplaceablePartSize() {
        return replaceablePartSize;
    }

    /**
     * Sets the number of individuals that are to be replaced from the
     * population memory.
     * @param replaceablePartSize The new number of individuals that are to be 
     * replaced.
     */
    public void setReplaceablePartSize(int replaceablePartSize) {
        this.replaceablePartSize = replaceablePartSize;
    }

    /**
     * @return The size of the population memory.
     */
    public int getPopMemSize() {
        return popMemSize;
    }

    /**
     * Sets the size of the population memory.
     * @param popMemSize The new size of the population memory considered by
     * this operator.
     */
    public void setPopMemSize(int popMemSize) {
        this.popMemSize = popMemSize;
    }
}
