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
public abstract class EAFAnalysis {

    public abstract void run(EvolutionaryAlgorithm algorithm, StopTest stopTest);
    
}
