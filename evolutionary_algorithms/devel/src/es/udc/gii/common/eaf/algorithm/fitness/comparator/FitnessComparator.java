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


package es.udc.gii.common.eaf.algorithm.fitness.comparator;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.Comparator;

/**
 * This abstract class implements the interface {@link java.util.Comparator}. It
 * represents a comparision fitness function, which could be used to order a 
 * collection of individuals using its fitness value using methos of 
 * {@link java.util.Collections} class.<p>
 * 
 * The user should extend this class and implemented the method <tt>compare</tt>.
 * This method should receive two individuals, in other case it will throw a
 * <tt>ClassCastException</tt>.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class FitnessComparator<T extends Individual> implements Comparator<T> {
    
    /**
     * Compares to individuals inducing an ordering.
     * 
     * Note: this comparator imposes orderings that are inconsistent with equals().
     */
    @Override
    public abstract int compare(T o1, T o2);
    
    
}

