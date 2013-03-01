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

package es.udc.gii.common.eaf.plugin.stoptest;

import java.util.List;
import org.apache.commons.configuration.Configuration;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.stoptest.EvolveGenerationsStopTest;
import es.udc.gii.common.eaf.stoptest.StopTest;

/**
 *  This class implements a plugin with the responsability of return the number
 * of current generations executed by the algorithm and the maximum number
 * of generations allowed to execute.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class GenerationsPlugin extends StopTestPlugin {

    /**
     * Maximum number of generations
     */
    private int generations = -Integer.MAX_VALUE;
    
    @Override
    public void configure(Configuration conf) {
    }

    /**
     * Return the current number of generations excuted by the algorithm
     * @param algorithm the algorithm that is in execution.
     * @return the current number of FEs executed by the algorithm.
     */
    @Override
    public int getCurrent(EvolutionaryAlgorithm algorithm) {
        return algorithm.getGenerations();
    }

    @Override
    public int getMax(EvolutionaryAlgorithm algorithm) {
        
        List<StopTest> list_test;
        
        //Si todavía no se ha comprobado el número máximo de FEs ha que comprobarlo
        //mirando los test de parada del algoritmo, si no está definido el test de 
        //parada correspondiente --> hay que lanzar una excepción y el algoritmo
        //no se puede ejecutar.
        if (this.generations == -Integer.MAX_VALUE) {
            
            list_test = this.getStopTestList(algorithm);
            
            for (StopTest s : list_test) {
                
                if (s instanceof EvolveGenerationsStopTest) {
                    this.generations = ((EvolveGenerationsStopTest)s).getGenerations();
                }
                
            }
            
            //Si "max_fes" sigue teniendo como valor -Integer.MAX_VALUE es porque
            //no se ha configurado bien el algoritmo, y no existe el test de parada
            //correspondiente --> se lanza una excepción de configuración:
            if (this.generations == -Integer.MAX_VALUE) {
                throw new ConfigurationException("Wrong Plugin configuration: " +
                        "If you use GenerationsPlugin you must configure an EvolveGenerationsStopTest");
            }
            
        }
        
        return this.generations;
        
    }

}
