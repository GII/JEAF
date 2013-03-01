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
 * MinimizingGeneValueComparator.java
 *
 * Created on 19 de abril de 2007, 18:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.fitness.comparator;

import es.udc.gii.common.eaf.algorithm.population.Individual;

/**
 * This class implements a comparator for comparing two instances i1 and i2 of
 * Individual based on their genes values.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MinimizingGeneValueComparator<T extends Individual> extends FitnessComparator<T> {

    private int geneIndex;

    /** Creates a new instance of MinimizingGeneValueComparator */
    public MinimizingGeneValueComparator() {
    }

    /**
     *
     * Compares two individuals based on their genes values.
     *
     * @param o1 First individual to compare.
     * @param o2 Second individual to compare.
     * @return If the gene value of the individual o1 is lower than the gene value of the
     * individual o2 returns -1. If the gene value of the individual o2 is lower than the gen value of
     * the individual o1 returns 1. If the gene value of the individual o1 is equal to the gene value
     * of the individual o2 returns 0.
     */
    @Override
    public int compare(T o1, T o2) {
        if (o1.getChromosomeAt(0)[geneIndex] < o2.getChromosomeAt(0)[geneIndex]) {
            return -1;
        } else {
            if (o1.getChromosomeAt(0)[geneIndex] > o2.getChromosomeAt(0)[geneIndex]) {
                return 1;
            } else {
                return 0;
            }
        }

    }

    public void setGeneIndex(int index) {
        this.geneIndex = index;
    }

    @Override
    public String toString() {
        return "Gene Value Comparator";
    }
}
