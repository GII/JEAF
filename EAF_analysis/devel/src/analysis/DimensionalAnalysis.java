/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analysis ;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.stoptest.StopTest;


/**
 * Clase que implementa un análisis poblacional de un algoritmo para una función de calidad.
 * 
 * @author pilar
 */
public class DimensionalAnalysis extends CompositeAnalysis {
    
    private int init_dim;
    
    private int end_dim;
    
    private int step_dim;
    
    public DimensionalAnalysis(int init_dim, int end_dim, int step_dim,
            EAFAnalysis succesor) {
    
            super(succesor);
            this.init_dim = init_dim;
            this.end_dim = end_dim;
            this.step_dim = step_dim;
        
    }

    @Override
    public void run(EvolutionaryAlgorithm algorithm, StopTest stopTest) {
        
        int[] dimension = new int[1];
        
        for (int dim = init_dim; dim <= end_dim; dim += step_dim) {
            
            dimension[0] = dim;
            
            algorithm.getPopulation().setDimension(dimension);
            
            
            this.succesor.run(algorithm, stopTest);
            
        }
        
    }
    
}
