/*
 * Copyright (C) 2012 Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
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
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.Collections;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class TolFunStopTest extends SimpleStopTest {

    private double tol_fun = 1.0e-12;

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {

        //Fitness value to check in this stop test:
        double worst_history, best_history, best_fitness, worst_fitness;

        if (algorithm.getFitnessHistory() != null && algorithm.getFitnessHistory().size() > 0) {
            worst_history = ((Individual) Collections.max(
                    algorithm.getFitnessHistory(), algorithm.getComparator())).getFitness();
            best_history = ((Individual) Collections.min(algorithm.getFitnessHistory(),
                    algorithm.getComparator())).getFitness();

            best_fitness = algorithm.getBestIndividual().getFitness();
            worst_fitness = algorithm.getWorstIndividual().getFitness();

            if ((algorithm.getGenerations() > 1)
                    && (Math.abs(Math.max(worst_history, worst_fitness) 
                    - Math.min(best_history, best_fitness))) <= tol_fun) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void configure(Configuration conf) {

        if (conf.containsKey("TolFun")) {
            tol_fun = conf.getDouble("TolFun");
        } else {
            ConfWarning w = new ConfWarning("TolFun", this.tol_fun);
            w.warn();
        }

    }
}
