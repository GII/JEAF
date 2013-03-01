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
 * SwapMutation.java
 *
 * Created on 27 de noviembre de 2006, 21:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a swap mutation operator. When this operator is applied to an individual, two
 * genes at random positions are swaped.<p>
 *
 * This operator does not need any kind of configuration.<p>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class SwapMutation extends MutationOperator {
    
    /** Creates a new instance of SwapMutation */
    public SwapMutation() {
    }
    
    public SwapMutation(int probability) {
        
        super(probability);
        
    }
    
    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm,
            Individual individual) {
        
        int i, j;
        double[] chrom = individual.getChromosomeAt(0);
        double geneI, geneJ;
        List<Individual> mutated_individual;
        
        mutated_individual = new ArrayList<Individual>();
        
        /*--- Select two bits at random (can be same) ---*/
        i = (int)Math.floor(EAFRandom.nextDouble()*chrom.length);
        j = (int)Math.floor(EAFRandom.nextDouble()*chrom.length);
        
        geneI = chrom[i];
        geneJ = chrom[j];
        
        chrom[i] = geneJ;
        chrom[j] = geneI;
                       
        individual.setChromosomeAt(0, chrom);
        mutated_individual.add(individual);
        /*--- Swap the elements ---*/
        //Collections.swap(chrom.getGenes(),i,j);
        return mutated_individual;
    }
    
}
