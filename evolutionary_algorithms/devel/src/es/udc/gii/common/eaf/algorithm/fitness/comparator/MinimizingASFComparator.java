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
 * MinimizingObjectiveComparator.java
 *
 * Created on 21 de diciembre de 2006, 11:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.fitness.comparator;

import es.udc.gii.common.eaf.algorithm.population.multiobjective.MONSGA2Individual;

/**
 * This class compares two individuals considering only one of the objective
 * values of each individual. Lesser values are better, i.e. it minimizes a 
 * given objective.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MinimizingASFComparator<T extends MONSGA2Individual> 
        extends FitnessComparator<T> {

    
    public MinimizingASFComparator() {
        
    }
    

    /**
     * Compares the ASF value of two individuals. 
     * 
     * @param o1 - First individual.
     * @param o2 - Second individual.
     * @return -1 if o1 is better than o2, 1 if o2 is better than o1 and 0 if
     * both individual have the same objective value for the given objective
     * index.
     */
    @Override
    public int compare(T o1, T o2) {
        double o1Value = o1.getASF();
        double o2Value = o2.getASF();
        
        if (o1Value < o2Value) {
            return -1;
        } else if(o1Value > o2Value) {
            return 1;
        } else {
            return 0;
        }

    }


    @Override
    public String toString() {
        return "Minimizing ASF Value Comparator";
    }
}
