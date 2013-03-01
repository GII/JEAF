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


package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.exception.OperatorException;
import java.util.List;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.ReproductionOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.ArrayList;
import org.apache.commons.configuration.Configuration;

/**
 * This abstract class represents a mutaiton operator.<p>
 *
 * The behavior of a mutation operator is to mutate the selected individuals of the population.<p>
 *
 * The classes that extend this operator will implement the method
 * {@link #mutation(EvolutionaryAlgorithm algorithm, List<Individual> individuals)}. This method receives
 * a list of individuals and return the mutated. This method is executed from the method
 * {@link operate(EvolutionaryAlgorithm algorithm, List<Individual> individuals)} for each of the individuals
 * of the list.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class MutationOperator extends ReproductionOperator {

    private double probability = 0.5;

    public MutationOperator() {
    }

    public MutationOperator(int probability) {

        this.probability = probability;
    }

    @Override
    public void configure(Configuration conf) {
        if (conf.containsKey("Probability")) {
            this.probability = conf.getDouble("Probability");
        } else {
            ConfWarning w = new ConfWarning("Probability", this.probability);
            w.warn();
        }
    }

    public double getProbability() {

        return this.probability;

    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

//    public Random getRandom() {
//        return this.random;
//    }
    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals)
            throws OperatorException {

        List<Individual> mutated_individuals;

        if (individuals == null) {

            throw new OperatorException("Mutation - Empty Individuals");

        }

        mutated_individuals = new ArrayList<Individual>();
        for (int i = 0; i < individuals.size(); i++) {

            mutated_individuals.addAll(this.mutation(algorithm, individuals.get(i)));

        }

        return mutated_individuals;
    }

    //24-06-08: Los operadores de mutacion devuelven una lista de individuos.
    protected abstract List<Individual> mutation(EvolutionaryAlgorithm algorithm,
            Individual individual);
}
