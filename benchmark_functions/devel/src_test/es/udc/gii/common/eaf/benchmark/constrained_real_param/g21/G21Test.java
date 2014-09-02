/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g21;

/**
 *
 * @author pilar
 */
public class G21Test {

    /**
     * 0 <= x1 <= 1000, 0 <= x2 , x3 <= 40, 100 <= x4 <= 300, 6.3 <= x5 <= 6.7,
     * 5.9 <= x6 <= 6.4 and 4.5 ≤ x≤ 6.25
     * @param values
     */
    public static void normalize(double[] values) {

       values[0] = (values[0]/500.0) - 1.0;
       values[1] = (values[1]/20.0) - 1.0;
       values[2] = (values[2]/20.0) - 1.0;
       values[3] = ((values[3] - 100.0)/200.0) * 2.0 - 1.0;
       values[4] = ((values[4] - 6.3)/0.4)*2.0 - 1.0;
       values[5] = ((values[5] - 5.9)/0.5)*2.0 - 1.0;
       values[6] = ((values[6] - 4.5)/1.75)*2.0 - 1.0;

    }

}
