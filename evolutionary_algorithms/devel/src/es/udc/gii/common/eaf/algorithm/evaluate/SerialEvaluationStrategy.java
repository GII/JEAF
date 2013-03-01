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
package es.udc.gii.common.eaf.algorithm.evaluate;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import es.udc.gii.common.eaf.algorithm.evaluate.constraint.ConstraintMethod;
import es.udc.gii.common.eaf.algorithm.evaluate.constraint.methods.NoConstraintsMethod;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This clas is an evaluation strategy for a monoprocessor environment.<p>
 *
 * This class is the responsible of evaluate the list of individuals with the list of objective functions
 * and the list of constraints. <p>
 *
 * An instance of this class has an instance of the constraint handling method for the constrained problems
 * {@see ConstraintMethod}.<p>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class SerialEvaluationStrategy implements EvaluationStrategy, Serializable {

    //Estrategia de manejo de restricciones:
    private ConstraintMethod contraintMethod = new NoConstraintsMethod();

    /**
     * This method is used to evaluate a list of individuals with a list of
     * objective functions and a list of constraints.
     * @param algorithm The Evolutionary Algorithm used in the optimization.
     * @param individuals A list of individuals to be evaluated by the list of
     * objective functions.
     * @param functions An objective functions' list with wich we will evaluate
     * the individuals' list.
     * @param constraints A list of constraints functions wich will be evaluate
     * with the individuals values.
     */
    @Override
    public void evaluate(EvolutionaryAlgorithm algorithm, List<Individual> individuals,
            List<ObjectiveFunction> functions, List<Constraint> constraints) {

        Individual[] arrayIndividuals;
        Individual currentIndividual;

        arrayIndividuals = new Individual[individuals.size()];
        arrayIndividuals = individuals.toArray(arrayIndividuals);

        //Para cada individuo de la poblacion se evalua la funcion objetivo
        //y se establece su valor de calidad:
        for (int i = 0; i < arrayIndividuals.length; i++) {

            currentIndividual = arrayIndividuals[i];
            this.evaluate(algorithm, currentIndividual, functions, constraints);

        }


    }

    /**
     * This method is used to evaluate an individual with a list of
     * objective functions and a list of constraints.
     * @param algorithm The Evolutionary Algorithm used in the optimization.
     * @param individuals A list of individuals to be evaluated by the list of
     * objective functions.
     * @param functions An objective functions' list with wich we will evaluate
     * the individuals' list.
     * @param constraints A list of constraints functions wich will be evaluate
     * with the individuals values.
     */
    @Override
    public void evaluate(EvolutionaryAlgorithm algorithm, Individual individual, List<ObjectiveFunction> functions,
            List<Constraint> constraints) {

        double constraintValue;
        double[] values;
        List<Double> objectiveValues = new ArrayList<Double>();
        List<Double> constraintsValues = new ArrayList<Double>();

        values = individual.getChromosomeAt(0);

        if (functions != null) {
            for (int i = 0; i < functions.size(); i++) {
                objectiveValues.add(functions.get(i).evaluate(values));
            }
            individual.setObjectives(objectiveValues);
        }

        //Si hay restricciones se evaluan en el individuo:
        individual.setViolatedConstraints(0);
        if (constraints != null) {
            for (int i = 0; i < constraints.size(); i++) {
                constraintValue = constraints.get(i).evaluate(values);
                constraintsValues.add(constraintValue);
                if (constraints.get(i).isViolated(constraintValue)) {
                    individual.setViolatedConstraints(
                            individual.getViolatedConstraints() + 1);
                }

            }
            individual.setConstraints(constraintsValues);

        }

        if (this.contraintMethod != null) {
            this.contraintMethod.evaluate(algorithm, constraints, individual);
        }


    }

    @Override
    public void configure(Configuration conf) {

        try {
            if (conf.containsKey("ConstraintMethod.Class")) {

                this.contraintMethod = ((ConstraintMethod) Class.forName(
                        conf.getString("ConstraintMethod.Class")).newInstance());
                this.contraintMethod.configure(conf.subset("ConstraintMethod"));
            } else {
                ConfWarning w = new ConfWarning("ConstraintMethod.Class", this.contraintMethod.getClass().getName());
                w.warn();
            }
        } catch (Exception ex) {
            throw new ConfigurationException(this.getClass(), ex);
        }

    }
}
