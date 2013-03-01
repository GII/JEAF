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
 * Order1CrossOver.java
 *
 * Created on 27 de noviembre de 2006, 21:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.List;

/**
 * Order 1 Crossover is a fairly simple permutation crossover. Basically, 
 * a swath of consecutive alleles from parent 1 drops down, and remaining 
 * values are placed in the child in the order which they appear in parent 2.<p>
 * 
 * To config this operator the xml code is:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.Order1CrossOver</Class>
 *      <Probability>value</Probability>
 * </Operator>
 * }
 * </pre>
 *
 * Where probability is a double value in the range [0, 100] which represents the probability of crossover.
 * If this does not appear in the configuration, the parameter is set to the default values. <p>
 *
 * Default values: <p>
 * <ul>
 * <li>Probability is set to 60.0</li>
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class Order1CrossOver extends CrossOverOperator {

    /** Creates a new instance of Order1CrossOver */
    public Order1CrossOver() {
    }

    public Order1CrossOver(int probability) {
        super(probability);
    }

    @Override
    protected List<Individual> crossOver(EvolutionaryAlgorithm ea, List<Individual> individuals) {

        double[] parent1, parent2, child1, child2;
        int[] crossPoints;
        int chromSize, xp1, xp2, i, p1, p2, c;

        chromSize = individuals.get(0).getChromosomeAt(0).length;
        parent1 = individuals.get(0).getChromosomeAt(0);
        parent2 = individuals.get(1).getChromosomeAt(0);

        child1 = new double[chromSize];
        child2 = new double[chromSize];
        crossPoints = super.getCrossPoints(false, 2, chromSize);
        xp1 = crossPoints[0];
        xp2 = crossPoints[1];

        //Copio de los padres a los hijos:
        for (i = xp1; i <= xp2; i++) {

            child1[i] = parent1[i];
            child2[i] = parent2[i];

        }

        for (i = 0, p1 = p2 = xp2; i < (chromSize - (xp2 - xp1 + 1)); i++) {

            c = (xp2 + 1 + i) % chromSize;

            while (true) {
                p2 = (p2 + 1) % chromSize;
                if (!this.contains(child1, parent2[p2])) {
                    break;
                }
            }

            while (true) {
                p1 = (p1 + 1) % chromSize;
                if (!this.contains(child2, parent1[p1])) {
                    break;
                }
            }

            child1[c] = parent2[p2];
            child2[c] = parent1[p1];
        }


        individuals.get(0).setChromosomeAt(0, child1);
        individuals.get(1).setChromosomeAt(0, child2);

        return individuals;
    }

    private boolean contains(double[] chrom, double gene) {

        for (int i = 0; i < chrom.length; i++) {

            if (chrom[i] == gene) {
                return true;
            }
        }

        return false;
    }
}
