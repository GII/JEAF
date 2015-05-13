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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the subtour exchange cross-over operator by
 * "M. Kobayashi, T. Ono, and S. Kobayashi (1992). Character-preserving genetic 
 * algorithms for traveling salesman problem (in japanese). Journal of Japanese
 * Society for Artificial Intelligence, 7:1049–1059."<p/>
 *
 * This operator exchanges two subsequences that share the same genes between two
 * parents. As an example:
 * 
 * <pre>
 * parent1 = 1 4 2 3 5 6
 *             -----
 * child1  = 1 3 2 4 5 6
 *             -----
 * child2  = 5 1 4 2 3 6
 *               -----
 * parent2 = 5 1 3 2 4 6
 *               -----
 * </pre>
 *
 * The subsequence [4 2 3] from parent1 has the same genes as the subsequence [3 2 4]
 * from parent2. Child1 gets all the genes from parent1 but with the subsequence
 * [4 2 3] changed to [3 2 4]. Child2 gets all genes from parent2 but with the
 * subsequence [3 2 4] changed to [4 2 3].<p/>
 *
 * Only the first chromosome of each parent is considered. The chromosome may
 * have repeated gene values.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class SXXCrossOver extends CrossOverOperator {

    @Override
    protected List<Individual> crossOver(EvolutionaryAlgorithm ea, List<Individual> individuals) {

        double[] parent1 = individuals.get(0).getChromosomeAt(0);
        double[] parent2 = individuals.get(1).getChromosomeAt(0);

        assert parent1.length == parent2.length : "Length missmatch!";

        double[] child1 = new double[parent1.length];
        double[] child2 = new double[parent1.length];

        // Copy parents to children.
        System.arraycopy(parent1, 0, child1, 0, parent1.length);
        System.arraycopy(parent2, 0, child2, 0, parent2.length);

        // Apply operator.
        subtourExchangeCrossover(parent1, parent2, child1, child2);

        // Override parents. (It is assumed that some previous operator has
        // made a copy).
        individuals.get(0).setChromosomeAt(0, child1);
        individuals.get(1).setChromosomeAt(0, child2);

        return individuals;
    }

    /**
     * Creates all subsequences of a sequence of genes. Subsequences have at
     * least length 2 and at most length {@code parent.length - 1}.
     * @param parent The parent chromosome
     * @return A list of all subsequences of {@code parent}, or an empty list
     * if no subsequence with at least length 2 can be constructed.
     */
    private List<Subsequence> createSubsequences(double[] parent) {
        List<Subsequence> subseq = new ArrayList<Subsequence>();

        for (int sl = 2; sl < parent.length; sl++) {
            for (int i = 0; i <= parent.length - sl; i++) {
                subseq.add(new Subsequence(i, sl, parent));
            }
        }

        return subseq;
    }

    /**
     * Subtour exchange crossover operator.
     * 
     * @param parent1 The first parent chromosome (in).
     * @param parent2 The second parent chromosome (in).
     * @param child1 The first child resulting from crossover (in-out). It must
     * contain a copy of parent1.
     * @param child2 The second child resulting from crossover (in-out). It must
     * contain a copy of parent2.
     */
    private void subtourExchangeCrossover(double[] parent1, double[] parent2,
            double[] child1, double[] child2) {

        // Find all subsequences.
        List<Subsequence> ss1 = createSubsequences(parent1);
        List<Subsequence> ss2 = createSubsequences(parent2);

        // Lists for storing subsequences that share the same genes. For all
        // i = 0..n, pairedSS1.get(i) has the same genes as pairedSS2.get(i),
        // where n is the number of subsequences with same genes.
        List<Subsequence> pairedSS1 = new ArrayList<Subsequence>();
        List<Subsequence> pairedSS2 = new ArrayList<Subsequence>();

        // Find out which subsequences share the same genes.
        for (Subsequence s1 : ss1) {
            for (Subsequence s2 : ss2) {
                if (s1.sharesAllGenesWith(s2)) {
                    pairedSS1.add(s1);
                    pairedSS2.add(s2);
                }
            }
        }

        assert pairedSS1.size() == pairedSS2.size() : "Size missmatch!";

        // If there are some subsequences that share all genes.
        if (!pairedSS1.isEmpty()) {

            // Choose one subsequence-pair.
            int r = EAFRandom.nextInt(pairedSS1.size());

            Subsequence s1 = pairedSS1.get(r);
            Subsequence s2 = pairedSS2.get(r);

            assert s1.length == s2.length : "Sequence length missmatch.";

            // Exchange both subsequences.
            System.arraycopy(s2.data, s2.init, child1, s1.init, s1.length);
            System.arraycopy(s1.data, s1.init, child2, s2.init, s2.length);
        }
    }

    /**
     * Represents a subsequence of a chromosome.
     */
    private class Subsequence {

        /**
         * The index where the subsequence begins, inclusive.
         */
        public int init;
        /**
         * The total length of the subsequence.
         */
        public int length;
        /**
         * Reference to the whole chromosome.
         */
        public double[] data;

        public Subsequence(int init, int length, double[] data) {
            this.init = init;
            this.length = length;
            this.data = data;
        }

        /**
         * Tests if a subsequence shares all the genes with another one.
         * @param subSeq The other subsequence.
         * @return {@code true} if both subsequences share the same genes (in any
         * order), {@code false} otherwise.
         */
        public boolean sharesAllGenesWith(Subsequence subSeq) {

            assert subSeq != null : "subSeq is null!";

            if (this.length != subSeq.length) {
                return false;
            }

            boolean contained = false;

            for (int i = this.init; i < this.init + this.length; i++) {
                contained = false;

                for (int j = subSeq.init; j < subSeq.init + subSeq.length; j++) {
                    contained = contained || this.data[i] == subSeq.data[j];

                    if (contained) {
                        break;
                    }
                }

                if (!contained) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public String toString() {
            String ret = "";

            for (int i = this.init; i < this.init + this.length; i++) {
                ret += " " + this.data[i];
            }

            return "[" + ret + " ]";
        }
    }

    // testing
    public static void main(String[] args) {
//        double[] p1 = {1, 3, 5, 2};
//        double[] p2 = {2, 5, 3, 1};
//        double[] c1 = {1, 3, 5, 2};
//        double[] c2 = {2, 5, 3, 1};
        double[] p1 = {1.0};
        double[] p2 = {3.0};
        double[] c1 = {1.0};
        double[] c2 = {3.0};

        EAFRandom.init();
        SXXCrossOver sxx = new SXXCrossOver();
        sxx.subtourExchangeCrossover(p1, p2, c1, c2);

        System.out.println(Arrays.toString(p1));
        System.out.println(Arrays.toString(p2));
        System.out.println(Arrays.toString(c1));
        System.out.println(Arrays.toString(c2));
    }
}
