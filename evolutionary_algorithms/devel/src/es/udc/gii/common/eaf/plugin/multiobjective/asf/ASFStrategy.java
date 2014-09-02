/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.plugin.multiobjective.asf;

import es.udc.gii.common.eaf.algorithm.population.multiobjective.MultiobjectiveIndividual;
import es.udc.gii.common.eaf.plugin.Plugin;
import java.util.List;

/**
 *
 * @author pilar
 */
public interface ASFStrategy<T extends MultiobjectiveIndividual> extends Plugin {
    
    public void calculate(List<T> individuals);
    
    
}
