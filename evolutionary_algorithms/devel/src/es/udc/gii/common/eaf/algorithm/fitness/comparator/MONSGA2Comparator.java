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

import es.udc.gii.common.eaf.algorithm.population.multiobjective.MONSGA2Individual;

/**
 * Implements the Many Objective - NSGAII comparison operator as it is described
 * in [1]
 *
 * <ol>
 * <li>Deb, K., & Jain, H. (2012, June). Handling many-objective problems using
 * an improved NSGA-II procedure. In Evolutionary Computation (CEC), 2012 IEEE
 * Congress on (pp. 1-8). IEEE.</li>
 * </ol>
 *
 * @author Grupo Integrado de Ingeniería (<a
 * href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MONSGA2Comparator<T extends MONSGA2Individual>
        extends FitnessComparator<T> {

    /**
     *
     * Compares two individuals based on their rank, deficiency count of their
     * reference points or ASF values.
     *
     * @param o1 First individual to compare.
     * @param o2 Second individual to compare.
     * @return If the o1 rank is lower than the o2 rank returns -1. If the o2's
     * rank is lower than the o1's rank returns 1. If the two individual have
     * the same rank, their reference points are compared. If their reference
     * points are different, if o1's reference point has a higher deficiency
     * count returns -1, else if the o2's reference point has a higher
     * deficiency count returns 1. In other case, if o1 and o2 belong to the
     * same reference point, their ASF is compared. If o1's has a smaller ASF
     * value, this method returns -1. In other case, if o2's ASF value is
     * smaller it returns 1. If the two individuals has the same ASF value, the
     * method returns 0.
     */
    @Override
    public int compare(T o1, T o2) {
        if (o1.getRank() < o2.getRank()) {
            return -1;
        } else if (o1.getRank() > o2.getRank()) {
            return 1;
        } else {

            if (!o1.getReferencePoint().equals(o2.getReferencePoint())) {
                if (o1.getReferencePoint().getDeficiencyCount()
                        > o2.getReferencePoint().getDeficiencyCount()) {
                    return -1;
                } else if (o1.getReferencePoint().getDeficiencyCount()
                        < o2.getReferencePoint().getDeficiencyCount()) {
                    return 1;
                }

            }

            if (o1.getASF() < o2.getASF()) {
                return -1;
            } else if (o1.getASF() > o2.getASF()) {
                return 1;
            } else {
                return 0;
            }
        }

    }
}
