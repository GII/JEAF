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


package es.udc.gii.common.eaf.algorithm.operator;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.OperatorException;
import java.util.ArrayList;
import java.util.List;
import es.udc.gii.common.eaf.algorithm.population.Population;

/**
 * Implements an operators' chain. This chain contains a group of operators to
 * be executed on a particular problem to resolve it. This chain could be executed
 * operator by operator or all operators in one step.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class OperatorChain<T extends Operator> {

    /**
     * Indicates the next operator to be executed.
     */
    private int next = 0;
    /**
     * The operators of this chain.
     */
    private List<T> operators;

    /**
     * Creates a new operator chain with an empty list of operators.
     */
    public OperatorChain() {
        this.operators = new ArrayList<T>();
    }

    /**
     * Add an object of type Operator to the List operators.
     * @param operator an operator to be inserted in the chain.
     */
    public void addOperators(T operator) {
        this.operators.add(operator);
    }

    /**
     * Remove an object of type Operator from the List operators.
     * @param operator an operator to be deleted from the chain.
     */
    public void removeOperators(T operator) {
        this.operators.remove(operator);
    }

    /**
     * Get the list of operators
     *
     *
     * @return List of operators
     */
    public List<T> getOperators() {
        return this.operators;
    }

//Ejecuta todos los operadores:
    /**
     * This method execute all chain's operators in one step over the current 
     * algorithm. The population will change every time that an operator is executed.
     * @param algorithm the algorithm wich over we will execute the chain's operator.
     * @param population the initial population over we will execute the operators. And when we return
     * to the execution of the algorithm this population will change.
     */
    public List<Individual> execute(
            EvolutionaryAlgorithm algorithm,
            Population population) {

        List<Individual> resultIndividuals = new ArrayList<Individual>();

        resultIndividuals.addAll(population.getIndividuals());


        for (int i = 0; i < this.operators.size(); i++) {

            resultIndividuals =
                    this.executeNext(algorithm, resultIndividuals);

        //Se sustituyen los individuos de la poblacion por los resultantes
        //de aplicar el operator para que el siguiente operador se ejecute
        //sobre la nueva lista de individuos:
        //population.removeIndividuals(population.getIndividuals());

        //population.setIndividuals(resultIndividuals);
        }

        return resultIndividuals;
    }

    /**
     * Execute the next operator in the chain and change the value of <i>next</i>
     * attribute to execute the next operator the next time.
     * @param algorithm the algorithm wich over we will execute the current operator.
     * @param population the population which over we will execute the current operator.
     */
    public List<Individual> executeNext(
            EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) {

        List<Individual> resultIndividuals = null;

        try {

            resultIndividuals = this.operators.get(next).operate(
                    algorithm,
                    individuals);


        } catch (OperatorException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        //After operate the current operator this method update the next index
        this.next = (++next) % this.operators.size();

        return resultIndividuals;

    }

    /**
     * Return the String that represents an operators' chain.
     * @return an String representing an operators' chain.
     */
    @Override
    public String toString() {

        String s = "";
        int operatorsSize = this.operators.size();


        for (int i = 0; i < operatorsSize - 1; i++) {

            s += this.operators.get(i).toString() + " -> ";

        }

        s += this.operators.get(operatorsSize - 1);

        return s;

    }
}

