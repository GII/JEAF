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

import es.udc.gii.common.eaf.algorithm.population.NSGA2Individual;

/**
 * Implements the crowding comparison operator.<p/>
 * Given two NSGA2Individuals i1 and i2, i1 &lt; i2 iif i1.rank &lt; i2.rank or 
 * i1.rank = i2.rank and i1.crowding &gt; i2.crowding.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class NSGA2Comparator<T extends NSGA2Individual>
        extends FitnessComparator<T> {

    /**
     *
     * Compares two individuals based on their rank and crowding distance.
     *
     * @param o1 First individual to compare.
     * @param o2 Second individual to compare.
     * @return If the o1 rank is lower than the o2 rank returns -1. If the o2's
     * rank is lower than the o1's rank returns 1. If the two individual have the
     * same rank, if the o1's crowding distance is greater than the o2's crowding
     * distance, returns -1. If the o2's crowding distance is greater than the
     * o1's crowding distance, returns 1. If the two individuals have the same
     * crowding distance, returns 0.
     */
    @Override
    public int compare(T o1, T o2) {
        if (o1.getRank() < o2.getRank()) {
            return -1;
        } else if (o1.getRank() > o2.getRank()) {
            return 1;
        } else {
            if (o1.getCrowdingDistance() > o2.getCrowdingDistance()) {
                return -1;
            } else if (o1.getCrowdingDistance() < o2.getCrowdingDistance()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
