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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.plugin.individual;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * A plugin for choosing the best individual from a list of individuals.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class WorstIndividual extends IndividualChooser {

    /**
     * Returns the best individual chosen from a list of individuals.
     *
     * @param algorithm Current evolutionary algorithm.
     * @param individuals List of individuals from which to choose one.
     * @param reference Individual that serves as a reference. (Not used and hence
     * it can be set to {@code null}).
     * @return The best individual from the passed list.
     */
    @Override
    public Individual get(EvolutionaryAlgorithm algorithm, List<Individual> individuals, Individual reference) {
        FitnessComparator<Individual> comparator = algorithm.getComparator();
        Individual worst = individuals.get(0);

        for (Individual individual : individuals) {
            if (comparator.compare(individual, worst) > 0) {
                worst = individual;
            }
        }
        return worst;
    }

    /**
     * Configures this plugin. No configuration is needed.
     * @param conf
     */
    @Override
    public void configure(Configuration conf) {
    }
}
