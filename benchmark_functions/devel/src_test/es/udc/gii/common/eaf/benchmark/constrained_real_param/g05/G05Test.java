/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g05;

/**
 *
 * @author pilar
 */
public class G05Test {

    /**
     * x1, x2 ~ [0.0, 1200.0]
     * x3, x4 ~ [-0.55, 0.55]
     * @param values
     */
    public static void normalize(double[] values) {

        values[0] = (values[0]/1200.0) - 1.0;
        values[1] = (values[1]/1200.0) - 1.0;
        values[2] = values[2]/0.55;
        values[3] = values[3]/0.55;


    }

}
