/*
 *  Copyright (C) 2011 Grupo Integrado de Ingeniería
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.util.DoubleArray;

/**
 * This class implements a simple stop test. It tests for the convergence of
 * the population considering the mean ratio of pairwise equal genes between any
 * two individuals in the population.<p/>
 *
 * Configuration:
 * 
 * <pre>
 * {@code
 * <StopTest>
 *      <Class>es.udc.gii.common.eaf.stoptest.GeneRateConvergence</Class>
 *      <ConvergenceRate>value</ConvergenceRate>
 *      <MaxDifference>10e-6</MacDifference>
 * </StopTest>
 * }
 * </pre>
 *
 * The convergence rate parameter sets the expected mean ratio of pairwise equal
 * genes between any two individuals in the population. For example, with a
 * value of 0.9, the stop condition is met once any two individuals share at
 * least 90% of their genes in mean. That means that there might be some pair of
 * individuals which share more genes, and there might be some pair of
 * individuals which share less genes than the 90% of their genome, but the mean
 * of all pairs of individuals share at least 90% of their genes.<p/>
 *
 * The max difference parameter is the maximum absolute difference for 
 * considering two gene values as equal. Formally gene g1 is considered equal
 * to gene g2 if and only if: abs(value(g1) - value(g2)) <= max difference.<p/>
 * 
 * All individuals must have the same number of genes and chromosomes. Any
 * number of chromosomes is supported.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class GeneRateConvergence extends SimpleStopTest {

    private double maxDifference;
    private double convergenceRate;

    private double convergenceRate(Individual i1, Individual i2) {

        double rate = 0;
        int total = 0;
        DoubleArray[] ci1 = i1.getChromosomes();
        DoubleArray[] ci2 = i2.getChromosomes();

        for (int i = 0; i < ci1.length; i++) {
            for (int j = 0; j < ci1[i].getNumElements(); j++) {
                total++;
                if (Math.abs(ci1[i].getElement(j) - ci2[i].getElement(j))
                        <= this.maxDifference) {
                    rate += 1;
                }
            }
        }

        return rate / total;
    }

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {

        List<Individual> inds = algorithm.getPopulation().getIndividuals();

        if (inds.isEmpty() || inds.size() == 1) {
            return true;
        }

        double rate = 0;
        int totalPairs = 0;

        for (int i = 0; i < inds.size(); i++) {
            for (int j = i + 1; j < inds.size(); j++) {
                totalPairs++;
                rate += convergenceRate(inds.get(i), inds.get(j));
            }
        }

        return rate / totalPairs >= this.convergenceRate;
    }

    @Override
    public void configure(Configuration conf) {
        if (conf.containsKey("MaxDifference")) {
            this.maxDifference = conf.getDouble("MaxDifference");
        } else {
            (new ConfWarning("MaxDifference", 10e-6)).warn();
        }

        if (conf.containsKey("ConvergenceRate")) {
            this.convergenceRate = conf.getDouble("ConvergenceRate");
        } else {
            (new ConfWarning("ConvergenceRate", 0.9)).warn();
        }
    }
}
