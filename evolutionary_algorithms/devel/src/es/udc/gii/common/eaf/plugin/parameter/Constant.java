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

import org.apache.commons.configuration.Configuration;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;

/**
 * A plugin that represents a constant value. <p/>
 *
 * Configuration:
 * <pre>
 * <Value>v</Value>
 * </pre>
 *
 * This configures a constant with value v.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class Constant extends Parameter
{
    private double value;
    
    public Constant(double value)
    {
        this.value = value;
    }
    public Constant()
    {
        value = 0.5;
    }
    @Override
    public void configure(Configuration conf)
    {
        if (conf.containsKey("Value"))
        {
            this.value = conf.getDouble("Value");
        }
    }
    @Override
    public double get(EvolutionaryAlgorithm algorithm)
    {
        return this.value;
    }

}
