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
package es.udc.gii.common.eaf.log.nsga2;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.NSGA2Individual;
import es.udc.gii.common.eaf.exception.WrongIndividualException;
import es.udc.gii.common.eaf.log.LogTool;
import java.util.ArrayList;
import java.util.List;

/**
 * The base class for all NSGA2 log tools.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class NSGA2LogTool extends LogTool {

    /**
     * Returns the {@code front + 1}-th front. The 0-th is the Pareto front.
     *
     * @param population  List of individuals.
     * @param front The front number. The 0-th front is the Pareto front.
     * @return A list of individuals.
     * @throws es.udc.gii.common.eaf.exception.WrongIndividualException  If the
     * passed list of individuals contains instances that are not instances of
     * {@link NSGA2Individual}.
     */
    public List<NSGA2Individual> getFront(List<Individual> population, int front) {
        if (population == null || population.isEmpty()) {
            return new ArrayList<NSGA2Individual>();
        }

        // population.size() is just a hint
        List<NSGA2Individual> list =
                new ArrayList<NSGA2Individual>(population.size());

        for (Individual ind : population) {
            if (!(ind instanceof NSGA2Individual)) {
                throw new WrongIndividualException(NSGA2Individual.class,
                        ind.getClass());
            }

            if (((NSGA2Individual) ind).getRank() == front) {
                list.add((NSGA2Individual) ind);
            }
        }

        return list;
    }
}
