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
 * MOPReplaceOperator.java
 *
 * Created on 21 de diciembre de 2006, 16:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.replace;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.NSGA2Algorithm;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.CrowdingDistanceComparator;
import es.udc.gii.common.eaf.algorithm.population.NSGA2Individual;
import es.udc.gii.common.eaf.exception.WrongAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The replace operator for the {@link NSGA2Algorithm}.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class NSGA2ReplaceOperator extends ReplaceOperator {

    /** Creates a new instance of MOPReplaceOperator */
    public NSGA2ReplaceOperator() {
    }

    @Override
    protected List<Individual> replace(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation) {

        if (!(algorithm instanceof NSGA2Algorithm)) {
            throw new WrongAlgorithmException(NSGA2Algorithm.class,
                    algorithm.getClass());
        }

        NSGA2Algorithm nsga2 = (NSGA2Algorithm) algorithm;

        int popSize = nsga2.getPopulation().getSize();
        List<Individual> newPopulation = new ArrayList<Individual>(popSize);
        List<Individual> wholePop = new ArrayList<Individual>(2 * popSize);

        wholePop.addAll(nsga2.getPopulation().getIndividuals());
        wholePop.addAll(toPopulation);

        int nFronts = nsga2.getRanking().calculate(wholePop);

        List<List<NSGA2Individual>> fronts = getFronts(wholePop, nFronts);

        for (int f = 0; (f < nFronts) && (newPopulation.size() <= popSize); f++) {
            List<NSGA2Individual> front = fronts.get(f);
            nsga2.getCrowding().calculate(front);
            if (fronts.get(f).size() <= (popSize - newPopulation.size())) {
                newPopulation.addAll(front);
            } else {
                Collections.sort(front,
                        new CrowdingDistanceComparator<NSGA2Individual>());
                newPopulation.addAll(front.subList(0,
                        popSize - newPopulation.size()));
            }
        }

        return newPopulation;
    }

    private List<List<NSGA2Individual>> getFronts(List<Individual> pop, int nFronts) {

        List<List<NSGA2Individual>> fronts = new ArrayList<List<NSGA2Individual>>(nFronts);

        for (int i = 0; i < nFronts; i++) {
            fronts.add(new ArrayList<NSGA2Individual>());
        }

        for (Individual ind : pop) {
            fronts.get(((NSGA2Individual) ind).getRank()).add((NSGA2Individual) ind);
        }

        return fronts;
    }

    @Override
    public String toString() {
        return "NSGA2 Replace Operator";
    }
}
