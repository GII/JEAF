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

package es.udc.gii.common.eaf.algorithm.evaluate.constraint.methods;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.evaluate.constraint.ConstraintMethod;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a constraint handling method that do nothing with the individuals.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class NoConstraintsMethod implements ConstraintMethod {

    
    @Override
    public void evaluate(EvolutionaryAlgorithm algorithm, List<Constraint> constraint, Individual individual) {
        individual.setFitness(individual.getObjectives().get(0).doubleValue());
    }

    @Override
    public void configure(Configuration conf) {
    }

}
