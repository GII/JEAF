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
package es.udc.gii.common.eaf.plugin.parameter;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.plugin.stoptest.FesPlugin;
import es.udc.gii.common.eaf.plugin.stoptest.StopTestPlugin;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class LogAnnealing extends Parameter {

    private StopTestPlugin counter;

    public LogAnnealing() {
        this.counter = new FesPlugin();
    }

    public LogAnnealing(StopTestPlugin counter) {
        this.counter = counter;
    }

    @Override
    public double get(EvolutionaryAlgorithm algorithm) {
        return 1.0d - Math.log10(9.0d * counter.getCurrent(algorithm) / counter.getMax(algorithm) + 1.0d);
    }

    @Override
    public void configure(Configuration conf) {
        try {
            if (conf.containsKey("Counter.Class")) {
                this.counter = (StopTestPlugin) Class.forName(conf.getString("Counter.Class")).newInstance();
                this.counter.configure(conf.subset("Counter"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
