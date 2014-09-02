/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g10;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.g10.*;

/**
 *
 * @author pilar
 */
public class G10Test {

    /**
     *  100 ≤ x1 ≤ 10000, 1000 ≤ xi ≤ 10000 (i = 2, 3) and 10 ≤ xi ≤ 1000 (i = 4, . . . , 8)
     * @param values
     */
    public static void normalize(double[] values) {


        values[0] = (values[0] - 100.0)/4950.0 - 1.0;
        values[1] = (values[1] - 1000.0)/4500.0 - 1.0;
        values[2] = (values[2] - 1000.0)/4500.0 - 1.0;
        values[3] = (values[3] -10.0)/495.0 - 1.0;
        values[4] = (values[4] -10.0)/495.0 - 1.0;
        values[5] = (values[5] -10.0)/495.0 - 1.0;
        values[6] = (values[6] -10.0)/495.0 - 1.0;
        values[7] = (values[7] -10.0)/495.0 - 1.0;


    }

}
