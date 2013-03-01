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
package es.udc.gii.common.eaf.log;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.List;
import java.util.Observable;

/**
 * This log tool implements a log tool that record the whole algorithm population after the replace
 * stage of the algorithm. This log tool does not need any kind of configuration, apart from the
 * configuration need in the superclass.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class PopulationFitnessLogTool extends LogTool {

    /** Creates a new instance of BestMeanLogTool */
    public PopulationFitnessLogTool() {
    }

    @Override
    public void update(Observable o, Object arg) {

        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm) o;
        List<Individual> individuals;

        super.update(o, arg);
        if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE /*||
                algorithm.getState() == EvolutionaryAlgorithm.FINAL_STATE */) {
            individuals = algorithm.getPopulation().getIndividuals();

            super.getLog().println("pop size    = " + individuals.size());
            super.getLog().println("generation  = " + algorithm.getGenerations());
            super.getLog().println("evaluations = " + algorithm.getFEs());

            super.getLog().println();

            for (Individual i : individuals) {
                super.getLog().println(i.getFitness());

            }

            super.getLog().println("\n###############################\n");
        }
    }

    @Override
    public String toString() {
        return "population";
    }
}
