/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import org.apache.commons.math.stat.StatUtils;

/**
 * Problem F07: Spread Spectrum Radar Polly phase Code Design
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F07 extends BenchmarkObjectiveFunction {

    private double u_bound = 2.0*Math.PI;
    private double l_bound = 0.0;

    @Override
    public double[][] getOptimum(int dim) {
        return null;
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double sum;
        double[] hsum;
        int m, init_j, init_k;
        
        for (int i = 0; i < values.length; i++) {
            values[i] = denormalize(values[i]);
        }
        
        m = 2*values.length - 1;
        hsum = new double[2*m];
        
        for (int i = 0; i < 2*m; i++) {
        
            if (i > m) {
                hsum[i] = -hsum[i-m];
            } else {
            
                if (i % 2 == 0) {
                
                    hsum[i] = 0;
                    init_j = i/2;
                    
                    
                } else {
                    
                    hsum[i] = 0.5;
                    init_j = (i+1)/2;
                }
                
                
                for (int j = init_j; j < values.length; j++) {
                    sum = 0;    
                    if (i % 2 == 0) {
                        init_k = Math.abs(2*init_j - j);
                    } else {
                        init_k = Math.abs(2*init_j - j - 1);
                    }
                    
                    for (int k = init_k; k <= j; k++) {
                    
                        sum += values[k];
                    
                    }
                    
                    hsum[i] += Math.cos(sum);
                    
                }
                
            }
        
        }
        
        fitness = StatUtils.max(hsum);
        
        
        return fitness;

    }
    
   
    public double denormalize(double value) {

        return (u_bound - l_bound) * (value + 1.0) / 2.0 + l_bound;

    }

    public double normalize(double value) {
        return (value - l_bound) / (u_bound - l_bound) * 2.0 - 1.0;
    }
    
    public double[] normalize(double[] values) {
    
        double[] new_values = new double[values.length];
        
        for (int i = 0; i < values.length; i++) {
            new_values[i] = normalize(values[i]);
        }
        
        return new_values;
        
    }

    @Override
    public void reset() {
        //Do nothing
    }
}
