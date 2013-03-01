/*
 *  Copyright (C) 2011 Grupo Integrado de Ingeniería
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the cycle crossover. See "Oliver, L M . , D.J. Smith,
 * and J .R.C. Holland ( 1987) . A Study of Permutation Crossover Operators on
 * the Traveling Salesman Problem. In Proceedings of the 2nd International
 * Conference on Genetic Algorithms. Lawrence Erlbaum Associates, Hillsdale, NJ
 * pp. 224-230" or "Z. Michalewicz and D. Fogel, How to Solve It: Modern
 * Heuristics. Springer-Verlag, 2000." for a description of how this operator
 * works.<p/>
 *
 * Only the first chromosome of each parent is considered. The chromosome must
 * not have repeated gene values.<p/>
 *
 * The implementation realies on Double.NaN for marking special positions in
 * the chromosomes. Thus, Double.NaN must not be used as a valid gene value.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class CXCrossOver extends CrossOverOperator {

    @Override
    protected List<Individual> crossOver(EvolutionaryAlgorithm ea, List<Individual> individuals) {
        double[] parent1 = individuals.get(0).getChromosomeAt(0);
        double[] parent2 = individuals.get(1).getChromosomeAt(0);

        assert parent1.length == parent2.length : "Length missmatch!";

        double[] child1 = new double[parent1.length];
        double[] child2 = new double[parent1.length];

        // Apply operator.
        cxCrossover(parent1, parent2, child1, child2);

        // Override parents. (It is assumed that some previous operator has
        // made a copy).
        individuals.get(0).setChromosomeAt(0, child1);
        individuals.get(1).setChromosomeAt(0, child2);

        return individuals;
    }

    /**
     * Performs the cycle crossover.
     * 
     * @param parent1 The first parent chromosome (in).
     * @param parent2 The second parent chromosome (in).
     * @param child1 The first child resulting from crossover (in-out).
     * @param child2 The second child resulting from crossover (in-out).
     */
    private void cxCrossover(double[] parent1, double[] parent2,
            double[] child1, double[] child2) {

        // Generate each of the children
        generateChild(parent1, parent2, child1);
        generateChild(parent2, parent1, child2);
    }

    /**
     * Generates a child given its two parents.
     *
     * @param parent1 The first parent. (in)
     * @param parent2 The second parent. (in)
     * @param child The resulting child. (in-out)
     */
    private void generateChild(double[] parent1, double[] parent2, double[] child) {

        // Position at which a gene of parent2 is situated in parent1.
        int pos = 0;

        // Guard for knowing when a cycle is reached.
        boolean cycle = false;

        // Mark all positions as empty
        Arrays.fill(child, Double.NaN);

        // The first element of the child is the first of the parent1.
        child[0] = parent1[0];

        // While we do not encounter a cycle.
        while (!cycle) {
            if (!contains(child, parent2[pos])) {
                pos = findPosition(parent2[pos], parent1);
                child[pos] = parent1[pos];
            } else { // Cycle found.
                cycle = true;
            }
        }

        // Copy the genes of parent2 to child where child has empty genes.
        for (int i = 0; i < child.length; i++) {
            if (Double.isNaN(child[i])) {
                child[i] = parent2[i];
            }
        }
    }

    /**
     * Returns true if item is in data, false otherwise.
     */
    private boolean contains(double[] data, double item) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == item) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the position of {@code d} in {@code parent1}. If the item is not
     * found -1 is returned.
     */
    private int findPosition(double d, double[] parent1) {

        for (int i = 0; i < parent1.length; i++) {
            if (parent1[i] == d) {
                return i;
            }
        }

        assert false : "Corrupted chromosome: did not found d = " + d
                + " in " + Arrays.toString(parent1);

        return -1;
    }

    // testing
    public static void main(String[] args) {
        double[] p1 = {1, 3, 5, 2};
        double[] p2 = {2, 5, 3, 1};
        double[] c1 = {1, 3, 5, 2};
        double[] c2 = {2, 5, 3, 1};

        EAFRandom.init();
        CXCrossOver cx = new CXCrossOver();
        cx.cxCrossover(p1, p2, c1, c2);

        System.out.println(Arrays.toString(p1));
        System.out.println(Arrays.toString(p2));
        System.out.println(Arrays.toString(c1));
        System.out.println(Arrays.toString(c2));
    }
}
