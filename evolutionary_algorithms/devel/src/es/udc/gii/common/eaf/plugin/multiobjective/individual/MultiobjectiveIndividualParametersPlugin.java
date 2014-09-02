/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.plugin.multiobjective.individual;

import es.udc.gii.common.eaf.algorithm.population.multiobjective.MultiobjectiveIndividual;
import es.udc.gii.common.eaf.plugin.Plugin;
import java.util.List;

/**
 *
 * @author pilar
 */
public interface MultiobjectiveIndividualParametersPlugin<T extends MultiobjectiveIndividual> extends Plugin {

    public abstract void calculateParameters(List<T> individuals);

    
}
