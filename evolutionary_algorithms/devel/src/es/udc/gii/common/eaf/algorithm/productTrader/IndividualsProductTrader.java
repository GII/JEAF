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
 * IndividualsProductTrader.java
 *
 * Created on 30 de noviembre de 2006, 19:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.productTrader;

import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.Specification;
import java.util.List;

/**
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class IndividualsProductTrader {

    /**
     * Creates a new instance of IndividualsProductTrader
     */
    public IndividualsProductTrader() {
    }

    public static <T extends Individual> List<T> get(Specification specification,
            List<T> individuals, int n, FitnessComparator<? super Individual> comparator) {

        return specification.get(individuals, n, comparator);
    }
}
