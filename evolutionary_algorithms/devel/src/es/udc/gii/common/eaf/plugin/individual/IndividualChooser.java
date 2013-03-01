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
import es.udc.gii.common.eaf.plugin.Plugin;
import java.util.List;

/**
 * A plugin for choosing individuals from a list of individuals.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class IndividualChooser implements Plugin {

    /**
     * Returns an individual chosen from a list of individuals. The current
     * algorithm and another reference individual might be considered for
     * deciding wich individual to choose.
     *
     * @param algorithm Current evolutionary algorithm.
     * @param individuals List of individuals from which to choose one.
     * @param reference Individual that serves as a reference.
     * @return An individual from a the passed list.
     */
    public abstract Individual get(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals, Individual reference);
}
