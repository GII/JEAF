/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;

/**
 * Problem F02: Lennard-Jones Potential Problem
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F02 extends BenchmarkObjectiveFunction {

    @Override
    public double[][] getOptimum(int dim) {
        return null;
    }

    @Override
    public double evaluate(double[] values) {
       
        double fitness = 0.0;
        //Number of atoms
        int N;
        double[][] atoms_matrix;
        double[][] r;
        double d_tmp;
        //Lennard-Jones Potential:
        double V = 0.0;
        
        //The first step is to check that the chromosome length is perfectly divided by 3:
        if (values.length % 3 == 0) {
        
            //Denormalize the values between the propper bounds:
            values = denormalize(values);
            N = values.length/3;
            //Second step: create the atoms matrix -> reshape in matlab.
            atoms_matrix = new double[N][3];
            for (int i = 0; i < values.length; i = i+3) {
            
                atoms_matrix[i/3][0] = values[i];
                atoms_matrix[i/3][1] = values[i+1];
                atoms_matrix[i/3][2] = values[i+2];
                
            }
            //Third step: calculate the distances between atoms:
            r = new double[N][N];
            for (int i = 0; i < N-1; i++) {
            
                for (int j = i+1; j < N; j++) {
                
                    d_tmp = Math.sqrt(Math.pow(atoms_matrix[i][0] - atoms_matrix[j][0], 2.0) +
                            Math.pow(atoms_matrix[i][1] - atoms_matrix[j][1], 2.0) + 
                            Math.pow(atoms_matrix[i][2] - atoms_matrix[j][2], 2.0));
                    
                    r[i][j] = d_tmp;
                     //Fourth step: calculate the Lennard-Jones potential:
                    V += 1.0/Math.pow(r[i][j], 12.0) - 2.0/Math.pow(r[i][j], 6.0); 
                }
               
                
            }
            
            fitness = V;
            
            
            
        } else {
            System.err.println("ERROR: The chromosome length used in this function must be "
                    + "n-dimensional, where n is perfectly divided by 3.");
            System.exit(-1);
        }
        
        
        
        return fitness;
        
    }

    /**
     * Method used to denormalize the values of the genes. This class recieve an array of doubles 
     * bounded in [-1,1] and it should be denormalize to calculate the fitness values.
     * The new values are bounded as follows:
     * x1 in [0, 4].
     * x2 in [0, 4].
     * x3 in [0, pi]
     * xi in [-4 - 1/4*floor(((i-4)/3), 4 + 1/4*floor((i-4)/3)]
     */
    public double[] denormalize(double[] values) {
        
        double[] new_values;
        double upper_bound, lower_bound;
        
        new_values = new double[values.length];
        
        new_values[0] = (values[0] + 1.0)*2.0;
        new_values[1] = (values[1] + 1.0)*2.0;
        new_values[2] = (values[2] + 1.0)*(Math.PI/2.0);
        
        for (int i = 3; i < values.length; i++) {
        
            lower_bound = -4.0 - (1.0/4.0)*Math.floor(((i+1)-4.0)/3.0);
            upper_bound = 4.0 + (1.0/4.0)*Math.floor(((i+1)-4.0)/3.0);
            
            new_values[i] = (values[i] - lower_bound) / (upper_bound - lower_bound) * 2.0 - 1.0;
            
        }
        
        return new_values;
    
    }
    
    public double[] normalize(double[] values) {
    
        double[] new_values;
        double upper_bound, lower_bound;
        
        new_values = new double[values.length];
        
        new_values[0] = values[0]/2.0 - 1.0;
        new_values[1] = values[1]/2.0 - 1.0;
        new_values[2] = values[2]/(Math.PI/2.0) - 1.0;
                
        for (int i = 3; i < values.length; i++) {
        
            lower_bound = -4.0 - (1.0/4.0)*Math.floor(((i+1)-4.0)/3.0);
            upper_bound = 4.0 + (1.0/4.0)*Math.floor(((i+1)-4.0)/3.0);
            
            new_values[i] = ((values[i]  + 1.0)/2.0)*(upper_bound - lower_bound) + lower_bound;
            
        }
        
        return new_values;
    
    }
    
    @Override
    public void reset() {
        //Do not nothing
    }

    
}
