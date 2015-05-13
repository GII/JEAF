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
 * BinCrossOverScheme.java
 *
 * Created on 30 de agosto de 2007, 12:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.crossover;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;

/**
 * This class represents a specific implementation of a CrossOverScheme. Specifically, this class
 * implements the exponential crossover scheme.<p>
 *
 * The exponential crossover was designed to be similar to the one point and two point crossover variants,
 * generally used in genetic algorithms. This cross over scheme constructs the trial vector by taking 
 * consecutive components (in a circular manner) from the mutant vector, as we describe here:<p>
 *
 * z<sub>i</sub><sup>j</sup> is equal to v<sub>i</sub><sup>j</sup> (the trial vector), while U<sub>i</sub>
 * &le; CR, where U<sub>i</sub> is a random value. Otherwise, z<sub>i</sub><sup>j</sup> is equal to x<sub>i</sub><sup>j</sup> (the
 * target vector).<p>
 *
 * To use this specific cross over scheme, the xml configuration code should have the
 * configuration of the CR parameter. So the xml code should be like this:<p>
 *
 * <pre>
 * {@code
 * <CrossOverScheme>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.crossover.ExpCrossOverScheme</Class>
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
public class ExpCrossOverScheme extends CrossOverScheme {

    /** Creates a new instance of ExpCrossOverScheme */
    public ExpCrossOverScheme() {
    }

    @Override
    public Individual crossOver(EvolutionaryAlgorithm ea, Individual target, Individual v) {

        double[] trial;
        int k, L;
        int size;
        double CR = this.getCRPlugin().get(ea);

        trial = target.getChromosomeAt(0);
        size = trial.length;

        //Punto de partida:
        k = EAFRandom.nextInt(size);
        L = 0;

        double[] vArray = v.getChromosomeAt(0);
        do {
            trial[k] = vArray[k];
            k = (k+1) % size;
            L++;

        } while (EAFRandom.nextDouble() < CR && L < size);

                Individual crossoverIndividual = new Individual();
        crossoverIndividual.setChromosomeAt(0, trial);
        
        return crossoverIndividual;

    }
}
