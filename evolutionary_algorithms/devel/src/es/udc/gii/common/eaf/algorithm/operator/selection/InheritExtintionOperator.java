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
package es.udc.gii.common.eaf.algorithm.operator.selection;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.MacroevolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.MaximizingFitnessComparator;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.MinimizingFitnessComparator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.MaIndividual;
import es.udc.gii.common.eaf.exception.OperatorException;
import es.udc.gii.common.eaf.plugin.individual.BestIndividual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class InheritExtintionOperator extends ExtintionOperator {

    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) throws OperatorException {
        MacroevolutionaryAlgorithm alg = (MacroevolutionaryAlgorithm) algorithm;
        FitnessComparator<Individual> comparator = algorithm.getComparator();
        Wxy wxy;
        MaIndividual indA, indB;
        double weight;
        int survivors = 0;

        if (comparator instanceof MaximizingFitnessComparator) {
            wxy = new WxyMaximizing();
        } else {
            if (comparator instanceof MinimizingFitnessComparator) {
                wxy = new WxyMinimizing();
            } else {
                throw new OperatorException("Wrong comparator for Inherit Extintion operator");
            }
        }
        for (int i = 0; i < individuals.size(); i++) {
            indA = (MaIndividual) individuals.get(i);
            if (indA.getBestFitness() != indA.getFitness()) {
                indA.setSurvivor(false);
            } else {
                for (int j = i + 1; j < individuals.size(); j++) {
                    indB = (MaIndividual) individuals.get(j);
                    if (!indA.isSurvivor() || !indB.isSurvivor()) {
                        alg.setWxy(i, j, wxy.get(indA, indB));
                    }
                }
                weight = 0;
                for (int j = 0; j < i; j++) {
                    weight -= alg.getWxy(j, i);
                }
                for (int j = i + 1; j < individuals.size(); j++) {
                    weight += alg.getWxy(i, j);
                }
                if (weight > 0) {
                    indA.setSurvivor(true);
                    survivors++;
                } else {
                    if (weight < 0) {
                        indA.setSurvivor(false);
                    } else {
                        if (EAFRandom.nextDouble() < 0.5) {
                            indA.setSurvivor(true);
                            survivors++;
                        } else {
                            indA.setSurvivor(false);
                        }
                    }
                }
            }
        }

        // We always need at least one survivor
        if (survivors == 0) {
            BestIndividual chooser = new BestIndividual();
            ((MaIndividual) chooser.get(algorithm, individuals, null)).setSurvivor(true);
        }

        return individuals;
    }
}
