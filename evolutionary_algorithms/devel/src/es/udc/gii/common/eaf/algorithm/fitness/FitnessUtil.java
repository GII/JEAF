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
 * FitnessUtil.java
 *
 * Created on 11 de noviembre de 2006, 22:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.algorithm.fitness;

import java.util.List;
import static org.apache.commons.math.stat.StatUtils.*;
import es.udc.gii.common.eaf.algorithm.population.Individual;

/**
 * The class <tt>FitnessUtil</tt> contains methods for performing fitness 
 * statistical operations over individuals. This operations could be mean fitness 
 * value of an individuals' list, etc.
 * 
 * Note: This class use the {@link org.apache.commons.math.stat.StatUtils} class.
 * This class provides several of statistical methods.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 *
 */
public class FitnessUtil {
    
    /**
     * This method recive a list of individuals and return the mean fitness value.
     * @param individuals a list of individuals to which we will compute the 
     * mean fitness value.
     * @return a double representing the mean fitness value.
     */
    public static double meanFitnessValue(List<Individual> individuals) {
        
        double[] arrayFitness;
        
        arrayFitness = getArrayFitness(individuals);
        
        return mean(arrayFitness);
        
    }
    
    /**
     * This method recive a list of individuals and return the sum fitness value.
     * @param individuals a list of individuals to which we will compute the 
     * sum of the fitness value.
     * @return a double representing the sum fitness value.
     */
    public static double sumFitnessValue(List<Individual> individuals) {
        
        double[] arrayFitness;
        
        arrayFitness = getArrayFitness(individuals);
        
        return sum(arrayFitness);
        
    }
    
    
    /**
     * This method receive a list of individuals and return an array containing 
     * the fitness value of each individual.
     * @param individuals a list of individuals.
     * @return an array containing the fitness value of each individual in 
     * the list.
     */
    private static double[] getArrayFitness(List<Individual> individuals) {
        
        Individual[] arrayIndividuals = new Individual[individuals.size()];
        double[] arrayFitness = new double[individuals.size()];
        
        arrayIndividuals = individuals.toArray(arrayIndividuals);
        
        for (int i = 0; i<arrayIndividuals.length; i++) {
            
            arrayFitness[i] = arrayIndividuals[i].getFitness();
            
        }
        
        return arrayFitness;
        
    }
    
}
