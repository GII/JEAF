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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.config.Configurable;
import org.apache.commons.configuration.Configuration;

/**
 * The maximum numbers of FEs that has to run an algorithm depends on the dimension
 * of the individuals.
 *
 * To use and configure this stop test, you must add the following xml code in the appropriate
 * section of the configuration file:
 *
 * <pre>
 * {@code
 * <StopTest>
 *      <Class>es.udc.gii.common.eaf.stoptest.DimensionFEsStopTest;/Class>
 *      <Factor>value</Factor>
 * </StopTest>
 * }
 * </pre>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class DimensionFEsStopTest extends FEsStopTest
        implements Configurable {

    /**
     * Factor that multiply the dimension of the algorithm and 
     * establish the maximum number of function evaluations
     */
    private int factor = 1;
    /**
     * Dimension of the problem
     */
    private int dimension = 1;

    /**
     * Creates a new instance of DimensionFEsStopTest
     */
    public DimensionFEsStopTest() {
    }

    /**
     * Create a new instance of DimensionFEsStopTest.
     * @param factor factor that multiply the dimension of the algorithm and 
     * establish the maximum number of function evaluations.
     */
    public DimensionFEsStopTest(int factor) {

        this.factor = factor;

    }

    /**
     * Configure this stop test.
     * @param conf Configuration object which contains the configuration values.
     */
    @Override
    public void configure(Configuration conf) {
        this.factor = conf.getInt("Factor");
    }

    /**
     * Returns <tt>true</tt> if the problem had done the number of function 
     * evaluations determined by this concrete objective.
     * @return <tt>true</tt> if the problem do a number of function evaluations, 
     * <tt>false</tt> in other case.
     * @param algorithm the algorithm wich has to reach the objective
     */
    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {

        int problemFEs = algorithm.getFEs();
        this.dimension = algorithm.getPopulation().getIndividual(0).getDimension();

        return problemFEs >= this.factor * this.dimension;

    }

    /**
     * Returns a String representing this objective.
     * @return a String representing this objective.
     */
    @Override
    public String toString() {

        return "Factor = " + this.factor;

    }

    /**
     * Returns the maximun nunmber of function evaluations that the algorithm will perform.
     * @return maximum number of function evaluations.
     */
    @Override
    public int getFEs(EvolutionaryAlgorithm algorithm) {
        return this.factor * algorithm.getPopulation().getIndividual(0).getDimension();
    }
}
