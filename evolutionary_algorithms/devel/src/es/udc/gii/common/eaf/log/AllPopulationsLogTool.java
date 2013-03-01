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
 * AllPopulationsLogTool.java
 *
 * Created on 8 de enero de 2007, 17:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.log;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.NSGA2Individual;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class AllPopulationsLogTool extends LogTool {
    
    /** Creates a new instance of AllPopulationsLogTool */
    public AllPopulationsLogTool() {
    }

    @Override
    public void update(Observable o, Object arg) {
        
        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm)o;
        //NSGA2Individual individual;
        List<Individual> allIndividuals;
        
        super.update(o, arg);
        
        allIndividuals = algorithm.getPopulation().getIndividuals(); 
       
        super.getLog().println("Generation number = " + algorithm.getGenerations() + 
                "- individuals size = " + algorithm.getPopulation().getSize());
        
                
        for(Individual ind:allIndividuals) {
            super.getLog().println(((NSGA2Individual)ind).toString());
        }

     
    }
       
    @Override
    public String toString() {
        return "AllPopulationsLogTool";
    }
    
    @Override
    public String getLogID() {
        return "allpopulation";
    }

        
 }
    

