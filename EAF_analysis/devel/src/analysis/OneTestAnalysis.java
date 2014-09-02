/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analysis;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.stoptest.StopTest;

/**
 *
 * @author pilar
 */
public class OneTestAnalysis extends EAFAnalysis {
    
    private int num_test;
    
    public OneTestAnalysis(int num_test) {
        this.num_test = num_test;
    }

    @Override
    public void run(EvolutionaryAlgorithm algorithm, StopTest stopTest) {
       
        for (int i = 0; i<this.num_test; i++) {
            algorithm.resolve(stopTest);
        }
        
    }

   
    
}
