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
 * BitwiseConvergence.java
 *
 * Created on December 3, 2007, 10:21 PM
 *
 */
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a simple stoptest. The objective is reached when
 * the current population of the passed evolutionary algorithm has converged
 * to a specified rate. The convergence is messured as the average number of bits
 * that each two individuals from the population share.<p>
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
 * This class uses an algorithm with a computational cost of O(n**2), beeing n
 * the number of individuals of the population of the passed evolutionary
 * algorithm. So, consider using other approaches for calculating the population's
 * convergence (such as a fixed number of generations) if you plan to use an
 * algorithm with a big population.
 *
 * To use and configure this stop test, you must add the following xml code in the appropriate
 * section of the configuration file:
 *
 * <pre>
 * {@code
 * <StopTest>
 *      <Class>es.udc.gii.common.eaf.stoptest.BitwiseConvergence</Class>
 *      <ConvergenceRate>value</ConvergenceRate>
 * </StopTest>
 * }
 * </pre>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class BitwiseConvergence extends SimpleStopTest {

    private double convergenceRate = 1.0;

    /** Creates a new instance of BitwiseConvergence */
    public BitwiseConvergence() {
    }

    /**
     * Calculates the convergence rate between two individuals.
     */
    private double convergence(Individual i1, Individual i2) {

        double convergence = 0.0;

        /* Asume both individuals have the same number of genes !! */
        int genes = i1.getChromosomeAt(0).length;

        /* For each pair of genes */
        for (int i = 0; i < genes; i++) {

            /* Get the value of the genes. Note that only individuals which have
             * a double as an internal value are considered. */
            double d1 = i1.getChromosomeAt(0)[i];
            double d2 = i2.getChromosomeAt(0)[i];

            /* Get the binary codification of the values. */
            Long lg1 = new Long(Double.doubleToRawLongBits(d1));
            Long lg2 = new Long(Double.doubleToRawLongBits(d2));

            /* Perform a bitwise XOR operation. Bitpositions that are identical
             * will yield a 0 and bitpositions which differ will yield a 1. So
             * we are counting the bits in which the two individuals *differ* */
            Long lg = new Long(lg1.longValue() ^ lg2.longValue());

            /* Count the number of bits in which the two individuals differ. */
            convergence += Long.bitCount(lg);
        }

        /* Get the average bitwise difference. */
        convergence /= Long.SIZE * genes;

        /* Get the average convergence. */
        convergence = 1 - convergence;

        return convergence;
    }

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {
        List<Individual> individuals = algorithm.getPopulation().getIndividuals();

        double convergence = 0.0;

        /* For each pair of individuals */
        for (int i = 0; i < individuals.size(); i++) {
            for (int j = 0; j < individuals.size(); j++) {
                /* Calculate their convergence rate. */
                convergence += convergence(individuals.get(i), individuals.get(j));
            }
        }

        /* Find the average convergence rate. */
        convergence = convergence / (individuals.size() * individuals.size());

        /* Return true if the desired convergence has been reached. */
        return convergence >= this.convergenceRate;
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
        return "Bitwise convergence stoptest (convergence rate = "
                + convergenceRate + ")";
    }
}
