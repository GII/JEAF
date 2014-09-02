/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g23;

/**
 *
 * @author pilar
 */
public class G23Test {

    /**
     *  0 ≤ x1 , x2 , x6 ≤ 300, 0 ≤ x3 , x5 , x7 ≤ 100, 0 ≤ x4 , x8 ≤ 200 and 0.01 ≤ x9 ≤ 0.03.
     * @param values
     */
    public static void normalize(double[] values) {

       values[0] = (values[0]/150.0) - 1.0;
       values[1] = (values[1]/150.0) - 1.0;
       values[2] = (values[2]/50.0) - 1.0;
       values[3] = (values[3]/100.0) - 1.0;
       values[4] = (values[4]/50.0) - 1.0;
       values[5] = (values[5]/150.0) - 1.0;
       values[6] = (values[6]/50.0) - 1.0;
       values[7] = (values[7]/100.0) - 1.0;
       values[8] = (values[8]-0.01)/0.01 - 1.0;
       
    }

}
