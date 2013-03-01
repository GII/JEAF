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
 * PopulationLogTool.java
 *
 * Created on April 3, 2008, 4:30 PM
 *
 */

package es.udc.gii.common.eaf.log.parallel;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import java.util.Observable;

/**
 * Logs the population in each state in a parallel evolutionary algorithm.<p/>
 *
 * The output reads as follows:
 *
 * <pre>
 * State: ...
 * Generations: ...
 * Population:
 * ...
 * </pre>
 *
 * The "{@code ...}" are replaced by the current state of the algorithm, the
 * number of generations so far and the whole population repectively.<p/>
 *
 * In an island model this log tool logs each population of each island in an
 * independent file. For a distributed evaluation model, only a file is needed.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class PopulationLogTool extends ParallelLogTool{
    
    /** Creates a new instance of PopulationLogTool */
    public PopulationLogTool() {
    }
    
    @Override
    public String getLogID() {
        return "PopulationLogTool";
    }
    
    @Override
    public void update(Observable o, Object arg) {        
        ParallelEvolutionaryAlgorithm pea = (ParallelEvolutionaryAlgorithm)o;        
        
        if(pea.getCurrentObservable() instanceof EvolutionaryAlgorithm) {
            super.update(o, arg);
            EvolutionaryAlgorithm ea = (EvolutionaryAlgorithm)pea.getCurrentObservable();
            
            String state = "";
            
            switch(ea.getState()) {
                case EvolutionaryAlgorithm.EVALUATE_STATE:
                    state = "Evaluate state";
                    break;
                case EvolutionaryAlgorithm.FINAL_STATE:
                    state = "Final state";
                    break;
                case EvolutionaryAlgorithm.INIT_EVALUATE_STATE:
                    state = "Init evaluate state";
                    break;
                case EvolutionaryAlgorithm.INIT_STATE:
                    state = "Init state";
                    break;
                case EvolutionaryAlgorithm.REPLACE_STATE:
                    state = "Replace state";
                    break;
                case EvolutionaryAlgorithm.REPRODUCTION_STATE:
                    state = "Reproduction state";
                    break;
                case EvolutionaryAlgorithm.SELECT_STATE:
                    state = "Select state";
                    break;
            }
            getLog().println("State: " + state + "\nGeneration: " +
                ea.getGenerations() + "\nPopulation:\n" + ea.getPopulation() + "\n");
        }
    }
    
}
