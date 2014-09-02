/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;

/**
 * Problem F01: Parameter Estimation for Frequency-Modulated (FM) Sound Waves.
 * 
 * Frequency-Modulated (FM) sound wave synthsis has an important role in several modern music systems
 * and to optimize the parameter of a FM synthesizer is a six dimensional optimization problem where
 * the vector to be optimized is X = {a1, w1, a2, w2, a3, w3} of the sound wave difen in eqn (1). The 
 * problem is to generate a sound (1) simular to target sound (2). This problem is a highly complex
 * multimodal one having strong epistasis, with minimum value f(x_sol) = 0.0.
 * 
 * Eqn (1): y(t) = a1*sin(w1*t*theta + a2*sin(w2*t*theta + a3*sin(w3*t*theta)))
 * 
 * where theta = 2*PI/100 and the parameters are defined in the range [-6.4, 6.35].
 * The fitness function is the summation of the square errors between the estimated wave and the wave
 * defined by X = {1.0, 5.0. -1.5, 4.8, 2.0, 4.9}.
 * 
 * f(x) = sum(t = 0; 100)(y(t) - y_opt(t))^2
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F01 extends BenchmarkObjectiveFunction {

    private double u_bound = 6.35;
    private double l_bound = -6.4;

    @Override
    public double[][] getOptimum(int dim) {


        double[] opt = {1.0, 5.0, -1.5, 4.8, 2.0, 4.9};
        double[][] optimum = new double[1][];

        for (int i = 0; i < opt.length; i++) {
            opt[i] = normalize(opt[i]);
        }

        optimum[0] = opt;

        return optimum;
    }

    @Override
    public double evaluate(double[] values) {

        double a1, w1, a2, w2, a3, w3;
        double fitness;
        double[] opt; 
        double theta = 2.0*Math.PI/100.0;
         
        //With 4 decimal as in Matlab
        a1 = denormalize(values[0]);
        w1 = denormalize(values[1]);
//        a2 = 0.0;
//        w2 = 0.0;
//        a3 = 0.0;
//        w3 = 0.0;
        a2 = denormalize(values[2]);
        w2 = denormalize(values[3]);
        a3 = denormalize(values[4]);
        w3 = denormalize(values[5]);

        opt = this.getOptimum(6)[0];
        
        for (int i = 0; i < opt.length; i++) {
            opt[i] = denormalize(opt[i]);
        }
        
        fitness = 0.0;
        
        for (double t = 0.0; t <= 50; t++) {
        
            fitness += Math.pow(
                    y(a1, w1, a2, w2, a3, w3, t, theta) - 
                    y(opt[0], opt[1], opt[2], opt[3], opt[4], opt[5], t, theta),2.0);
            
        }
        
        return fitness;

    }
    
    private double y(double a1, double w1, double a2, double w2, double a3, double w3, double t, double theta) {
    
        double y_val = 0.0;
        
        y_val  = a1*Math.sin(w1*t*theta + a2*Math.sin(w2*t*theta + a3*Math.sin(w3*t*theta)));
        
        return y_val;
    
    }

    public double denormalize(double value) {

        return (u_bound - l_bound) * (value + 1.0) / 2.0 + l_bound;

    }

    public double normalize(double value) {
        return (value - l_bound) / (u_bound - l_bound) * 2.0 - 1.0;
    }

    @Override
    public void reset() {
        //Do nothing
    }
}
