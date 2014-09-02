/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g17;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.g17.*;

/**
 *
 * @author pilar
 */
public class G17Test {

    /**
     * 0 ≤ x1 ≤ 400, 0 ≤ x2 ≤ 1000, 340 ≤ x3 ≤ 420, 340 ≤ x4 ≤ 420, −1000 ≤ x5 ≤ 1000
     * and 0 ≤ x6 ≤ 0.5236
     * @param values
     */
    public static void normalize(double[] values) {

        values[0] = (values[0]/200.0) - 1.0;
        values[1] = (values[1]/500.0) - 1.0;
        values[2] = (values[2] - 340.0)/40.0 - 1.0;
        values[3] = (values[3] - 340.0)/40.0 - 1.0;
        values[4] = (values[4])/1000.0;
        values[5] = (values[5]/0.2618) - 1.0;


    }

}
