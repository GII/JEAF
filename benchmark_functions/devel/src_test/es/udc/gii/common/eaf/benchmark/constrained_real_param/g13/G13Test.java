/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g13;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.g13.*;

/**
 *
 * @author pilar
 */
public class G13Test {

    /**
     * −2.3 ≤ xi ≤ 2.3 (i = 1, 2) and −3.2 ≤ xi ≤ 3.2 (i = 3, 4, 5)
     * @param values
     */
    public static void normalize(double[] values) {

        values[0] = values[0]/2.3;
        values[1] = values[1]/2.3;
        values[2] = values[2]/3.2;
        values[3] = values[3]/3.2;
        values[4] = values[4]/3.2;

    }

}
