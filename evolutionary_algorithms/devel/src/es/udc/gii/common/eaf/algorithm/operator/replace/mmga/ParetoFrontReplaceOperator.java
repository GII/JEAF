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
 * ParetoFrontReplaceOperator.java
 *
 * Created on December 10, 2007, 2:40 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.operator.replace.mmga;

import es.udc.gii.common.eaf.util.MOUtil;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.mga.MMGAAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.replace.ReplaceOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a replace operator for the Pareto-front of
 * {@link MMGAAlgorithm}. It uses an adaptative grid (hypercube) for managing
 * the unformity of the Pareto-front.<p/>
 *
 * Configuration example:
 *
 * <pre>
 * {@code
 * <MaximumParetoFrontSize>100</MaximumParetoFrontSize>
 * <HypercubeDivisions>25</HypercubeDivisions>
 * }
 * </pre>
 *
 * The values showed are the default values for these parameters. If some
 * parameter is not explicitly set, it takes the default value.<p/>
 *
 * The {@code MaximumParetoFrontSize} states how big the Pareto front can get,
 * i.e. how many solutions in the Pareto front are allowed at most.<p/>
 * 
 * The {@code HypercubeDivisions} parameter states how many divisions per
 * dimension has the hypercube used for crowding.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ParetoFrontReplaceOperator extends ReplaceOperator {

    private int maximumParetoFrontSize = -1;
    private int hypercubeDivisions = -1;

    /** Creates a new instance of ParetoFrontReplaceOperator */
    public ParetoFrontReplaceOperator() {
    }

    @Override
    protected List<Individual> replace(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation) {

        /* MMGAAlgorithm required! */
        MMGAAlgorithm mmga = (MMGAAlgorithm) algorithm;

        /* Construct a list with the current Pareto-front and the converged
         * population.*/
        List<Individual> individuals = new ArrayList<Individual>(toPopulation.size());
        individuals.addAll(toPopulation);

        /* Ignore individuals which do not satisfy constraints or are already
         * members of the Pareto-front.*/
        for (int i = 0; i < mmga.getPopulation().getIndividuals().size(); i++) {
            Individual ind = mmga.getPopulation().getIndividuals().get(i);

            if (ind.getViolatedConstraints() > 0 && !individuals.contains(ind)) {
                individuals.add(ind);
            }
        }

        /* Keep only the non-dominated individuals. */
        individuals =
                MOUtil.findNonDominatedIndividuals(individuals, MOUtil.FIND_ALL);

        /* If we have more individuals than desired */
        if (individuals.size() > getMaximumParetoFrontSize()) {
            /* Costruct a hypercube with the Pareto-front. */
            Hypercube hypercube =
                    new Hypercube(individuals, mmga.getProblem().getObjectiveFunctions().size(),
                    getHypercubeDivisions());

            /* While we have more individuals than desired */
            while (hypercube.getIndividuals().size() > getMaximumParetoFrontSize()) {
                /* Get the cell with the most individuals. I.e., get the piece
                 * of objective space that has the greatest density.
                 */
                Hypercube.Cell mostCrowdedCell = hypercube.getMostCrowdedCell();

                /* And remove one of the individuals of that cell. I.e., decrement
                 * the density of individuals in that piece of objective space.
                 */
                mostCrowdedCell.removeIndividual(mostCrowdedCell.getRandomIndividual());
            }

            /* Finally, get the resulting Pareto-front. */
            individuals = hypercube.getIndividuals();
        }

        return individuals;
    }

    @Override
    public void configure(Configuration conf) {
        /* Default values */
        int maxPFS = 100;
        int hypDiv = 25;
        
        if(conf.containsKey("MaximumParetoFrontSize")) {
            maxPFS = conf.getInt("MaximumParetoFrontSize");
        } else {
            (new ConfWarning("ParetoFrontReplaceOperator.MaximumPareto" +
                    "FrontSize", maxPFS)).warn();
        }
        
        if(conf.containsKey("HypercubeDivisions")) {
            hypDiv = conf.getInt("HypercubeDivisions");
        } else {
            (new ConfWarning("ParetoFrontReplaceOperator.HypercubeDivisions",
                    hypDiv)).warn();
        }
        
        setMaximumParetoFrontSize(maxPFS);
        setHypercubeDivisions(hypDiv);
    }

    /**
     * @return The maximum size of the Pareto front.
     */
    public int getMaximumParetoFrontSize() {
        return maximumParetoFrontSize;
    }

    /**
     * Sets the maximum size of the Pareto front.
     * @param maximumParetoFrontSize The new maximum Pareto front size.
     */
    public void setMaximumParetoFrontSize(int maximumParetoFrontSize) {
        this.maximumParetoFrontSize = maximumParetoFrontSize;
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
}
