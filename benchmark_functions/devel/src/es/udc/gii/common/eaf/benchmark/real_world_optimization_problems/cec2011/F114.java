/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

/**
 * F11.3 - Economic Load Dispatch (13 Units).
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F114 extends ELDFunction {

    public F114() {
        super();
        this.setNo_of_Units(13);

        double[] l_b = {0.0, 0.0, 0.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 40.0, 40.0, 55.0, 55.0};
        this.setLower_bounds(l_b);

        double[] u_b = {680.0, 360.0, 360.0, 180.0, 180.0, 180.0, 180.0, 180.0, 180.0, 120.0, 120.0, 120.0, 120.0};
        this.setUpper_bounds(u_b);

        this.setPower_Demand(1800.0);

        double[][] d_1 = {
            {0, 680, 0.00028, 8.1, 550, 300, 0.035},
            {0, 360, 0.00056, 8.1, 309, 200, 0.042},
            {0, 360, 0.00056, 8.1, 307, 200, 0.042},
            {60, 180, 0.00324, 7.74, 240, 150, 0.063},
            {60, 180, 0.00324, 7.74, 240, 150, 0.063},
            {60, 180, 0.00324, 7.74, 240, 150, 0.063},
            {60, 180, 0.00324, 7.74, 240, 150, 0.063},
            {60, 180, 0.00324, 7.74, 240, 150, 0.063},
            {60, 180, 0.00324, 7.74, 240, 150, 0.063},
            {40, 120, 0.00284, 8.6, 126, 100, 0.084},
            {40, 120, 0.00284, 8.6, 126, 100, 0.084},
            {55, 120, 0.00284, 8.6, 126, 100, 0.084},
            {55, 120, 0.00284, 8.6, 126, 100, 0.084}
        };
        this.setData_1(d_1);
        this.setData_2(null);

        double[][] b_1 = new double[13][13];
        this.setB1(b_1);

        double[] b_2 = new double[13];
        this.setB2(b_2);

        this.setB3(0.0);

        this.setPower_balance_penalty_factor(1.0e+5);
        this.setCapacity_limit_penalty_factor(1.0e+3);
        this.setRamp_limit_penalty_factor(0.0);
        this.setPoz_penalty_factor(0.0);

    }
}
