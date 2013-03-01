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
package es.udc.gii.common.eaf.log.parallel;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.fitness.FitnessUtil;
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import java.util.Observable;

/**
 * A parallel log tool for logging the best and mean fitness of a population.
 *
 * The output reads as follows:
 *
 * <pre>
 * generations - best fitness - mean fitness
 * </pre>
 *
 * There is a line per generation of the algorithm. The log is written after
 * the replace state.
 *
 * In an island model this log tool logs for each island in an independent file.
 * For a distributed evaluation model, only one file is needed.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ParallelBestMeanLogTool extends ParallelLogTool {

    public ParallelBestMeanLogTool() {
    }  

    @Override
    public String getLogID() {
        return "ParallelBestMeanLogTool";
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
        ParallelEvolutionaryAlgorithm pea = (ParallelEvolutionaryAlgorithm) o;

        if (pea.getCurrentObservable() instanceof EvolutionaryAlgorithm) {
            super.update(o, arg);
            EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm) pea.getCurrentObservable();
            
            BestIndividualSpecification bestSpec =
                    new BestIndividualSpecification();
            Individual best;            
            if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE && arg == null) {
                best = IndividualsProductTrader.get(bestSpec,
                        algorithm.getPopulation().getIndividuals(), 1, pea.getComparator()).get(0);

                super.getLog().println(
                        algorithm.getGenerations() + " - " +
                        best.getFitness() + " - " +
                        FitnessUtil.meanFitnessValue(
                        algorithm.getPopulation().getIndividuals()));
            }
        }
    }
}
