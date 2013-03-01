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

import es.udc.gii.common.eaf.algorithm.CMAEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.plugin.Plugin;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public abstract class RecombinationType implements Plugin {

    @Override
    public void configure(Configuration conf) {
        
    }
    
    public double[] getWeights(int mu) {
        double[] weights = this.calculateWeights(mu);
        
        this.normalizeWeigths(weights);
        
        return weights;
    }
    
    public abstract double[] calculateWeights(int mu);
    
    private void normalizeWeigths(double[] weights) {
    
        double sum = 0.0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
        }
        
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= sum;
        }
        
        
    }
    
}
