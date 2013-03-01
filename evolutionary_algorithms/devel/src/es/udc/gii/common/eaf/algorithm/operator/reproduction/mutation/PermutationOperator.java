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
package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements a mutation operator which permutes all genes of the
 * chromosome of an individual. Only mono-chromosomatic individuals are currently
 * supported. If more than one chromosome is present, only the first one is
 * permuted.<p/>
 *
 * The generation of a permutation is O(n), where n is the number
 * of genes of the first chromosome.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class PermutationOperator extends MutationOperator {

    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm, Individual individual) {

        List<Individual> mutatedInd = new ArrayList<Individual>(1);

        /*
         * TODO: In future versions, this should be extended to support individuals
         * with multiple chromosomes.
         */
        double[] chromosome = individual.getChromosomeAt(0);

        // Generate permutation.
        int[] permutation = generatePermutation(chromosome.length);

        // Construct new chromosome.
        double[] newChromosome = new double[chromosome.length];
        for (int i = 0; i < newChromosome.length; i++) {
            newChromosome[i] = chromosome[permutation[i]];
        }

        individual.setChromosomeAt(0, newChromosome);

        mutatedInd.add(individual);

        return mutatedInd;
    }

    private int[] generatePermutation(int numElements) {
        int[] permutation = new int[numElements];

        /*
         * Initialize number sequence.
         */
        for (int i = 0; i < permutation.length; i++) {
            permutation[i] = i;
        }

        // Random index
        int r = 0;

        // Aux for swapping numbers
        int aux = 0;

        /*
         * Generate permutation.
         */
        for (int i = permutation.length - 1; i > 0; i--) {
            r = EAFRandom.nextInt(i + 1);
            aux = permutation[r];
            permutation[r] = permutation[i];
            permutation[i] = aux;
        }

        // Assertion for error checking if needed.
        assert validPermutation(permutation, numElements) :
                "Incorrect permutation generated: " + Arrays.toString(permutation);

        return permutation;
    }
    
    /**
     * Checks if a permutation is valid. Only for debugging pourposes.
     */
    private boolean validPermutation(int[] permutation, int numElements) {

        if (permutation == null) {
            return false;
        }

        if (permutation.length != numElements) {
            return false;
        }

        for (int i = 0; i < numElements; i++) {
            for (int j = i + 1; j < numElements; j++) {
                if (permutation[i] == permutation[j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // testing
    public static void main(String[] args) {
        EAFRandom.init();
        PermutationOperator po = new PermutationOperator();

        for (int n = 1; n <= 5; n++) {
            for (int i = 0; i < 3; i++) {
                System.out.println(Arrays.toString(po.generatePermutation(n)));
            }
        }
    }
}
