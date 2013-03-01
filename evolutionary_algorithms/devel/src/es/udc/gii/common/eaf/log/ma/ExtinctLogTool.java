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

package es.udc.gii.common.eaf.log.ma;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.MacroevolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.MaIndividual;
import es.udc.gii.common.eaf.exception.WrongAlgorithmException;
import es.udc.gii.common.eaf.log.LogTool;
import java.util.Observable;

/**
 * Logs the extincted individuals in each generation of a
 * {@link MacroevolutionaryAlgorithm}. Each line represents an individual.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ExtinctLogTool extends LogTool {

    public ExtinctLogTool() {
    }

    @Override
    public String getLogID() {
        return "Extinct";
    }

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);
        
        if(!(o instanceof MacroevolutionaryAlgorithm)) {
            throw new WrongAlgorithmException(MacroevolutionaryAlgorithm.class, o.getClass());
        }
        
        MacroevolutionaryAlgorithm ma = (MacroevolutionaryAlgorithm)o;
        
        if(ma.getState() == EvolutionaryAlgorithm.SELECT_STATE) {
            int extinct = 0;
            for(Individual i : ma.getPopulation().getIndividuals()) {
                if(!((MaIndividual)i).isSurvivor()) {
                    extinct++;
                }
            }
            getLog().println(extinct);
        }
    }      

}
