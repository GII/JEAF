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
 * CompositeStopTest.java
 *
 * Created on 29 de noviembre de 2006, 19:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements the interface <tt>StopTest</tt>. A <tt>CompositeStopTest</tt>
 * consists of a <tt>SimpleStopTest</tt> list. To reach this objective an algorithm
 * must reach at least one of these objectives.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class CompositeStopTest implements StopTest {
    
    /**
     * Condicitions' list of a composite stop test.
     */
    private List<StopTest> stopTests;
    
    /**
     * Creates a new instance of CompositeStopTest
     */
    public CompositeStopTest() {
        
        this.stopTests = new ArrayList<StopTest>();
        
    }

    /**
     * Configure this stop test.
     * @param conf Configuration object which contains the configuration values.
     */
    @Override
    public void configure(Configuration conf) {        
    }
    
    /**
     * Return the list of stop test that contains this Composite stop test.
     * @return a list of stop test.
     */
    public List<StopTest> getStopTests() {
        return this.stopTests;
    }
    
    /**
     * Add a stop test to the list of a composite stop test.
     * @param stopTest A stop test to be added to the stop tests' list.
     */
    public void addStopTest(StopTest stopTest) {
        this.stopTests.add(stopTest);
    }

    /**
     * Test if an algorithm reach the condition represented by this stop test. This 
     * class represents a concrete stop test, so to reach the represented objective
     * the algorithm has to reach almost one of the stop tests.
     * @param algorithm the algorithm wich has to reach the objective
     * @return <tt>true</tt> if the problem has reached the objective, <tt>false</tt> in
     * other case.
     */
    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {
        
        boolean isReach = false;
        
        for (int i = 0; i<this.stopTests.size(); i++) {
            
            if (this.stopTests.get(i).isReach(algorithm))
                return true;
            
        }
        
        return isReach;
        
    }

    @Override
    public void reset(EvolutionaryAlgorithm algorithm) {
       if(this.stopTests != null) {
           for(StopTest st : this.stopTests) {
               st.reset(algorithm);
           }
       }
    }
}
