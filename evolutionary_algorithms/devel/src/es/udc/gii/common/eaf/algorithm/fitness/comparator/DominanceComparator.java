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
 * DominanceComparator.java
 *
 * Created on November 15, 2007, 12:31 PM
 *
 */

package es.udc.gii.common.eaf.algorithm.fitness.comparator;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.MOUtil;

/**
 * This class implements a comparator for comparing two instances i1 and i2 of
 * Individual based on their dominance.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class DominanceComparator<T extends Individual> extends FitnessComparator<T>{
    
    /** Creates a new instance of DominanceComparator */
    public DominanceComparator() {
    }
  
    /**
     * 
     * Compares two individuals based on their dominance.<p/>
     *
     * IMPORTANT: It is assumed that all objectives are minimized.
     *
     * @param o1 First individual to compare.
     * @param o2 Second individual to compare.
     * @return If o1 dominates o2 it returns -1. If o2 dominates o1 it returns 1.
     * If o1 and o2 are non-dominated it returns 0.
     */
    @Override
    public int compare(T o1, T o2) {                        
        
        if(MOUtil.checkDominance(o1, o2) == MOUtil.FIRST_DOMINATES) {
            return -1;
        } else if(MOUtil.checkDominance(o1, o2) == MOUtil.SECOND_DOMINATES) {
            return 1;
        } else {
            return 0;
        }
    }
    
}
