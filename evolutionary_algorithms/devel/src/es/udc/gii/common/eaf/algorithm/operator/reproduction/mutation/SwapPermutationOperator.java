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
import java.util.List;

/**
 * This class implements a mutation operator where each gene {@code i} is swapped
 * with an other gene {@code j} from a randomly generated position. Only the
 * first chromosome of an individual is considered.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class SwapPermutationOperator extends MutationOperator {

    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm, Individual individual) {

        List<Individual> mutatedInd = new ArrayList<Individual>(1);

        /*
         * TODO: In future versions, this should be extended to support individuals
         * with multiple chromosomes.
         */
        double[] chromosome = individual.getChromosomeAt(0);

        double aux = 0;
        int r = 0;

        for (int i = 0; i < chromosome.length; i++) {
            r = EAFRandom.nextInt(chromosome.length);
            aux = chromosome[i];
            chromosome[i] = chromosome[r];
            chromosome[r] = aux;
        }

        individual.setChromosomeAt(0, chromosome);
        mutatedInd.add(individual);

        return mutatedInd;

    }
}
