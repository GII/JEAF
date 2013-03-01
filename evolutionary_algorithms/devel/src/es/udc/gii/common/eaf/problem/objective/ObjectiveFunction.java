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
 * ObjectiveFunction.java
 *
 
 de octubre de 2006, 20:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.problem.objective;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.config.Configurable;
import java.io.Serializable;
import org.apache.commons.configuration.Configuration;

/**
 * 
 * Objective functions are used to determine the value of an {@link Individual}
 * when it is used to resolve a {@link es.udc.gii.common.eaf.problem.Problem}. 
 * This interface should be extended by the user and the <tt>evaluate</tt> 
 * method implemented. <p>
 * 
 * The objective function is given a Individual to evaluate and return an objective
 * value.<p> 
 * 
 * Note: Two individuals with equivalent chromosome should always be
 * assigned the same fitness value by any implementation of this interface.
 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class ObjectiveFunction implements Configurable, Serializable {
    
    public ObjectiveFunction() {}    
    
    /**
     * Return the objective value of an Individual after compute this objective
     * function with the individual's chromosome.
     * @param values the array of double values to evaluate.
     * @return the objective value of the individual in this objective function.
     */
    public abstract double evaluate(double[] values);
    
    public abstract void reset();

    @Override
    public void configure(Configuration conf) {        
    }
    
    public int getDimension() {
        return -Integer.MAX_VALUE;
    }
    
}
