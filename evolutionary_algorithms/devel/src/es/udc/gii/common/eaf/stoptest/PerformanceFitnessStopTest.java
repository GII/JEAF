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
 * PerformanceFitnessStopTest.java
 *
 * Created on 18 de octubre de 2006, 21:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.config.Configurable;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements the abstract class <tt>SimpleStopTest</tt>. To reach the
 * implemented objective in this class the fitness value of the best population
 * individual must reach a value specified on the configuration file.
 *
 * To use and configure this stop test, you must add the following xml code in the appropriate
 * section of the configuration file:
 *
 * <pre>
 * {@code
 * <StopTest>
 *      <Class>es.udc.gii.common.eaf.stoptest.PerformanceFitnessStopTest</Class>
 *      <Goal>value</Goal>
 * </StopTest>
 * }
 * </pre>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class PerformanceFitnessStopTest extends SimpleStopTest
        implements Configurable {

    /**
     * This attribute represents the desired fitness value that has to reach an
     * algorithm to finish its execution.
     */
    private double goal;

    /**
     * Creates a new instance of PerformanceFitnessStopTest
     */
    public PerformanceFitnessStopTest() {
    }

    /**
     *  Creates a new instance of PerformanceFitnessStopTest
     * @param goal Represents the fitness value that has to reach an algorithm to finish its
     * execution.
     */
    public PerformanceFitnessStopTest(double goal) {

        this.goal = goal;

    }

    /**
     * Configure this stop test.
     * @param conf Configuration object which contains the configuration values.
     */
    @Override
    public void configure(Configuration conf) {
        this.goal = conf.getDouble("Goal");
    }

    /**
     * Implements the method <tt>isReach</tt>. Test if the best individual of the
     * current algorithm reach a fitness value that is specified on the configuration
     * file.
     * @param algorithm The algorithm wich has to reach the objective
     * @return <tt>true</tt> if the algorithm reach the desired fitness value, <tt>false</tt>
     * in other case.
     */
    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {

        BestIndividualSpecification bestSpec =
                new BestIndividualSpecification();
        Individual best = IndividualsProductTrader.get(bestSpec,
                algorithm.getPopulation().getIndividuals(), 1, algorithm.getComparator()).get(0);
        Individual individual_goal = (Individual) best.clone();
        individual_goal.setFitness(this.goal);
        boolean reach = false;

        double problemFitness =
                best.getFitness();
                
        if (problemFitness == Double.MAX_VALUE) {
            return false;
        }
            
        int compare = algorithm.getComparator().compare(best, individual_goal);

//        return this.goal <= Math.abs(problemFitness);

        reach = (compare > 0 ? false : true);

        return reach;
    }

    /**
     * Returns a String representing this objective.
     * @return a string representing this objective.
     */
    @Override
    public String toString() {

        return "Min. Fitness Value = " + this.goal;

    }
}
