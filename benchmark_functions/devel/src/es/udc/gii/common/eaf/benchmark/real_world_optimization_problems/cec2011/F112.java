/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import java.util.Arrays;

/**
 * F11.1 Dynamic Economic Load Dispatch (10 unit system)
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F112 extends DEDFunction {

    private double[] l_b = {150.0, 135.0, 73.0, 60.0, 73.0, 57.0, 20.0, 47.0, 20.0, 55.0};
    private double[] u_b = {470.0, 460.0, 340.0, 300.0, 243.0, 160.0, 130.0, 120.0, 80.0, 55.0};
    private double[] p_d = {1036.0, 1110.0, 1258.0, 1406.0, 1480.0, 1628.0, 1702.0, 1776.0,
        1924.0, 2072.0, 2146.0, 2220.0, 2072.0, 1924.0, 1776.0, 1554.0, 1480.0, 1628.0, 1776.0,
        2072.0, 1924.0, 1628.0, 1332.0, 1184.0};
    private double[][] d_1 = {
        {150, 470, 0.00043, 21.60, 958.20, 450, 0.041},
        {135, 460, 0.00063, 21.05, 1313.6, 600, 0.036},
        {73, 340, 0.00039, 20.81, 604.97, 320, 0.028},
        {60, 300, 0.00070, 23.90, 471.60, 260, 0.052},
        {73, 243, 0.00079, 21.62, 480.29, 280, 0.063},
        {57, 160, 0.00056, 17.87, 601.75, 310, 0.048},
        {20, 130, 0.00211, 16.51, 502.7, 300, 0.086},
        {47, 120, 0.0048, 23.23, 639.40, 340, 0.082},
        {20, 80, 0.10908, 19.58, 455.60, 270, 0.098},
        {55, 55, 0.00951, 22.54, 692.4, 380, 0.094}
    };
    private double[][] d_2 = {
        {Double.NaN, 80.0, 80.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 80.0, 80.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 80.0, 80.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 50.0, 50.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 50.0, 50.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 50.0, 50.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 30.0, 30.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 30.0, 30.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 30.0, 30.0, 0.0, 0.0, 0.0, 0.0},
        {Double.NaN, 30.0, 30.0, 0.0, 0.0, 0.0, 0.0}
    };
    

    public F112() {

        super();
        this.No_of_Units = 10;
        this.No_of_Load_Hours = 24;
        this.lower_bounds = l_b;
        this.upper_bounds = u_b;
        this.Power_Demand = p_d;
        this.Data1 = d_1;
        this.Data2 = d_2;
        this.B1 = new double[No_of_Units][No_of_Units];
        this.power_balance_penalty_factor = 1.0e+3;
        this.capacity_limits_penalty_factor = 1.0e+3;
        this.ramp_limits_penalty_factor = 1.0e+3;
        this.poz_penalty_factor = 1.0e+5;

    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        fitness = super.evaluate(values);

        return fitness;
    }
}
