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
 * MigCullingStrategy.java
 *
 * Created on March 12, 2008, 7:54 PM 
 */

package es.udc.gii.common.eaf.algorithm.parallel.migration.culling;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.migration.MigrationOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.config.Configurable;
import java.util.List;

/**
 * A culling strategy is used in a {@link MigrationOperator} to know which
 * individuals from the current island's population are to be removed considering
 * those individuals who come from other islands.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0el Tedin Alvarez
 */
public interface MigCullingStrategy extends Configurable {
    /** Returns the individuals to remove from the current population. If no
     * individuals shall be removed an empty list is returned. */
    List<Individual> getIndividualsToCull(EvolutionaryAlgorithm algorithm, List<Individual> migrants, 
        List<Individual> currentIndividuals);        
}
