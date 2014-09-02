/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

/**
 * F11.3 - Economic Load Dispatch (6 Units).
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F113 extends ELDFunction {

    public F113() {
        super();
        this.setNo_of_Units(6);

        double[] l_b = {100.0, 50.0, 80.0, 50.0, 50.0, 50.0};
        this.setLower_bounds(l_b);

        double[] u_b = {500.0, 200.0, 300.0, 150.0, 200.0, 120.0};
        this.setUpper_bounds(u_b);

        this.setPower_Demand(1263.0);

        double[][] d_1 = {
            {100, 500, 0.0070, 7.0, 240},
            {50, 200, 0.0095, 10.0, 200},
            {80, 300, 0.0090, 8.5, 220},
            {50, 150, 0.0090, 11.0, 200},
            {50, 200, 0.0080, 10.5, 220},
            {50, 120, 0.0075, 12.0, 190}
        };
        this.setData_1(d_1);

        double[][] d_2 = {
            {440, 80, 120, 210, 240, 350, 380},
            {170, 50, 90, 90, 110, 140, 160},
            {200, 65, 100, 150, 170, 210, 240},
            {150, 50, 90, 80, 90, 110, 120},
            {190, 50, 90, 90, 110, 140, 150},
            {150, 50, 90, 75, 85, 100, 105}
        };
        this.setData_2(d_2);

        double[][] b_1 = {
            {1.7 * 1e-5, 1.2 * 1e-5, 0.7 * 1e-5, -0.1 * 1e-5, -0.5 * 1e-5, -0.2 * 1e-5},
            {1.2 * 1e-5, 1.4 * 1e-5, 0.9 * 1e-5, 0.1 * 1e-5, -0.6 * 1e-5, -0.1 * 1e-5},
            {0.7 * 1e-5, 0.9 * 1e-5, 3.1 * 1e-5, 0.0 * 1e-5, -1.0 * 1e-5, -0.6 * 1e-5},
            {-0.1 * 1e-5, 0.1 * 1e-5, 0.0 * 1e-5, 0.24 * 1e-5, -0.6 * 1e-5, -0.8 * 1e-5},
            {-0.5 * 1e-5, -0.6 * 1e-5, -0.1 * 1e-5, -0.6 * 1e-5, 12.9 * 1e-5, -0.2 * 1e-5},
            {0.2 * 1e-5, -0.1 * 1e-5, -0.6 * 1e-5, -0.8 * 1e-5, -0.2 * 1e-5, 15.0 * 1e-5}
        };
        this.setB1(b_1);

        double[] b_2 = {-0.3908 * 1e-5, -0.1297 * 1e-5, 0.7047 * 1e-5, 0.0591 * 1e-5, 0.2161 * 1e-5, -0.6635 * 1e-5,};
        this.setB2(b_2);

        this.setB3(0.0056 * 1e-2);

        this.setPower_balance_penalty_factor(1.0e+3);
        this.setCapacity_limit_penalty_factor(1.0e+3);
        this.setRamp_limit_penalty_factor(1.0e+5);
        this.setPoz_penalty_factor(1.0e+5);

    }
}
