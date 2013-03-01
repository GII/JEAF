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
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import java.util.List;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 * A parallel log tool for logging the best fitness of a population. The
 * best fitness is logged when user defined objective function evaluations
 * have been performed.<p/>
 *
 * Configuration:
 *
 * <pre>
 * {@code 
 * <Fes_Prints>...</Fes_Prints>
 * <Fes_Prints>...</Fes_Prints>
 * ...}
 * </pre>
 *
 * Each of the {@code Fes_Prints} (one or more) parameters states after how many objective
 * function evaluations the best fitness of the population is logged. The
 * log is written after the replace state and after the final state.<p/>
 *
 * The output reads as follows:
 * The best fitness per line per {@code Fes_Prints}.
 *
 * In an island model this log tool logs for each island in an independent file.
 * For a distributed evaluation model, only one file is needed.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 * @see ParallelBestFEsLogTool
 */
public class ParallelFEsLogTool extends ParallelLogTool {

    private int num_prints = 0;
    private List fes_prints;
    private int fes_index = 0;

    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);
            this.fes_prints = conf.getList("Fes_Prints");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getLogID() {
        return "ParallelFEsLogTool";
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

            int algorithm_fes;

            best = IndividualsProductTrader.get(bestSpec,
                    algorithm.getPopulation().getIndividuals(), 1, pea.getComparator()).get(0);

            if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE) {
                algorithm_fes = algorithm.getFEs();
                if (num_prints < this.fes_prints.size()
                        && algorithm_fes >= Integer.parseInt((String) this.fes_prints.get(fes_index))) {
                    super.getLog().println(
                            best.getFitness());
                    num_prints++;
                    fes_index++;

                }
            }

            if (algorithm.getState() == EvolutionaryAlgorithm.FINAL_STATE) {
                while (num_prints < this.fes_prints.size()) {
                    super.getLog().println(
                            best.getFitness());
                    num_prints++;
                }
            }
        }
    }
}
