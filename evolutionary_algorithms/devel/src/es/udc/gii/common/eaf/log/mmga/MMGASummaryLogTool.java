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
 * MMGASummaryLogTool.java
 *
 * Created on November 19, 2007, 1:47 PM
 *
 */

package es.udc.gii.common.eaf.log.mmga;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.mga.MMGAAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.log.LogTool;
import java.util.Observable;

/**
 * Logs a summary of a run of a multiobjective micro-genetic algorithm.<p/>
 *
 * The output reads as follows:
 * <pre>
 * *** Summary log for ... algorithm name ... ***
 * - solutions =
 * - eval =
 * - gen =
 * - objectives =
 * - not factible individuals =
 *
 * # ... objective values of the first individual in the Pareto front ...
 * # ... objective values of the second individual in the Pareto front ...
 * # ...
 * # ... objective values of the last individual in the Pareto front ...
 *
 * ... list of individuals in the Pareto front ...
 * </pre>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 * @see MMGAAlgorithm
 */
public class MMGASummaryLogTool extends LogTool {
    
    /**
     * Creates a new instance of MMGASummaryLogTool
     */
    public MMGASummaryLogTool() {
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
        /* Cambiado por rafa: adaptado a la nueva definicion de Individual */
        
        MMGAAlgorithm alg = (MMGAAlgorithm)o;
        
        super.update(o, arg);
        
        if(alg.getState() == EvolutionaryAlgorithm.FINAL_STATE) {
            
            super.getLog().print("*** Summary log for " + alg.toString());
            super.getLog().print(" ***\n\n");
            
            super.getLog().println("- solutions = " + alg.getParetoFront().getSize());
            super.getLog().println("- eval = " + alg.getFEs());
            super.getLog().println("- gen = " + alg.getGenerations());
                        
            if(alg.getParetoFront().getSize() > 0) {
                super.getLog().println("- objectives = " +
                    alg.getParetoFront().getIndividuals().get(0).getObjectives().size());
            } else {
                super.getLog().println("- objectives = ?");
            }
            
            int notFactible = 0;
            
            for (Individual ind : alg.getParetoFront().getIndividuals()) {
                if(ind.getViolatedConstraints() < 0) {
                    notFactible++;
                }
            }
            
            super.getLog().println("- not factible individuals = " + notFactible);
            
            super.getLog().println("\n");
            
            for (Individual ind : alg.getParetoFront().getIndividuals()) {
                super.getLog().print("#");
                for (Double obj : ind.getObjectives()) {
                    super.getLog().print(" " + obj);
                }
                super.getLog().println();
            }
            
            super.getLog().println("\n");
            super.getLog().println(alg.getParetoFront());
        }
    }
    
    @Override
    public String toString() {
        return "mmgasummary";
    }
}
