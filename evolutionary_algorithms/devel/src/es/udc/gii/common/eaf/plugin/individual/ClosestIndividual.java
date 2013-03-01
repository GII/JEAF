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
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFMath;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * A plugin for choosing the closet individual to an other one from a list of individuals.<p/>
 *
 * Closest means here the nearest individual in parameter space considering euclidean distance. Only
 * the first chromosome of the individual is used.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ClosestIndividual extends IndividualChooser {

    /**
     * Returns the closest individual chosen from a list of individuals.
     *
     * @param algorithm Current evolutionary algorithm. (Not used and hence it can be set to {@code null}).
     * @param individuals List of individuals from which to choose one.
     * @param reference Individual that serves as a reference.
     * @return The closest individual to {@code reference} from the passed list. Euclidean disntance
     * is used and only the first chromosome is considered.
     */
    @Override
    public Individual get(EvolutionaryAlgorithm algorithm, List<Individual> individuals, Individual reference) {
        Individual closest = individuals.get(0);
        double distance, newDistance;

        distance = EAFMath.distance(closest.getChromosomeAt(0), reference.getChromosomeAt(0));
        for (Individual individual : individuals) {
            if (!individual.equals(reference)) {
                newDistance = EAFMath.distance(individual.getChromosomeAt(0), reference.getChromosomeAt(0));
                if (newDistance < distance) {
                    closest = individual;
                    distance = newDistance;
                }
            }
        }
        return closest;
    }

    /**
     * Configures this plugin. No configuration is needed.
     *
     * @param conf
     */
    @Override
    public void configure(Configuration conf) {
    }
}
