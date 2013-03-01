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
 * BestIndividualSpecification.java
 *
 * Created on 4 de diciembre de 2006, 13:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.productTrader.specification;

import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class BestIndividualSpecification implements Specification {

    /** Creates a new instance of BestIndividualSpecification */
    public BestIndividualSpecification() {
    }

    @Override
    public <T extends Individual> List<T> get(List<T> individuals, int n,
            FitnessComparator<? super T> comparator) {
        List<T> copy = new ArrayList<T>(individuals.size());
        copy.addAll(individuals);
        Collections.sort(copy, comparator);
        return copy.subList(0, n);
    }
}
