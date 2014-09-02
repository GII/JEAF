/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.csea.operator;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.csea.population.EvaluatedPopulation;
import es.udc.gii.common.eaf.algorithm.operator.evaluate.EvaluationOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.OperatorException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pilar
 */
public class EvaluatedPopulationOperator extends EvaluationOperator {

    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm, List<Individual> individuals) throws OperatorException {

        int populationSize = algorithm.getPopulation().getSize();
        int numberOfIndividuals = populationSize;
        List<Individual> evaluatedPopulation = new ArrayList<Individual>();
        List<Individual> auxPopulation;
        
        do {
            
            auxPopulation = EvaluatedPopulation.getInstance().getEvaluatedPopulation(numberOfIndividuals);
            if (auxPopulation != null) {
                evaluatedPopulation.addAll(auxPopulation);
                numberOfIndividuals -= auxPopulation.size();
            }

        } while (evaluatedPopulation.size() < populationSize);

        //System.out.println("EvaluatedPopulationOperator " + individuals.size());

        return evaluatedPopulation;

    }

}
