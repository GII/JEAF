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
 * UnionAcceptancePolicy.java
 *
 * Created on March 20, 2008, 4:58 PM
 *
 */

package es.udc.gii.common.eaf.algorithm.parallel.migration.acceptance;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a binary acceptance policy. It represents the union of its left
 * and right components. See {@link BinaryAcceptancePolicy} for details.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class UnionAcceptancePolicy extends BinaryAcceptancePolicy{
    
    /** Creates a new instance of UnionAcceptancePolicy */
    public UnionAcceptancePolicy() {
    }

    /**
     * Implements an union of the two lists passed.
     */    
    @Override
    protected List<Individual> binaryOperation(List<Individual> acceptedFromLeft, 
        List<Individual> acceptedFromRight) {
        
        List<Individual> union = new ArrayList<Individual>();
        
        union.addAll(acceptedFromLeft);
        
        for(int i = 0; i < acceptedFromRight.size(); i++) {
            if(!union.contains(acceptedFromRight.get(i))) {
                union.add(acceptedFromRight.get(i));
            }
        }
        
        return union;
    }
    
    @Override
    public String toString() {
        return "(" + getLeftComponent() + " union " + getRightComponent() + ")";
    }    
}
