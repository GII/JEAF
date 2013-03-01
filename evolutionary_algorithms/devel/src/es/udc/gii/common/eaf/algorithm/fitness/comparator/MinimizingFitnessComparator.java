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
 * LessFitnessComparator.java
 *
 * Created on 24 de octubre de 2006, 19:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.fitness.comparator;

import es.udc.gii.common.eaf.algorithm.population.Individual;

/**
 * This class implements a comparator for comparing two instances i1 and i2 of
 * Individual based on their fitness value. This class is used in minimizing problems.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MinimizingFitnessComparator<T extends Individual> extends FitnessComparator<T> {

    public MinimizingFitnessComparator() {
    }

        /**
     *
     * Compares two individuals based on their fitness value.
     *
     * @param o1 First individual to compare.
     * @param o2 Second individual to compare.
     * @return If the fitness value of o1 is lower than the fitness value of o2 returns -1. If
     * the fitness value of o2 is lower than the fitness value of o1 returns 1. If the fitness value
     * of o1 is equals than the fitness value of o2 returns 0.
     */
    @Override
    public int compare(T o1, T o2) {
        if (o1.getFitness() < o2.getFitness()) {
            return -1;
        } else if (o1.getFitness() > o2.getFitness()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Minimizing Fitness Comparator";
    }
}
