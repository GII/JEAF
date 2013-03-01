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
package es.udc.gii.common.eaf.util;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for genetic algorithms for multiple objectives.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MOUtil {

    public static final int FIND_ALL = -1;
    /** The first individual dominates the second one. */
    public static final int FIRST_DOMINATES = -1;
    /** The second individual dominates the first one. */
    public static final int SECOND_DOMINATES = 1;
    /** The individuals are not-dominated by each other. */
    public static final int NOT_DOMINATED = 0;

    /** Creates a new instance of MOUtil */
    public MOUtil() {
    }

    /**
     * Checks the dominance between two individuals.<p/>
     *
     * IMPORTANT: All objectives are considered to be minimized.
     * 
     * @param first First individual.
     * @param second Second individual.
     * @return {@link MOUtil#FIRST_DOMINATES} if the first individual dominates the
     * second one. {@link MOUtil#SECOND_DOMINATES} if the second individual dominates
     * the first one. {@link MOUtil#NOT_DOMINATED} if the individuals are not-dominated.
     */
    public static int checkDominance(Individual first, Individual second) {
        boolean firstDominatesAnObjective = false;
        boolean secondDominatesAnObjective = false;

        List<Double> firstObjectives = first.getObjectives();
        List<Double> secondObjectives = second.getObjectives();

        int i = 0;
        for (Double objFirst : firstObjectives) {
            Double objSecond = secondObjectives.get(i++);

            if (objFirst.doubleValue() < objSecond.doubleValue()) {
                firstDominatesAnObjective = true;
            } else if (objSecond.doubleValue() < objFirst.doubleValue()) {
                secondDominatesAnObjective = true;
            }

            /* Improvement: if both dominate an objective, the individuals
             * are not-dominated.
             */
            if (firstDominatesAnObjective && secondDominatesAnObjective) {
                return NOT_DOMINATED;
            }
        }

        if (!firstDominatesAnObjective && !secondDominatesAnObjective) {
            return NOT_DOMINATED;
        } else if (firstDominatesAnObjective) {
            return FIRST_DOMINATES;
        } else { /* secondDominatesAnObjective == true */
            return SECOND_DOMINATES;
        }
    }

    /**
     * Checks the dominance between two individuals. Dominance is defined as follows:
     * If both individuals violate constraints, the one who violates less constraints
     * is dominant. Otherwise dominance as defined by Pareto is computed.<p/>
     *
     * IMPORTANT: All objectives are considered to be minimized.
     *
     * @param first First individual.
     * @param second Second individual.
     * @return {@link MOUtil#FIRST_DOMINATES} if the first individual dominates the
     * second one. {@link MOUtil#SECOND_DOMINATES} if the second individual dominates
     * the first one. {@link MOUtil#NOT_DOMINATED} if the individuals are not-dominated.
     */
    public static int checkDominanceConstraints(Individual first, Individual second) {
        boolean firstViolatesConstraints = first.getViolatedConstraints() > 0;
        boolean secondViolatesConstraints = second.getViolatedConstraints() > 0;

        if (firstViolatesConstraints || secondViolatesConstraints) {
            int constraintsViolatedByFirst = first.getViolatedConstraints();
            int constraintsViolatedBySecond = second.getViolatedConstraints();

            if (constraintsViolatedByFirst > constraintsViolatedBySecond) {
                return SECOND_DOMINATES;
            } else if (constraintsViolatedBySecond > constraintsViolatedByFirst) {
                return FIRST_DOMINATES;
            } else {
                return NOT_DOMINATED;
            }
        } else { /* No violated constraints */
            return checkDominance(first, second);
        }
    }

    /**
     * Finds a number of non-dominated individuals. If number is 
     * {@link MOUtil#FIND_ALL}, this method returns all non-dominated individuals.
     * @param individuals The list of individuals.
     * @param number Number of not-dominated individuals to find. This is the
     * maximum number of individuals returned, since it might be that there are
     * not as many not-dominated individuals in the passed list.
     * @return An empty list if no not-dominated individuals haven been found or
     * a list of at most {@code number} individuals.
     */
    public static List<Individual> findNonDominatedIndividuals(
            List<Individual> individuals, int number) {

        if (number == FIND_ALL) {
            number = individuals.size();
        }

        int i = 0;

        List<Individual> nonDominatedIndividuals =
                new ArrayList<Individual>(individuals.size() / 2);

        while ((nonDominatedIndividuals.size() < number)
                && (i < individuals.size())) {

            Individual indTest = individuals.get(i);

            boolean dominated = false;

            for (int j = 0; j < individuals.size(); j++) {
                Individual peer = individuals.get(j);

                if (checkDominance(indTest, peer) == SECOND_DOMINATES) {
                    dominated = true;
                    break;
                }
            }

            if (!dominated) {
                nonDominatedIndividuals.add(indTest);
            }

            i++;
        }

        return nonDominatedIndividuals;

    }
}
