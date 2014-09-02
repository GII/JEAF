/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.plugin.multiobjective.asf;

import es.udc.gii.common.eaf.algorithm.population.multiobjective.MONSGA2Individual;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author pilar
 */
public class MOASFStrategy implements ASFStrategy<MONSGA2Individual> {
    
    public void calculate(List<MONSGA2Individual> individuals) {
        
        double ASF;
        int m;
                
        
        for (MONSGA2Individual i : individuals) {
        
            m = 0;
            
            //El ASF en este caso es el valor máximo de todos sus objetivos trasladados
            ASF = Collections.max(i.getTranslatedObjectives());            
            
            i.setASF(ASF);
        
        }
        
    }

    @Override
    public void configure(Configuration conf) {
        //Do nothing
    }
    
}
