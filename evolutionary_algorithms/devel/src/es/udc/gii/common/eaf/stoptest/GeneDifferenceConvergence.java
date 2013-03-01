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
 * This class implements a simple stop test. The stop condition is met if the
 * absolute difference between the gene values of the same genes of each pair of
 * indivuals is less or equal than some user defined limit.<p/>
 *
 * More formally, let (i, j) be a pair of individuals where i &ne; j. Further let i[k]
 * denote the k-th gene value of the individual i. Then the stop condition is met
 * if and only if: &forall; (i,j) abs(i[k] - j[k]) &le; maxDifference, &forall;k.
 * maxDifference is the limit imposed by the user for considering to genes
 * as equal. This stop test can thus be used to test the convergence of a
 * population.<p/>
 *
 * From the definition above it's clear that all individuals must have the same
 * number of genes and chromosomes. The individuals may have any number of
 * chromosomes though.<p/>
 *
 * For configuring this stop test:
 *
 * <pre>
 *      <Class>es.udc.gii.common.eaf.stoptest.GeneDifferenceConvergence</Class>
 *      <MaxDifference>10e-6</MaxDifference>
 * </pre>
 *
 * If the maxDifference paramenter is not set, 10e-6 is assumed as the default
 * value.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class GeneDifferenceConvergence extends SimpleStopTest {

    private double maxDifference = 10e-6;

    private boolean exceedsDifference(Individual i1, Individual i2) {
        DoubleArray[] ci1 = i1.getChromosomes();
        DoubleArray[] ci2 = i2.getChromosomes();

        for (int i = 0; i < ci1.length; i++) {
            for (int j = 0; j < ci1[i].getNumElements(); j++) {
                if (Math.abs(ci1[i].getElement(j) - ci2[i].getElement(j))
                        > this.maxDifference) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {
        List<Individual> inds = algorithm.getPopulation().getIndividuals();

        for (int i = 0; i < inds.size(); i++) {
            for (int j = i + 1; j < inds.size(); j++) {
                if (exceedsDifference(inds.get(i), inds.get(j))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void configure(Configuration conf) {
        if (conf.containsKey("MaxDifference")) {
            this.maxDifference = conf.getDouble("MaxDifference");
        } else {
            (new ConfWarning("MaxDifference", 10e-6)).warn();
        }
    }
}
