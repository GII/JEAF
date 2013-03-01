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
 * SimpleStopTest.java
 *
 * Created on 29 de noviembre de 2006, 19:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import org.apache.commons.configuration.Configuration;

/**
 * This abstract class implements the interface <tt>StopTest</tt>. This concrete
 * implementation represents a test with a simple objective. This means that
 * it's an unique objective and it isn't made up by more than one objectives.<p>     
 *     
 * To implement a concrete stop test the user must extend this class and 
 * override the methods <tt>isReach</tt> and <tt>configure</tt>.<p>
 * 
 * The method <tt>isReach</tt> implements <i>visitor</i> pattern. It recieve a 
 * parameter representing the running algorithm and from it obtain necessary 
 * values to test if the objective is reached or not. This method returns
 * <tt>true</tt> when the algorithm reach the objective and <tt>false</tt> in
 * other case. <p>
 * 
 * The method <tt>configure</tt> is the one wich configure a concrete stop
 * test. It recieve a {@see Configuration} object with the configuration values of the specific class.
 * This parameteres are read from a configuration file and
 * parameters' names will match with the name of an atribute of the concrete 
 * stop test. For instance, with a XML configuration file the stop test 
 * configuration must be like this:<p>
 * 
 * <pre>
 * {@code
 *  <StopTest>
 *      <Class>es.udc.gii.common.eaf.stopTest.EvolveGenerationsStopTest</Class>
 *      <Generations>1000</Generations>
 * </StopTest>
 * }
 * </pre>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class SimpleStopTest implements StopTest {

    /** Creates a new instance of SimpleStopTest */
    public SimpleStopTest() {
    }

    @Override
    public void reset(EvolutionaryAlgorithm algorithm) {
        /* empty */
    }
}
