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
 * UniformSelection.java
 *
 * Created on 26 de febrero de 2007, 10:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.selection;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a uniform selection opetator. Every individual of the population has the same
 * probability to be selected. Each time that the operator is executed, a random index is generated
 * from an uniform distribution.<p>
 *
 * Configuration: <p>
 *
 * <pre>
 * {@code
 * <Operator><p>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.UniformSelection</Class>
 * </Operator>
 * }
 * </pre>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class UniformSelection extends SelectionOperator {

    /** Creates a new instance of UniformSelection */
    public UniformSelection() {
    }

    @Override
    public void configure(Configuration conf) {
    }

    @Override
    protected Individual select(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) {

        int randomPos;

        randomPos = (int) Math.floor(
                EAFRandom.nextDouble() * individuals.size());

        return individuals.get(randomPos);

    }
}
