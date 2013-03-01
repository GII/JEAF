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
 * IntersectionAcceptancePolicy.java
 *
 * Created on March 20, 2008, 4:49 PM
 *
 */

package es.udc.gii.common.eaf.algorithm.parallel.migration.acceptance;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a binary acceptance policy. It represents the intersection
 * of its left and right component. See {@link BinaryAcceptancePolicy} for
 * details.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class IntersectionAcceptancePolicy extends BinaryAcceptancePolicy {
    
    /** Creates a new instance of IntersectionAcceptancePolicy */
    public IntersectionAcceptancePolicy() {
    }

    /**
     * Implements an intersection of the two lists passed.
     */
    @Override
    protected List<Individual> binaryOperation(List<Individual> acceptedFromLeft, 
        List<Individual> acceptedFromRight) {
        
        List<Individual> intersection = new ArrayList<Individual>();
        
        if(!acceptedFromLeft.isEmpty() && !acceptedFromRight.isEmpty()) {
           
            List<Individual> l1 = null;
            List<Individual> l2 = null;
            
            if(acceptedFromLeft.size() < acceptedFromRight.size()) {
                l1 = acceptedFromLeft;
                l2 = acceptedFromRight;
            } else {
                l1 = acceptedFromRight;
                l2 = acceptedFromLeft;
            }
            
            for(int i = 0; i < l1.size(); i++) {
                if(l2.contains(l1.get(i))) {
                    intersection.add(l1.get(i));
                }
            }
        }
        
        return intersection;
    }

    @Override
    public String toString() {
        return "(" + getLeftComponent() + " intersec " + getRightComponent() + ")";
    }
    
}
