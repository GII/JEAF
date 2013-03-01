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
 * MGAAlgorithm.java
 *
 * Created on December 1, 2007, 3:11 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.mga;

import es.udc.gii.common.eaf.algorithm.operator.selection.DeterministicTournamentSelection;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.stoptest.BitwiseConvergence;
import es.udc.gii.common.eaf.stoptest.mga.MicroGenerationsConvergence;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a micro-genetic algorithm as defined by Krishnakumar
 * in "Micro-genetic algorithms for stationary and non-stationary function
 * optimization".<p/>
 *
 * The user has to provide a stoptest defining nominal convergence
 * and the number of individuals that are preserved among generations (elitism).
 * The user is free to provide some initial solutions that will be used as a
 * startpoint for the search. Note: The initial solutions are changed internally.
 * The user should not expect to get the same solutions he passed when invoking
 * the getter method once the search has begun.<p>
 *
 * Good results have been achieved using elitism of 1, {@link DeterministicTournamentSelection} and
 * elitism replace (for non-stationary problems use an elitism with reevaluation
 * of the best individuals). For convergence: {@link BitwiseConvergence} (convergence rate
 * between 0.6 and 0.9) or {@link MicroGenerationsConvergence} (~2 generations).
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 * @see AbstractMGAAlgorithm
 */
public class MGAAlgorithm extends AbstractMGAAlgorithm {

    /** Creates a new instance of MGAAlgorithm */
    public MGAAlgorithm() {
    }

    /**
     * Checks that the population size is constant.
     */
    private boolean assertPopulationSizeConstant(int required) {
        return required == getPopulation().getSize();
    }

    /**
     * Performs the operations needed for the next micro-evolution.
     */
    @Override
    protected void beforeMicroEvolution() {

        /* If the user has provided some initial solutions or if we have
         * some solution from a previous micro-evolution */
        if (getInitialSolutions() != null) {

            /* The population is replaced here with new ramdom solutions and the
             * initial solutions. */
            this.state = REPLACE_STATE;

            /* Geneate a new population and evaluate it. */

            int newIndSize =
                    getPopulation().getSize() - getInitialSolutions().size();

            Population newPopulation =
                    new Population(new ArrayList<Individual>());
            Individual newI = (Individual) getInitialSolutions().get(0).clone();
            newI.generate();
            newPopulation.addIndividual(newI);
            newPopulation.modifyPopulationSize(newIndSize);

            setPopulation(newPopulation);
            
            /* Evaluate the new population. */
            evaluate(getProblem(), getPopulation());

            /* Add the initial solutions to the population. */
            getPopulation().getIndividuals().addAll(getInitialSolutions());

            assert assertPopulationSizeConstant(
                    getInitialSolutions().size() + newIndSize) :
                    "Population size not constant";

            /* Notify observers the population change. */
            this.setChanged();
            this.notifyObservers();

        } else { /* If there's no initial solution it could be for two reasons:
             * 1) either the user has not provided any initial solution
             * 2) or the previous micro-evolution has not found any solution
             *
             * Note that case 2) makes only sense if we are dealing with
             * problems with constraints where absolutely no solution which
             * does not satisfy the constraints is allowed to evolve into the
             * the next micro-evolution cycle (and asuming a correct implementation
             * of the present code).
             *
             * In both cases we need to generate a complete new population.
             */

            /* Generate and evaluate the initial population. */
            state = INIT_EVALUATE_STATE;
            getPopulation().generate();
            evaluate(getProblem(), getPopulation());
        }

    }

    /**
     * Performs the operations needed after a micro-evolution.
     */
    @Override
    protected void afterMicroEvolution() {

        /* After a micro-evolution the population has reached nominal convergence
         * (as defined by the user).
         * So we choose a number of individuals as a startpoint for the next
         * micro-evolution. The rest will be randomly generated before the next
         * micro-evolution begins.
         */
        List<Individual> best = IndividualsProductTrader.get(
                new BestIndividualSpecification(),
                getPopulation().getIndividuals(), getElitism(), getComparator());

        setInitialSolutions(best);
    }

    @Override
    public String getAlgorithmID() {
        return "MGA";
    }

    @Override
    public String toString() {
        return "Micro-genetic Algorithm (single objective)";
    }
}
