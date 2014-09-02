/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analysis;

/**
 *
 * @author pilar
 */
public abstract class CompositeAnalysis extends EAFAnalysis {

    protected EAFAnalysis succesor;
    
    public CompositeAnalysis(EAFAnalysis succesor) {
        this.succesor = succesor;
    }
    
}
