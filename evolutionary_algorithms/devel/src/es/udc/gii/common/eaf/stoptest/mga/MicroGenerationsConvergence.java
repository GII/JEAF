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
 * MicroGenerationsConvergence.java
 *
 * Created on December 3, 2007, 9:45 PM
 *
 */
package es.udc.gii.common.eaf.stoptest.mga;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.mga.AbstractMGAAlgorithm;
import es.udc.gii.common.eaf.stoptest.*;
import es.udc.gii.common.eaf.util.ConfWarning;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a simple stop test for the internal cycle of
 * micro-genetic algorithms. The objective is reached after a number of
 * evaluations (given by the user). Thus, the convergence of the population
 * of the micro-genetic algorithm is defined in terms of a number of generations.<p>
 *
 * For population sizes of 5 individuals (MGA algorithm) good results have been
 * achieved using 2 generations.
 *
 * To use and configure this stop test, you must add the following xml code in the appropriate
 * section of the configuration file: <p>
 * 
 * <pre>
 * {@code
 * <StopTest> 
 *      <Class>es.udc.gii.common.eaf.stoptest.MicroGenerationsConvergence</Class>
 *      <Generations>number of microgenerations</Generations>
 * </StopTest>
 * }
 * </pre>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MicroGenerationsConvergence extends EvolveGenerationsStopTest {

    /** Creates a new instance of MicroGenerationsConvergence */
    public MicroGenerationsConvergence() {
    }

    /** Creates a new instance of MicroGenerationsConvergence
     * @param generation number of generations.
     */
    public MicroGenerationsConvergence(int generation) {
        super(generation);
    }

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {
        return ((AbstractMGAAlgorithm) algorithm).getMicrogenerations() >= getGenerations();
    }

    @Override
    public void configure(Configuration conf) {
        if (conf.containsKey("Generations")) {
            setGenerations(conf.getInt("Generations"));
        } else {
            setGenerations(2);
            (new ConfWarning("MicroGenerationsConvergence.Generations", 2)).warn();
        }
    }
}
