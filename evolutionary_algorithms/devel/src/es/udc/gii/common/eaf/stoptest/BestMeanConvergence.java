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
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.fitness.FitnessUtil;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a simple stoptest. The objective is reached when
 * the current population of the passed evolutionary algorithm has converged
 * to a specified rate. The convergence is messured as the difference between the best fitness value
 * and the mean fitness of the population. So, it is a phenotypic convergence measure.<p>
 *
 * The convergence rate is specified as a configuration parameter. A convergence
 * rate of 1.0 means that all individuals in the population have the same genotype.
 * A convergence rate of 0.x means that, in average, each two individuals from the
 * population share their genotypes at x %.<p>
 *
 * Convergence rates between 0.6 and 0.9 have yield good results for populations
 * of 5 individuals. This class is only suitable for individuals with the same
 * number of genes and for internal values of type 'double'. <p>
 *
 * To use and configure this stop test, you must add the following xml code in the appropriate
 * section of the configuration file:
 *
 * <pre>
 * {@code
 * <StopTest>
 *      <Class>es.udc.gii.common.eaf.stoptest.BestMeanConvergence</Class>
 *      <ConvergenceRate>value</ConvergenceRate>
 * </StopTest>
 * }
 * </pre>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class BestMeanConvergence extends SimpleStopTest {

    private double convergenceRate = 1.0;

    /** Creates a new instance of BitwiseConvergence */
    public BestMeanConvergence() {
    }

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {
        List<Individual> individuals = algorithm.getPopulation().getIndividuals();

        boolean convergence;
        double best_fitness, mean_fitness;

        best_fitness = algorithm.getBestIndividual().getFitness();
        mean_fitness = FitnessUtil.meanFitnessValue(individuals);

        convergence = (Math.abs(best_fitness - mean_fitness) / best_fitness) <= this.convergenceRate;

        return convergence;
    }

    /**
     * Configure this stop test.
     * @param conf Configuration object which contains the configuration values.
     */
    @Override
    public void configure(Configuration conf) {
        this.convergenceRate = conf.getDouble("ConvergenceRate");

    }

    /**
     * Returns a string representation of the object. 
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "BestMean convergence stoptest";
    }
}
