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
import es.udc.gii.common.eaf.plugin.stoptest.StopTestPlugin;
import es.udc.gii.common.eaf.plugin.stoptest.GenerationsPlugin;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class LinearAnnealing extends Parameter
{
    private double a;
    private double b;
    private double c;
    private StopTestPlugin counter;
    public LinearAnnealing()
    {
        a = 1.0;
        b = 1.0;
        c = 1.0;
        counter = new GenerationsPlugin();
    }

    @Override
    public void configure(Configuration conf)
    {
        if (conf.containsKey("A"))
        {
            this.a = conf.getDouble("A");
        }
        if (conf.containsKey("B"))
        {
            this.b = conf.getDouble("B");
        }
        if (conf.containsKey("C"))
        {
            this.c = conf.getDouble("C");
        }
        try
        {
            if (conf.containsKey("Counter.Class"))
            {
                this.counter = (StopTestPlugin)Class.forName(conf.getString("Counter.Class")).newInstance();
                this.counter.configure(conf.subset("Counter"));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    public double get(EvolutionaryAlgorithm algorithm)
    {
        double tau = a * 1.0 - (b * counter.getCurrent(algorithm)) / (c * counter.getMax(algorithm));
        
        tau = (tau <= 1) ? tau : 1.0;
        tau = (tau >= 0) ? tau : 0.0;
        return tau;
    }

}
