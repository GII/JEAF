/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analysis ;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.config.EAFConfigurationState;
import es.udc.gii.common.eaf.stoptest.StopTest;

/**
 * Clase que implementa un análisis poblacional de un algoritmo para una función de calidad.
 * 
 * @author pilar
 */
public class PopulationAnalysis extends CompositeAnalysis {
    
    private int init_pop;
    
    private int end_pop;
    
    private int step_pop;
    
    public PopulationAnalysis(int init_pop, int end_pop, int step_pop,
            EAFAnalysis succesor) {
    
            super(succesor);
            this.init_pop = init_pop;
            this.end_pop = end_pop;
            this.step_pop = step_pop;
        
    }

    @Override
    public void run(EvolutionaryAlgorithm algorithm, StopTest stopTest) {
        
        for (int pop = init_pop; pop <= end_pop; pop += step_pop) {
            
            EAFConfigurationState.getInstance().setPopulationSize(pop);
            algorithm.getPopulation().modifyPopulationSize(pop);
            
            this.succesor.run(algorithm, stopTest);
            
        }
        
    }
    
}
