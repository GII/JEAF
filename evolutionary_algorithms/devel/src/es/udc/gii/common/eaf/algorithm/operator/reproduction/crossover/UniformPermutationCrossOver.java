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
 * This class implements an uniform permutation crossover. See
 * "A. Ngom, 1997, Genetic algorithms for the jump number scheduling problem,
 * Order - A Journal on the Theory of Ordered Sets and its Applications,
 * pp.59-73, 1998." for details.<p/>
 *
 * This implementation assumes that the individuals have only one chromosome. If
 * an individual has more than one chromosome, only the first one is considered
 * and the other ignored.<p/>
 *
 * Further it is assumed that all chromosomes of the populations are permutations
 * of the same set of numbers and that a chromosome has no repeated values.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class UniformPermutationCrossOver extends CrossOverOperator {

    @Override
    protected List<Individual> crossOver(EvolutionaryAlgorithm ea,
            List<Individual> individuals) {

        double[] parent1 = individuals.get(0).getChromosomeAt(0);
        double[] parent2 = individuals.get(1).getChromosomeAt(0);

        double[] child1 = new double[parent1.length];
        double[] child2 = new double[parent1.length];

        uniformPermutationCrossOver(parent1, parent2, child1, child2);

        individuals.get(0).setChromosomeAt(0, child1);
        individuals.get(1).setChromosomeAt(0, child2);

        return individuals;
    }

    private void uniformPermutationCrossOver(double[] parent1, double[] parent2,
            double[] child1, double[] child2) {

        // Key for shuffling to one parent or the other.
        int key = EAFRandom.nextInt(2);

        int length = parent1.length;

        // Stores missing genes in child1.
        double[] missing1 = new double[length];

        // Stores the index of each of the
        // above missing genes in the chromosome of child1.
        int[] missingIdx1 = new int[length];

        // Stores missing genes in child2.
        double[] missing2 = new double[length];

        // Stores the index of each of the
        // above missing genes in the chromosome of child2.
        int[] missingIdx2 = new int[length];

        // Fill missing arrays with invalid values.
        Arrays.fill(missing1, Double.NaN);
        Arrays.fill(missing2, Double.NaN);

        int totalMissing1 = 0; // Total missing genes in child1.
        int totalMissing2 = 0; // Total missing genes in child2.

        // Shuffle genes from parents to children.
        for (int i = 0; i < length; i++) {
            if (key == EAFRandom.nextInt(2)) { // Gene goes from parent1 to child1
                child1[i] = parent1[i];

                // Child 2 misses the i-th gene from parent2.
                missing2[totalMissing2] = parent2[i];
                // Store the index of the gene which is missed.
                missingIdx2[totalMissing2++] = i;

            } else { // Gene goes from parent2 to child2

                child2[i] = parent2[i];

                // Child 1 misses the i-th gene from parent1.
                missing1[totalMissing1] = parent1[i];

                // Store the index of the gene which is missed.
                missingIdx1[totalMissing1++] = i;
            }
        }

        // Sort missing genes so as to make the use of binary search possible.
        Arrays.sort(missing1);
        Arrays.sort(missing2);

        // The missing genes of child1 have to follow the order of those same genes
        // in parent2. To ensure this order, we iterate along the genes in
        // parent2, test if those are missing in child1 and insert them at the
        // place where the next gene is missing in child1. We are finished either
        // when we travelled across all the genes of parent2 or when we filled
        // up all the missing genes of child1.
        for (int i = 0, m1 = 0; i < length && m1 < totalMissing1; i++) {
            if (Arrays.binarySearch(missing1, parent2[i]) >= 0) {
                // If gene parent2[i] is missing in child1
                child1[missingIdx1[m1++]] = parent2[i];
            }
        }

        // Same as above but exchanging child1 by child2 and parent2 by
        // parent1.
        for (int i = 0, m2 = 0; i < length && m2 < totalMissing2; i++) {
            if (Arrays.binarySearch(missing2, parent1[i]) >= 0) {
                // If gene parent1[i] is missing in child2
                child2[missingIdx2[m2++]] = parent1[i];
            }
        }
    }

    // testing
    public static void main(String[] args) {

        EAFRandom.init();

        UniformPermutationCrossOver upo = new UniformPermutationCrossOver();
        double[] p1 = {4.0, 1.0, 5.0, 3.0, 2.0};
        double[] p2 = {2.0, 1.0, 5.0, 3.0, 4.0};
        double[] c1 = new double[5];
        double[] c2 = new double[5];

        for (int i = 0; i < 60; i++) {
            upo.uniformPermutationCrossOver(p1, p2, c1, c2);
            System.out.println(Arrays.toString(c1) + " - " + Arrays.toString(c2));
        }
    }
}
