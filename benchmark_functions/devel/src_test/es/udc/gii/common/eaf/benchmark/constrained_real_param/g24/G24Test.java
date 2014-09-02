/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g24;

/**
 *
 * @author pilar
 */
public class G24Test {

    /**
     *  0 ≤ x≤ 3 and 0 ≤ x≤ 4
     * @param values
     */
    public static void normalize(double[] values) {

       values[0] = (values[0] + 1.0)*1.5;
       values[1] = (values[1] + 1.0)*2.0;
       
    }

}
