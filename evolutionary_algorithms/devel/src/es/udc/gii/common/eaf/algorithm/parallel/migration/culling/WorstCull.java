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
 * WorstCull.java
 *
 * Created on April 1, 2008, 2:30 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel.migration.culling;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.WorstIndividualSpecification;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * A culling strategy that returns the worst individuals of the current population.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class WorstCull implements MigCullingStrategy {

    /** Creates a new instance of WorstCull */
    public WorstCull() {
    }

    @Override
    public List<Individual> getIndividualsToCull(EvolutionaryAlgorithm algorithm,
            List<Individual> migrants, List<Individual> currentIndividuals) {

        if (migrants.isEmpty()) {
            /* If no individuals where accepted, don't cull. */
            return new ArrayList<Individual>();
        } else {
            WorstIndividualSpecification spec = new WorstIndividualSpecification();

            return IndividualsProductTrader.get(spec, currentIndividuals, migrants.size(),
                    algorithm.getComparator());
        }
    }

    @Override
    public void configure(Configuration conf) {
    }

}
