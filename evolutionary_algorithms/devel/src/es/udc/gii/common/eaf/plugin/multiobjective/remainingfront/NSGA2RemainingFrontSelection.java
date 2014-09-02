/*
 * Copyright (C) 2013 pilar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.common.eaf.plugin.multiobjective.remainingfront;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.NSGA2Algorithm;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.CrowdingDistanceComparator;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.NSGA2Individual;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author pilar
 */
public class NSGA2RemainingFrontSelection implements RemainingFrontSelectionPlugin<NSGA2Individual> {

    @Override
    public List<NSGA2Individual> selection(EvolutionaryAlgorithm alg, List<NSGA2Individual> newPop,
        List<NSGA2Individual> front, int size) {

        NSGA2Algorithm nsga2 = (NSGA2Algorithm)alg;        

        nsga2.getParametersPlugin().calculateParameters(front);
        
        Collections.sort(front,
                new CrowdingDistanceComparator<NSGA2Individual>());
        newPop.addAll(front.subList(0, size));

        return newPop;
    }

    @Override
    public void configure(Configuration conf) {
        //Do nothing
    }
}
