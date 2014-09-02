/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g16;

/**
 *
 * @author pilar
 */
public class G16Test {

    /**
     * 704.4148 ≤ x1 <= 906.3855, 68.6 <= x2 <= 288.88, 0 <= x3 <= 134.75,
     * 193 <= x4 ≤ 287.0966 and 25 ≤ x5 ≤ 84.1988.
     * @param values
     */
    public static void normalize(double[] values) {


        values[0] = (values[0]/100.98935) - 1.0;
        values[1] = (values[1]/110.14) - 1.0;
        values[2] = (values[2]/67.375) - 1.0;
        values[3] = (values[3]/47.0486) - 1.0;
        values[4] = (values[4]/29.5994) - 1.0;


    }

}
