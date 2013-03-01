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
 * FlatCrossOver.java
 *
 * Created on 28 de febrero de 2007, 19:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.real_code;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.CrossOverOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.List;

/**
 * An offspring, H = (h<sub>1</sub>...h<sub>i</sub>...h<sub>n</sub>), is 
 * generated, where h<sub>i</sub> is a randomly (uniformly) chosen value of the 
 * interval [c<sup>1</sup>, c<sup>2</sup>].<p>
 * 
 * To config this operator add these lines to the config file:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.real_code.FlatCrossOver</Class>
 *      <Probability>value</Probability>
 * </Operator>
 * }
 * <pre>
 *
 * Where probability is a double value in the range [0, 100] which represents the probability of crossover.
 * If this does not appear in the configuration, the parameter is set to the default values. <p>
 *
 * Default values: <p>
 * <ul>
 * <li>Probability is set to 60.0</li>
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class FlatCrossOver extends CrossOverOperator {
    
    /** Creates a new instance of FlatCrossOver */
    public FlatCrossOver() {
    }
    
    
    //An offspring H = (h1,...,hi,...hn), is generated where hi is a randomly
    //(uniformly) choosen value of the interval[c1i,c2i]:
    @Override
    protected List<Individual> crossOver(EvolutionaryAlgorithm ea, List<Individual> individuals) {
        
        double[] genes1, genes2;
        Double gene1, gene2, newGene1, newGene2, range;
        
        genes1 = individuals.get(0).getChromosomeAt(0);
        genes2 = individuals.get(1).getChromosomeAt(0);
        
        for (int i = 0; i<genes1.length; i++) {
            
            gene1 = genes1[i];
            gene2 = genes2[i];
            
            range = gene2-gene1;
            
            newGene1 = range*EAFRandom.nextDouble() - gene1;
            newGene2 = range*EAFRandom.nextDouble() - gene1;
            
            genes1[i] =  checkBounds(ea, newGene1);
            genes2[i] = checkBounds(ea, newGene2);
        }       
        
        return individuals;
        
    }

}
