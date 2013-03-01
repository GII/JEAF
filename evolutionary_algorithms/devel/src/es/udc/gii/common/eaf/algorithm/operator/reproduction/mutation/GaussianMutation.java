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
 * GaussianMutation.java
 *
 * Created on 14 de noviembre de 2006, 12:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import java.util.List;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;


/**
 * This class implements a gaussian mutation operator. When this operator is applied to an
 * individual, with a probability <i>p</i> a random value from an gaussian distribution is added to
 * the gene value.<p>
 *
 * This operator does not need any configuration parameters, except the parameters needed to configure
 * the superclass {@see MutationOperator}. So, to use this operator, the configuration file should
 * contains the following xml code: <p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.GaussianMutation</Class>
 *      <Probability>value</Probability>
 * </Operator>
 * }
 * </pre>
 *
 * Where, <i>Class</i> is the only mandatory tag. If the tag <i>Probability</i> does not appear, is set
 * to its default value.<p>
 *
 * Default values:
 * <ul>
 * <li>Probability default value is 0.5.
 * </ul>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class GaussianMutation extends MutationOperator {
    
//    private Random random;
    
    /** Creates a new instance of GaussianMutation */
    public GaussianMutation() {
        
//        this.random = new Random(System.currentTimeMillis());
        
        
    }
    
    public GaussianMutation(int probability) {
        
        super(probability);
        
    }
    
    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm,
            Individual individual) {
        
        //Recorro todos los genes del chromosoma del individuo y los muto:
        double[] genes = individual.getChromosomeAt(0);
        double newValue, a, b, mutationGene, value;
        Individual newIndividual = (Individual)individual.clone();
        List<Individual> mutated_individual = new ArrayList<Individual>();

        a = -1.0;
        b = 1.0;
        
        
        for (int i = 0; i<genes.length; i++) {
            
            if (super.getProbability() >= EAFRandom.nextDouble()*100) {
                
                mutationGene = genes[i];                              
                
                value = EAFRandom.nextGaussian() +
                        mutationGene;
                genes[i] = checkBounds(algorithm, value);
                
            }
        }
        
        newIndividual.setChromosomeAt(0, genes);
        
        mutated_individual.add(newIndividual);
        return mutated_individual;
        
    }
    
    
}
