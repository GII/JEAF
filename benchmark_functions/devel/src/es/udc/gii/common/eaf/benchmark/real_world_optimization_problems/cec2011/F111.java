/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import java.util.Arrays;

/**
 * F11.1 Dynamic Economic Load Dispatch (5 unit system)
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F111 extends DEDFunction {

    private double[] l_b = {10.0, 20.0, 30.0, 40.0, 50.0};
    
    private double[] u_b = {75.0, 125.0, 175.0, 250.0, 300.0};
    
    private double[] p_d = {410, 435, 475, 530, 558, 608, 626, 654, 690, 704,
            720, 740, 704, 690, 654, 580, 558, 608, 654, 704, 680, 605, 527, 463};
    private double[][] d_1 = {
            {10.0, 75.0, 0.0080, 2.0, 25.0, 100.0, 0.042},
            {20.0, 125.0, 0.0030, 1.8, 60.0, 140.0, 0.040},
            {30.0, 175.0, 0.0012, 2.1, 100.0, 160.0, 0.038},
            {40.0, 250.0, 0.0010, 2.0, 120.0, 180.0, 0.037},
            {50.0, 300.0, 0.0015, 1.8, 40.0, 200.0, 0.035}
        };
    
    private double[][] d_2 = {
            {Double.NaN, 30.0, 30.0, 10.0, 10.0, 10.0, 10.0},
            {Double.NaN, 30.0, 30.0, 20.0, 20.0, 20.0, 20.0},
            {Double.NaN, 40.0, 40.0, 30.0, 30.0, 30.0, 30.0},
            {Double.NaN, 50.0, 50.0, 40.0, 40.0, 40.0, 40.0},
            {Double.NaN, 50.0, 50.0, 50.0, 50.0, 50.0, 50.0}
        };
    private double[][] b_1 = {
            {0.000049, 0.000014, 0.000015, 0.000015, 0.000020},
            {0.000014, 0.000045, 0.000016, 0.000020, 0.000018},
            {0.000015, 0.000016, 0.000039, 0.000010, 0.000012},
            {0.000015, 0.000020, 0.000010, 0.000040, 0.000014},
            {0.000020, 0.000018, 0.000012, 0.000014, 0.000035}
        };
    
    public F111() {
        
        super();
        this.No_of_Units = 5;
        this.No_of_Load_Hours = 24;
        this.lower_bounds = l_b;
        this.upper_bounds = u_b;
        this.Power_Demand = p_d;
        this.Data1 = d_1;
        this.Data2 = d_2;
        this.B1 = b_1;
        this.power_balance_penalty_factor = 1.0e+3;
        this.capacity_limits_penalty_factor = 1.0e+3;
        this.ramp_limits_penalty_factor = 1.0e+5;
        this.poz_penalty_factor = 1.0e+5;
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        fitness = super.evaluate(values);
        
        return fitness;
    }


}
