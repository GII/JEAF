/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g08;

/**
 *
 * @author pilar
 */
public class G08Test {

    /**
     * xi ~ [-10, 10]
     * @param values
     */
    public static void normalize(double[] values) {

        for (int i = 0; i<values.length; i++) {
            values[i] = (values[i]/5.0 - 1.0);
        }

    }

}
