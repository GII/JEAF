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
package es.udc.gii.common.eaf.plugin.multiobjective;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.MultiobjectiveIndividual;
import es.udc.gii.common.eaf.exception.WrongIndividualException;
import es.udc.gii.common.eaf.plugin.Plugin;
import es.udc.gii.common.eaf.util.MOUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class has a method which calculates the rank of each individual in a 
 * population.<p>
 * 
 * Fast non-dominated sort is used. See "A Fast and Elitist Multiobjective
 * Genetic Algorithm: NSGA-II", Kalyanmoy Deb, Amrit Pratap, Sameer Agarwal 
 * and T. Meyarivan, IEEE TRANSACTIONS ON EVOLUTIONARY COMPUTATION, VOL. 6,
 * NO. 2, pp. 182-197, APRIL 2002.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 *
 */
public class NSGA2Ranking<T extends MultiobjectiveIndividual> implements Plugin {

    /**
     * Calculates the rank (front index) of each individual in a list.
     * 
     * @param list - List of individuals.
     * @return The number of fronts found.
     */
    public int calculate(List<T> list) {

        /* Checks */
        if (list == null || list.isEmpty()) {
            return 0;
        }

        if (!(list.get(0) instanceof MultiobjectiveIndividual)) {
            throw new WrongIndividualException(MultiobjectiveIndividual.class,
                    list.get(0).getClass());
        }

        /* The current front. */
        List<MultiobjectiveIndividual> front =
                new ArrayList<MultiobjectiveIndividual>(list.size());

        /* The current rank of the front. */
        int rank = 0;

        /* Reset individuals. */
        for (Individual i : list) {
            MultiobjectiveIndividual ni = (MultiobjectiveIndividual) i;
            ni.clearDominatedIndividuals();
            ni.setRank(Integer.MAX_VALUE);
            ni.setDominationCount(0);
        }

        /* NOTE:
         * The original algorithm is here enhaced somewhat. The inner loop
         * does not iterate through all individuals since by doing this we are
         * calculating 2 times the dominace between the same two individuals.
         */

        /* For each individual. */
        for (int p = 0; p < list.size(); p++) {
            /* Get the individual. */
            MultiobjectiveIndividual indP = (MultiobjectiveIndividual) list.get(p);

            /* For all other individuals that have not been considered 
             * until now. */
            for (int q = p + 1; q < list.size(); q++) {
                /* Get that individual. */
                MultiobjectiveIndividual indQ = (MultiobjectiveIndividual) list.get(q);

                /* Check their dominance. */
                int dominance = MOUtil.checkDominanceConstraints(indP, indQ);

                switch (dominance) {
                    case MOUtil.FIRST_DOMINATES:
                        /* Add the second individual to the domination list of
                         * the first individual. */
                        indP.addDominatedIndividual(indQ);

                        /* IMPROVEMENT: increase the domination count of the
                         * second individual. In the original algorithm this
                         * step would be done in another iteration of the outer
                         * loop.*/
                        indQ.increaseDominationCount();
                        break;
                    case MOUtil.SECOND_DOMINATES:
                        /* Increase the domination count for the current first
                         * individual. */
                        indP.increaseDominationCount();

                        /* IMPROVEMENT: add the dominated individual to the
                         * domination list of the second individual. In the 
                         * original algorithm this step would be done in another
                         * iteration of the outer loop.*/
                        indQ.addDominatedIndividual(indP);
                        break;
                }
            }

            /* If the first individual remains non-dominated. */
            if (indP.getDominationCount() == 0) {
                /* Set his rank to the current rank. */
                indP.setRank(rank);

                /* IMPROVEMENT: add the first individual to the front only if
                 * it dominates some other individual. So we do less iterations
                 * later. */
                if (!indP.getDominatedIndividuals().isEmpty()) {
                    front.add(indP);
                }
            }
        }

        /* The next front. */
        List<MultiobjectiveIndividual> newFront =
                new ArrayList<MultiobjectiveIndividual>(front.size());

        /* Increase rank counter. */
        rank++;

        /* While we have some individual in the current front. */
        while (!front.isEmpty()) {

            /* For each individual in the front. */
            for (int p = 0; p < front.size(); p++) {

                /* Get that individual. */
                MultiobjectiveIndividual indP = front.get(p);

                /* Get dominated individuals. */
                List<MultiobjectiveIndividual> dominated =
                        indP.getDominatedIndividuals();

                /* For each dominated individual. */
                for (int q = 0; q < dominated.size(); q++) {

                    /* Get it. */
                    MultiobjectiveIndividual indQ = (MultiobjectiveIndividual)dominated.get(q);

                    /* Decrease its domination count. */
                    indQ.decreaseDominationCount();

                    /* If its domination count is 0, then it is now non-dominated
                     * so we can include it in the next front. */
                    if (indQ.getDominationCount() == 0) {
                        /* Set the rank of the individual to the current rank. */
                        indQ.setRank(rank);

                        /* IMPROVEMENT: add the individual to the new front 
                         * only if it dominates some other individual. So we do
                         * less iterations later. */
                        if (!indQ.getDominatedIndividuals().isEmpty()) {
                            newFront.add(indQ);
                        }
                    }
                }
            }

            /* Reset the old front. */
            front.clear();

            /* If there is a new front. */
            if (!newFront.isEmpty()) {
                /* Add the new front. */
                front.addAll(newFront);
                /* Increase rank counter. */
                rank++;
                /* Reset the next front. */
                newFront.clear();
            }
        }

        /* Return the number of ranks (fronts). */
        return rank + 1;
    }

    /**
     * Configures this plugin. No configuration is needed.
     * @param conf
     */
    @Override
    public void configure(Configuration conf) {
        // nothing needed
    }
}
