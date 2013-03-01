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
 * EvolveGenerationsStopTest.java
 *
 * Created on 18 de octubre de 2006, 20:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.config.Configurable;
import org.apache.commons.configuration.Configuration;

/**
 * Concrete simple objective. The problem must complete a number of given
 * function evaluations.
 *
 * <pre>
 * {@code
 * <StopTest>
 *      <Class>es.udc.gii.common.eaf.stoptest.MaxFEsStopTest</Class>
 *      <MaxFEs>value</MaxFEs>
 * </StopTest>
 * }
 * </pre>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MaxFEsStopTest extends FEsStopTest
        implements Configurable {

    /**
     * Number of FEs that a problem has to reach.
     */
    private int fes = 1;

    /**
     * Creates a new instance of MaxFEsStopTest
     */
    public MaxFEsStopTest() {
    }

    /**
     * Create a new instance of MaxFEsStopTest.
     * @param fes FEs' number that a concrete problem has to reach.
     */
    public MaxFEsStopTest(int fes) {

        this.fes = fes;

    }

    /**
     * Configure this stop test.
     * @param conf Configuration object which contains the configuration values.
     */
    @Override
    public void configure(Configuration conf) {
        this.fes = conf.getInt("MaxFEs");
    }

    /**
     * Returns <tt>true</tt> if the problem had done the generation's number 
     * determined by this concrete objective.
     * @return <tt>true</tt> if the problem do a number of generations, <tt>false</tt> in
     * other case.
     * @param algorithm the algorithm wich has to reach the objective
     */
    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {

        int problemFEs = algorithm.getFEs();

        return problemFEs >= this.fes;

    }

    /**
     * Returns a String representing this objective.
     * @return a String representing this objective.
     */
    @Override
    public String toString() {

        return "Max. functions evaluations = " + this.fes;

    }

    /**
     * Returns the maximun nunmber of function evaluations that the algorithm will perform.
     * @return maximum number of function evaluations.
     */
    @Override
    public int getFEs(EvolutionaryAlgorithm algorithm) {
        return this.fes;
    }

    @Override
    public void reset(EvolutionaryAlgorithm algorithm) {
        /* empty */
    }
}
