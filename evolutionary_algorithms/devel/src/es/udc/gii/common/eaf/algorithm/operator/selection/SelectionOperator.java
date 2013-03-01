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
package es.udc.gii.common.eaf.algorithm.operator.selection;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.exception.OperatorException;
import java.util.Arrays;
import java.util.List;
import es.udc.gii.common.eaf.algorithm.operator.Operator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.ArrayList;
import org.apache.commons.configuration.Configuration;

/**
 * This abstract class represents a selection operator.<p>
 *
 * The behavior of a selection operator is to select the individuals of the population that will be
 * used during the reproduction phase.<p>
 *
 * The classes that extend this operator will implement the method 
 * {@link #select(EvolutionaryAlgorithm algorithm, List<Individual> individuals)}. This method receives
 * a list of individuals and return a selected individual. This method is executed from the method
 * {@link operate(EvolutionaryAlgorithm algorithm, List<Individual> individuals)} until the children
 * population is filled.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
abstract public class SelectionOperator implements Operator {

    public SelectionOperator() {
    }

    /**
     * Selects individuals. <p/>
     *
     * Note for implementors: The list returned by the implementations of this
     * method must contain only copies of the selected individuals. Those copies might
     * be modified by successive operators. See also the note in
     * {@link SelectionOperator#select(es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm, java.util.List)}.
     */
    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) throws OperatorException {

        Individual[] selected = null;

        if (individuals == null) {

            throw new OperatorException("Selection - "
                    + "Empty individuals");

        }

        selected = new Individual[algorithm.getPopulation().getSize()];


        for (int i = 0; i < selected.length; i++) {

            //selected[i] = this.select(individuals);
            selected[i] = this.select(algorithm, algorithm.getPopulation().getIndividuals());

        }

        return new ArrayList<Individual>(Arrays.asList(selected));


    }

    /**
     * Selects individuals. <p/>
     *
     * Note for implementors: The individual returned by the implementations of this
     * method must be a copy of the selected individual. This copy might
     * be modified by successive operators. See also the note in
     * {@link SelectionOperator#operate(es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm, java.util.List)}.
     */
    protected abstract Individual select(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals);

    @Override
    public String toString() {
        return "Selection operator";
    }

    @Override
    public void configure(Configuration conf) {
    }
}
