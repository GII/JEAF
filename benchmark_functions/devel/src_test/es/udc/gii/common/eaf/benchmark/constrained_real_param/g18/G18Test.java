/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g18;

/**
 *
 * @author pilar
 */
public class G18Test {

    /**
     * −10 ≤ xi ≤ 10 (i = 1, . . . , 8) and 0 ≤ x9 ≤ 20
     * @param values
     */
    public static void normalize(double[] values) {

       for (int i = 0; i < 7; i++) {
            values[i] = values[i]/10.0;
       }

       values[8] = values[8]/10.0 - 1.0;

    }

}
