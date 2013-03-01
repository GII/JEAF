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
public class PopulationLogTool extends LogTool {
    
    /** Creates a new instance of BestMeanLogTool */
    public PopulationLogTool() {}
 
    @Override
    public void update(Observable o, Object arg) {
        
        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm)o;
        List<Individual> individuals;
        double[] chromosome;
        String individual_str;
        
        super.update(o, arg);
        if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE /*||
                algorithm.getState() == EvolutionaryAlgorithm.FINAL_STATE */) {
            individuals = algorithm.getPopulation().getIndividuals();

            //super.getLog().println(algorithm.getGenerations());
//            super.getLog().println(individuals.size() + "\t0");
//            super.getLog().println(algorithm.getGenerations() + " " + algorithm.getFEs());
            for (Individual i : individuals) {

                individual_str = "";
                chromosome = i.getChromosomeAt(0);

                for (int d = 0; d < chromosome.length; d++) {
                    individual_str += chromosome[d] + "\t";
                }
                
                individual_str += i.getFitness();
                super.getLog().println(individual_str);

            }
            
            super.getLog().println();
            super.getLog().println();
//
////            super.getLog().println(algorithm.getPopulation());
//            super.getLog().println("---------------------------");
            
        }
         
        
    }
    
    @Override
    public String toString() {
        return "population";
    }
}
