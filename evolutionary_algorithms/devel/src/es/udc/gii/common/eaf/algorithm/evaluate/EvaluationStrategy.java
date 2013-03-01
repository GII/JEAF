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
 * EvaluationStrategy.java
 *
 * Created on 8 de noviembre de 2006, 11:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.evaluate;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.config.Configurable;
import java.util.List;
import es.udc.gii.common.eaf.algorithm.population.Individual;

/**
 * An <tt>EvaluationStrategy</tt> represents a way to evaluate a list of 
 * individuals with a list of objective functions and a list of constraints.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public interface EvaluationStrategy extends Configurable {

    
    
    /**
     * This method is used to evaluate a list of individuals with a list of 
     * objective functions.
     * @param algorithm The Evolutionary Algorithm used in the optimization.
     * @param individuals A list of individuals to be evaluated by the list of 
     * objective functions.
     * @param functions An objective functions' list with wich we will evaluate 
     * the individuals' list.
     * @param constraints A list of constraints with wich we will evaluate the 
     * individuals list.
     */
    public abstract void evaluate(
            EvolutionaryAlgorithm algorithm,
            List<Individual> individuals, List<ObjectiveFunction> functions,
            List<Constraint> constraints);

    /**
     * This method is used to evaluate an individual with a list of 
     * objective functions.
     * @param algorithm The Evolutionary Algorithm used in the optimization.
     * @param individual An individual to be evaluated by the list of 
     * objective functions.
     * @param functions An objective functions' list with wich we will evaluate 
     * the individual.
     * @param constraints A list of constraints with wich we will evaluate the 
     * individual.
     */
    public abstract void evaluate(
            EvolutionaryAlgorithm algorithm,
            Individual individual, List<ObjectiveFunction> functions,
            List<Constraint> constraints);

    
    
}
