/*
 * Copyright (C) 2012 Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.common.eaf.plugin.cma;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class SuperlinearRecombinationType extends RecombinationType {

    @Override
    public double[] calculateWeights(int mu) {
        
        double[] weights;
        
        weights = new double[mu];
        for (int i = 0; i < mu; i++) {
            weights[i] = Math.log(mu + 1) - Math.log(i + 1);
        }
        
        return weights;
    }
    
    
    
}
