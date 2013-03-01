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
package es.udc.gii.common.eaf.plugin.multiobjective.crowding;

import es.udc.gii.common.eaf.algorithm.fitness.comparator.MinimizingObjectiveComparator;
import es.udc.gii.common.eaf.algorithm.population.NSGA2Individual;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * A crowding plugin. The objective values are considered for measuring the
 * crowding distance of each individual.<p>
 * 
 * IMPORTANT: It is asumed that all objectives are minimized.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ObjectiveSpaceCrowding extends Crowding {

    public ObjectiveSpaceCrowding() {
    }

    @Override
    public void calculate(List<NSGA2Individual> list) {

        /* Reset all crowding distances. */
        resetCrowdingDistance(list);

        if (list == null || list.isEmpty()) {
            return;
        }

        /* Comparator. */
        MinimizingObjectiveComparator<NSGA2Individual> comparator = 
                new MinimizingObjectiveComparator<NSGA2Individual>();
        
        /* Number of objectives. */
        int nObjectives = list.get(0).getObjectives().size();

        /* For each objective. */
        for (int obj = 0; obj < nObjectives; obj++) {

            /* Set the objective to consider. */
            comparator.setObjectiveIndex(obj);

            /* Sort individuals considering the objective above. */
            Collections.sort(list, comparator);

            /* Individuals on the boundaries have maximal crowding distance. */
            NSGA2Individual firstInd = list.get(0);
            firstInd.setCrowdingDistance(Double.MAX_VALUE);

            NSGA2Individual lastInd = list.get(list.size() - 1);
            lastInd.setCrowdingDistance(Double.MAX_VALUE);

            double minValue = firstInd.getObjectives().get(obj);
            double maxValue = lastInd.getObjectives().get(obj);

            /* If there are diferent values, i.e. if there is some distance
             * between individuals in objective space.
             */
            if (minValue != maxValue) {

                /* Calculate the increase of crowding distance 
                for each individual. */
                for (int i = 1; i < list.size() - 1; i++) {
                    NSGA2Individual i0 = list.get(i);
                    NSGA2Individual i1 = list.get(i - 1);
                    NSGA2Individual i2 = list.get(i + 1);

                    double delta =
                            (i2.getObjectives().get(obj) - i1.getObjectives().get(obj)) / (maxValue - minValue);

                    i0.increaseCrowdingDistance(delta);
                }
            }
        }

        for (int i = 1; i < list.size() - 1; i++) {
            NSGA2Individual ind = list.get(i);
            double cd = ind.getCrowdingDistance() / (double) nObjectives;
            ind.setCrowdingDistance(cd);
        }
    }

    /**
     * Configures this plugin<p/>
     *
     * @param conf
     */
    @Override
    public void configure(Configuration conf) {
        // nothing needed
    }
}
