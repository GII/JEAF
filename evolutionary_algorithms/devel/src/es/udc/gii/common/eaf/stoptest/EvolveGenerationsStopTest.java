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
 * generations.
 *
 * <pre>
 * {@code
 * <StopTest>
 *      <Class>es.udc.gii.common.eaf.stoptest.EvolveGenerationsStopTest</Class>
 *      <Generations>value</Generations>
 * </StopTest>
 * }
 * </pre>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EvolveGenerationsStopTest extends SimpleStopTest
        implements Configurable {

    /**
     * Number of generations that a problem has to reach.
     */
    private int generations = 1;

    /**
     * Creates a new instance of EvolveGenerationsStopTest
     */
    public EvolveGenerationsStopTest() {
    }

    /**
     * Create a new instance of EvoleGenerationsObjective.
     * @param generations Generations' number that a concrete problem has to reach.
     */
    public EvolveGenerationsStopTest(int generations) {

        this.generations = generations;

    }

    /**
     * Configure this stop test.
     * @param conf Configuration object which contains the configuration values.
     */
    @Override
    public void configure(Configuration conf) {
        this.generations = conf.getInt("Generations");
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

        int problemGeneration = algorithm.getGenerations();

        return problemGeneration == this.generations;

    }

    /**
     * Returns the number of generations that the algorithm will run.
     * @return number of generations.
     */
    public int getGenerations() {
        return this.generations;
    }

    /**
     * Set the value of the number of generations that the algorithm will run.
     * @param generations new number of generations.
     */
    public void setGenerations(int generations) {
        this.generations = generations;
    }

    /**
     * Returns a String representing this objective.
     * @return a String representing this objective.
     */
    @Override
    public String toString() {

        return "Max. evolve generations = " + this.generations;

    }
}
