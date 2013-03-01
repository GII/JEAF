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
 * MOPMutation.java
 *
 * Created on 19 de diciembre de 2006, 12:52
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
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MOPMutation extends MutationOperator {
    
    private int mutationIndex;
    
    private int mutationsNumber = 0;
    
    /** Creates a new instance of MOPMutation */
    public MOPMutation() {
      
    }
    
    public MOPMutation(int probability) {
        super(probability);
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        this.mutationIndex = conf.getInt("MutationIndex");
    }

    
    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm,
            Individual individual) {
        
        
        double y, yl, yu, delta, val, xy, deltaq, mut_pow, delta2, delta1, rnd;    
        double[] gens = individual.getChromosomeAt(0);
        List<Individual> mutated_individual = new ArrayList<Individual>();
      
        //gens.size() ---> numero de variables
        for(int j=0;j<gens.length;j++) {
            //hemos de mutar todos los genes del individuo
            //pero solo mutara en caso de que se cumpla la condicion de mutacion
            if(EAFRandom.nextDouble()*100 <= super.getProbability()) {
                y = gens[j];
                yl = -1.0;
                yu = 1.0;
                delta1 = (y-yl)/(yu-yl);
                delta2 = (yu-y)/(yu-yl);
                rnd = EAFRandom.nextDouble();
                mut_pow = 1.0/(mutationIndex+1.0);
                if (rnd <= 0.5) {
                    xy = 1.0 - delta1;
                    val = 2.0*rnd + (1.0-2.0*rnd)*(Math.pow(xy,(mutationIndex+1.0)));
                    deltaq =  Math.pow(val,mut_pow) - 1.0;
                }
                else {
                    xy = 1.0 - delta2;
                    val = 2.0*(1-rnd)+2*(rnd-0.5)*(Math.pow(xy,(mutationIndex+1.0)));
                    deltaq = 1.0 - (Math.pow(val,mut_pow));
                }
                y = y + deltaq*(yu-yl);
                if (y<yl)
                    y = yl;
                if (y>yu)
                    y = yu;
                
                gens[j] = checkBounds(algorithm, y);
                increaseMutationsNumber();
            }
       
        }

        individual.setChromosomeAt(0,gens);
        mutated_individual.add(individual);
        //individual.getChromosome().setGenes(gens);
        return mutated_individual;
    }
    
      
   
    
    
     private void increaseMutationsNumber() {
        this.mutationsNumber++;
        //System.out.println("El numero de mutaciones realizadas es = " + this.mutationsNumber);
    }
    
    @Override
    public String toString() {
        return "Multi-Objective Mutation Operator";
    }

}
