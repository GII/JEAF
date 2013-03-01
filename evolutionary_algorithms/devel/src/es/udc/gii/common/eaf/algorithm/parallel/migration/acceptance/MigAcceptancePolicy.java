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
 * MigAcceptancePolicy.java
 *
 * Created on March 20, 2008, 9:58 AM
 *
 */

package es.udc.gii.common.eaf.algorithm.parallel.migration.acceptance;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.topology.migration.MigrationObject;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.config.Configurable;
import java.util.List;

/**
 * An acceptance policy states which individuals are accepted when they arrive
 * from another process.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public interface MigAcceptancePolicy extends Configurable {
    
    /**
     * Accepts the individuals that come in a {@link MigrationObject} based on
     * some criterion.<p>
     *
     * @return A list of the accepted individuals. If no individual is accepted,
     * then an empty list is returned.
     */
    List<Individual> accept(MigrationObject migrant,
        EvolutionaryAlgorithm algorithm, List<Individual> currentPopulation);        
}
