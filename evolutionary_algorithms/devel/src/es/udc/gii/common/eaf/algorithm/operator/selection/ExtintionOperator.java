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
 * CalculateStateOperator.java
 *
 * Created on 4 de diciembre de 2006, 19:35
 *
 * To change this template, choose Tools | Template Manager
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
import es.udc.gii.common.eaf.plugin.Plugin;
import es.udc.gii.common.eaf.plugin.individual.BestIndividual;
import es.udc.gii.common.eaf.util.EAFMath;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This operator allows calculating the surviving individuals throug their
 * relations, i.e., as a sum of penalties and benefits. The state of a given
 * individual <i>S<sub>i</sub></i> will be given by:<p>
 * 
 * <i>S<sub>i</sub>(t+1) = 1, if &#8721;W<sub>i,j</sub>(t) &ge; 0</i><p>
 * <i>S<sub>i</sub>(t+1) = 0, otherwise </i><p>
 * 
 * where <i>t</i> is the generation number and <i>W<sub>i,j</sub> = W(p<sub>i</sub>
 * ,p<sub>j</sub>)</i> is calculated according to: <p>
 * 
 * <i>W<sub>i,j</sub>=(f(p<sub>i</sub>)-f(p<sub>j</sub>))/(|p<sub>i</sub>-
 * p<sub>j</sub>|)</i><p>
 * 
 * Configuration: <p>
 *
 * <pre>
 * {@code
 * <Operator><p>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.ExtintionOperator</Class>
 * </Operator>
 * }
 * </pre>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ExtintionOperator extends SelectionOperator {

    public ExtintionOperator() {
    }

    @Override
    public void configure(Configuration conf) {
    }

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
                throw new OperatorException("Wrong comparator for Extintion operator");
            }
        }
        for (int i = 0; i < individuals.size(); i++) {
            indA = (MaIndividual) individuals.get(i);
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

        // We always need at least one survivor
        if (survivors == 0) {
            BestIndividual chooser = new BestIndividual();
            ((MaIndividual) chooser.get(algorithm, individuals, null)).setSurvivor(true);
        }

        return individuals;
    }

    @Override
    protected Individual select(EvolutionaryAlgorithm algorithm, List<Individual> individuals) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected abstract class Wxy implements Plugin {

        abstract double get(MaIndividual indA, MaIndividual indB);
    }

    protected class WxyMaximizing extends Wxy {

        @Override
        double get( MaIndividual indA, MaIndividual indB) {
            double distance, wxy;

            distance = EAFMath.distance(indA.getChromosomeAt(0), indB.getChromosomeAt(0));
            if (distance != 0.0) {
                wxy = (indA.getFitness() - indB.getFitness()) / distance;
            } else {
                wxy = 0;
            }
            return wxy;
        }

        @Override
        public void configure(Configuration conf) {
        }
    }

    protected class WxyMinimizing extends Wxy {

        @Override
        double get( MaIndividual indA, MaIndividual indB) {
            double distance, wxy;

            distance = EAFMath.distance(indB.getChromosomeAt(0), indA.getChromosomeAt(0));
            if (distance != 0.0) {
                wxy = (indB.getFitness() - indA.getFitness()) / (distance);
            } else {
                wxy = 0;
            }
            return wxy;
        }

        @Override
        public void configure(Configuration conf) {
        }
    }
}
