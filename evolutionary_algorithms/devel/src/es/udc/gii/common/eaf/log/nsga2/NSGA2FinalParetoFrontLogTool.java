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

package es.udc.gii.common.eaf.log.nsga2;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.NSGA2Algorithm;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.NSGA2Individual;
import java.io.PrintStream;
import java.util.List;
import java.util.Observable;

/**
 * Logs the final Pareto-front of an evolution with the {@link NSGA2Algorithm}. 
 * The * total generations, the front size and each individual belonging to the front
 * are logged.<p/>
 *
 * The output reads as follows:
 * <pre>
 * Generation: ... generations ...
 * Front size: ... Pareto front size ...
 * ... list of individuals in the Pareto front, one per line ...
 * </pre>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 * @see NSGA2Algorithm
 */
public class NSGA2FinalParetoFrontLogTool extends NSGA2LogTool {

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);
        
        NSGA2Algorithm nsga2 = (NSGA2Algorithm)o;
        
        if(nsga2.getState() == EvolutionaryAlgorithm.FINAL_STATE) {
            PrintStream out = super.getLog();
            List<NSGA2Individual> front = 
                    getFront(nsga2.getPopulation().getIndividuals(), 0);
            out.println("Generation: " + nsga2.getGenerations());
            out.println("Front size: " + front.size() + "\n");
            
            for(NSGA2Individual ind : front) {
                out.println(ind.toString());
            }
        }
    }

    @Override
    public String getLogID() {
        return "NSGA2FinalParetoFrontLogTool";
    }   
}
