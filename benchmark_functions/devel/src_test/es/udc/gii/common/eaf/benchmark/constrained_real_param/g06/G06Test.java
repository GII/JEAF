/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g06;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.g06.*;

/**
 *
 * @author pilar
 */
public class G06Test {

    /**
     * x1 ~ [13.0, 100.0]
     * x2 ~ [0.0, 100.0]
     * @param values
     */
    public static void normalize(double[] values) {

        values[0] = (values[0] - 56.5)/43.5;

        values[1] = (values[1]/50.0) - 1.0;


    }

}
