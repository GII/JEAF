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
 * MigSelectionStrategy.java
 *
 * Created on March 12, 2008, 7:52 PM
 */
package es.udc.gii.common.eaf.algorithm.parallel.migration.selection;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.migration.MigrationOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.config.Configurable;
import java.util.List;

/**
 * This strategy is used in a {@link MigrationOperator} to know which individuals
 * from the current island's population will migrate to other islands.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public interface MigSelectionStrategy extends Configurable {

    /** Returns th individuals that can migrate to other processes. If no individuals
     * are chosen an empty list is returned. */
    List<Individual> getIndividualsForMigration(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals);
}
