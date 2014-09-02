/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.population.multiobjective;

import java.util.Comparator;

/**
 *
 * @author pilar
 */
public class ReferencePointDeficiencyCountComparator implements Comparator<ReferencePoint> {

    @Override
    public int compare(ReferencePoint t, ReferencePoint t1) {
        
        if (t.getDeficiencyCount() > t1.getDeficiencyCount()) {
            return -1;
        } else if (t.getDeficiencyCount() < t1.getDeficiencyCount()) {
            return 1;
        }
        
        return 0;
    }
    
}
