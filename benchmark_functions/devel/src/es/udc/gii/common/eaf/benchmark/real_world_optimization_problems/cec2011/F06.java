/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;

/**
 * Problem F05: Tersoff Potential Function Minimization Problem Si(C)
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F06 extends BenchmarkObjectiveFunction {

    private double R1 = 2.85;
    
    private double R2 = 0.15;
    
    private double A = 1.8308e+3;

    private double B = 4.7118e+2;

    private double lemda1 = 2.4799;

    private double lemda2 = 1.7322;

    private double lemda3 = 1.7322;

    private double c = 1.0039e+05;

    private double d = 1.6218e+01;

    private double n1 = 7.8734e-01;
    
    private double gama = 1.0999e-06;
    
    private double h = -5.9826e-01;
            
    @Override
    public double[][] getOptimum(int dim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double evaluate(double[] values) {
       
        double fitness = 0.0;
        //Number of atoms
        int N;
        double[][] atoms_matrix;
        double[][] r, fcr, VRr, VAr, jeta;
        double[] E;
        double d_tmp;
        //Lennard-Jones Potential:
        double V = 0.0;
        double rd1;
        double rd2;
        double rd3;
        double ctheta_ijk;
        double G_th_ijk;
        double Bij;
        
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
            fcr = new double[N][N];
            VRr = new double[N][N];
            VAr = new double[N][N];
            E = new double[N];
            
            for (int i = 0; i < N; i++) {
            
                for (int j = 1; j < N; j++) {
                
                    d_tmp = Math.sqrt(Math.pow(atoms_matrix[i][0] - atoms_matrix[j][0], 2.0) +
                            Math.pow(atoms_matrix[i][1] - atoms_matrix[j][1], 2.0) + 
                            Math.pow(atoms_matrix[i][2] - atoms_matrix[j][2], 2.0));
                    
                    r[i][j] = d_tmp;
                    fcr[i][j] = (r[i][j] < (R1 - R2) ? 1.0 :
                            (r[i][j] > (R1 + R2) ? 0.0 : 0.5-0.5*Math.sin(Math.PI/2*(r[i][j]-R1)/R2)));
                    VRr[i][j] = A*Math.exp(-lemda1*r[i][j]);
                    VAr[i][j] = B*Math.exp(-lemda2*r[i][j]);
                    
                }
               
                
            }
            
            for (int i = 0; i < N; i++) {
            
                for (int j = 0; j < N; j++) {
                
                    if (i == j) continue;
                    
                    jeta = new double[N][N];
                    
                    for (int k = 0; k < N; k++) {
                    
                        if (i == k || j == k) continue;
                        
                        rd1 = this.distance(atoms_matrix[i], atoms_matrix[k]);
                        rd2 = this.distance(atoms_matrix[i], atoms_matrix[j]);
                        rd3 = this.distance(atoms_matrix[k], atoms_matrix[j]);
                            
                        ctheta_ijk = (rd1*rd1+rd2*rd1-rd3*rd3*rd3)/(2*rd1*rd2);
                        G_th_ijk =1+(c*c)/(d*d)-(c*c)/(d*d+(h-ctheta_ijk)*(h-ctheta_ijk));
                        jeta[i][j] += fcr[i][k]*G_th_ijk*Math.exp(lemda3*lemda3*lemda3*Math.pow(r[i][j]-r[i][k],3.0));
                        
                    }
                    
                    Bij = Math.pow(1+Math.pow(gama*jeta[i][j],n1),(-0.5/n1));
                    E[i] += fcr[i][j]*(VRr[i][j]-Bij*VAr[i][j])/2;
                }
            }
            
            for (int i = 0; i < N; i++) {
                fitness += E[i];
            }
            
            
            
            
        } else {
            System.err.println("ERROR: The chromosome length used in this function must be "
                    + "n-dimensional, where n is perfectly divided by 3.");
            System.exit(-1);
        }
        
        
        
        return fitness;
        
    }
    
    private double distance(double[] x1, double[] x2) {
    
        double distance = 0.0;
        
        for (int i = 0; i < x1.length; i++) {
            
            distance += (x1[i] - x2[i])*(x1[i] - x2[i]);
            
        }
        
        distance = Math.sqrt(distance);
        
        return distance;
    
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
