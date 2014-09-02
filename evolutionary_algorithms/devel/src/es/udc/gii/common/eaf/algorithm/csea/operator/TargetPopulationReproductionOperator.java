/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.algorithm.csea.operator;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.csea.population.TargetPopulation;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.ReproductionOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.OperatorException;
import java.util.List;

/**
 *
 * @author pilar
 */
public class TargetPopulationReproductionOperator extends ReproductionOperator {

    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm, List<Individual> individuals) throws OperatorException {
        
        
        TargetPopulation.getInstance().setTargetPopulation(individuals);
        
        return individuals;
        
    }
    
}
