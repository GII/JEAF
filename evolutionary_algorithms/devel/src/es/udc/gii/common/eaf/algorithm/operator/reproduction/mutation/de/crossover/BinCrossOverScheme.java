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

package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.crossover;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.plugin.parameter.Parameter;
import es.udc.gii.common.eaf.util.EAFRandom;

/**
 * This class represents a specific implementation of a CrossOverScheme. Specifically, this class
 * implements the binomial crossover scheme.<p>
 *
 * The binomial crossover constructs the trial vector by taking, in a random manner, elements
 * either from the mutant vector or from the current element, as we describe here:<p>
 *
 * z<sub>i</sub><sup>j</sup> is equal to v<sub>i</sub><sup>j</sup> (the trial vector), if U<sub>i</sub>
 * < CR or j = k, where U<sub>i</sub> is a random value. Otherwise, z<sub>i</sub><sup>j</sup> is equal to x<sub>i</sub><sup>j</sup> (the
 * target vector).<p>
 * 
 * To use this specific cross over scheme, the xml configuration code should have the
 * configuration of the CR parameter. So the xml code should be like this:<p>
 *
 * <pre>
 * {@code
 * <CrossOverScheme>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.crossover.BinCrossOverScheme</Class>
 *      <CR>value</CR>
 *      ...
 * <CrossOverScheme>
 * }
 * </pre>
 *
 * where the tag CR indicates the plugin used. If some of the parameters do not appear in the configuration, they are set
 * to their default values.<p>
 *
 * Default values:
 * <ul>
 * <li>CR is set to Constant parameter with value 0.1</li>
 * </ul>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class BinCrossOverScheme extends CrossOverScheme {
    
    /** Creates a new instance of BinCrossOverScheme */
    public BinCrossOverScheme() {
    }

    public BinCrossOverScheme(Parameter CR) {
        super(CR);
    }

    @Override
    public Individual crossOver(EvolutionaryAlgorithm ea, Individual target, Individual v) {
        
        Individual trial;
        int size, randomI;
        double[] trial_chromosome;
        double CR;
        
        trial = (Individual)target.clone();
        size = target.getChromosomeAt(0).length;
        randomI = (int)Math.floor(EAFRandom.nextDouble()*size);
        CR = this.getCRPlugin().get(ea);
        
        trial_chromosome = trial.getChromosomeAt(0);
        for (int j = 0; j<size; j++) {
            
            if (EAFRandom.nextDouble() <= CR || randomI == j) {
                
                trial_chromosome[j] = v.getChromosomeAt(0)[j];
                                
            } else {
                
                trial_chromosome[j] = target.getChromosomeAt(0)[j];
                
            }
            
        }
        
        trial.setChromosomeAt(0, trial_chromosome);
        return trial;
        
    }
    
}
