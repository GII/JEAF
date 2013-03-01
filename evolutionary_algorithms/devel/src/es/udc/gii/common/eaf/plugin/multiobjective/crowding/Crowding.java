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
package es.udc.gii.common.eaf.plugin.multiobjective.crowding;

import es.udc.gii.common.eaf.algorithm.population.NSGA2Individual;
import es.udc.gii.common.eaf.plugin.Plugin;
import java.util.List;

/**
 * This plugin calculates the crowding distance of a NSGA2Individual. Look at
 * the different subclases for details on what criteria is followed to calculate
 * this distance.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class Crowding implements Plugin {

    /**
     * Resets the crowding distance of each individual to cero.
     * @param list - List of individuals.
     */
    public void resetCrowdingDistance(List<NSGA2Individual> list) {
        if (list != null && !list.isEmpty()) {
            for (NSGA2Individual i : list) {
                i.setCrowdingDistance(0.0d);
            }
        }
    }

    /**
     * Calculates the crowding distance of each individual of a given list of
     * individuals.
     * @param list - List of individuals.
     */
    public abstract void calculate(List<NSGA2Individual> list);

    @Override
    public String toString() {
        return "Crowding plugin";
    }
}
