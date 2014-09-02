/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g22;

/**
 *
 * @author pilar
 */
public class G22Test {

    /**
     * 0 ≤ x1 ≤ 20000, 0 ≤ x2 , x3 , x4 ≤ 1 × 10^6, 0 ≤ x5 , x6 , x7 ≤ 4 × 10^7
     * , 100 ≤ x8 ≤ 299.99, 100 ≤ x9 ≤ 399.99, 100.01 ≤ x10 ≤ 300, 100 ≤ x11 ≤ 400,
     * 100 ≤ x12 ≤ 600, 0 ≤ x13 , x14 , x15 ≤ 500, 0.01 ≤ x16 ≤ 300, 0.01 ≤ x17 ≤ 400,
     * −4.7 ≤ x18 , x19 , x20 , x21 , x22 ≤ 6.25.
     * @param values
     */
    public static void normalize(double[] values) {

       values[0] = (values[0]/10000.0) - 1.0;
       values[1] = (values[1]/500000.0) - 1.0;
       values[2] = (values[2]/500000.0) - 1.0;
       values[3] = (values[3]/500000.0) - 1.0;
       values[4] = (values[4]/20000000.0) - 1.0;
       values[5] = (values[5]/20000000.0) - 1.0;
       values[6] = (values[6]/20000000.0) - 1.0;
       values[7] = (values[7] - 100.0)/99.995 - 1.0;
       values[8] = (values[8] - 100.0)/149.995 - 1.0;
       values[9] = (values[9] - 100.01)/99.95 - 1.0;
       values[10] = (values[10] - 100.0)/150.0 - 1.0;
       values[11] = (values[11] - 100.0)/250.0 - 1.0;
       values[12] = (values[12]/250.0) - 1.0;
       values[13] = (values[13]/250.0) - 1.0;
       values[14] = (values[14]/250.0) - 1.0;
       values[15] = (values[15] - 0.01)/149.995 - 1.0;
       values[16] = (values[16] - 0.01)/199.995 - 1.0;
       values[17] = (values[17] + 4.7)/5.475 - 1.0;
       values[18] = (values[18] + 4.7)/5.475 - 1.0;
       values[19] = (values[19] + 4.7)/5.475 - 1.0;
       values[20] = (values[20] + 4.7)/5.475 - 1.0;
       values[21] = (values[21] + 4.7)/5.475 - 1.0;

    }

}
