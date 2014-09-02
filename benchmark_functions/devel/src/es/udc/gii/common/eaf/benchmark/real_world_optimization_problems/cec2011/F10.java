/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Problem F10: Circular Antenna Array Design
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F10 extends BenchmarkObjectiveFunction {

    private double[] null_angles = {Math.toRadians(50.0), Math.toRadians(120.0)};
    private double phi_desired = 180.0;
    private double distance = 0.5;

    @Override
    public double[][] getOptimum(int dim) {
        return null;
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        values = denormalize(values);

        int dim = values.length;
        int num_null = null_angles.length;
        int num1 = 300;

        double[] phi = new double[300];

        phi[0] = 0.0;
        for (int i = 1; i < phi.length; i++) {

            phi[i] = phi[i - 1] + (360.0 / 299.0);

        }

        double phizero = 0.0;

        double[] yax = new double[num1];
        yax[0] = array_factorcirc(values, (Math.PI / 180.0) * phi[0], phi_desired, distance, dim);

        double maxi = yax[0];
        int phi_ref = 1;

        //This loop finds out the maximum gain:
        for (int i = 1; i < num1; i++) {

            yax[i] = array_factorcirc(values, (Math.PI / 180.0) * phi[i], phi_desired, distance, dim);
            if (maxi < yax[i]) {
                maxi = yax[i];
                phizero = phi[i];
                phi_ref = i;
            }

        }

        double maxtem = 0.0;
        List<Double> sidelobes = new ArrayList<Double>();
        List<Double> sllphi = new ArrayList<Double>();

        if (yax[0] > yax[num1 - 1] && yax[0] > yax[1]) {
            sidelobes.add(yax[0]);
            sllphi.add(yax[0]);
        }

        if (yax[num1 - 1] > yax[0] && yax[num1 - 1] > yax[num1 - 2]) {
            sidelobes.add(yax[num1 - 1]);
            sllphi.add(yax[num1 - 1]);
        }

        for (int i = 1; i < num1 - 2; i++) {
            if (yax[i] > yax[i + 1] && yax[i] > yax[i - 1]) {
                sidelobes.add(yax[i]);
                sllphi.add(yax[i]);
            }
        }

        //Ordena la lista de forma descendente:
        Collections.sort(sidelobes, Collections.reverseOrder());

        double upper_bound = 180.0;
        double lower_bound = 180.0;

        fitness = sidelobes.get(1) / maxi;

        double sllreturn = 20.0 * Math.log10(fitness);
        double tem;

        for (int i = 0; i < num1 / 2; i++) {

            if ((phi_ref + i) > (num1 - 1)) {
                upper_bound = 180.0;
                break;
            }
            tem = yax[phi_ref + i];
            if (yax[phi_ref + i] < yax[phi_ref + i - 1] && yax[phi_ref + i] < yax[phi_ref + i + 1]) {
                upper_bound = phi[phi_ref + 1] - phi[phi_ref];
                break;
            }

        }

        for (int i = 0; i < num1 / 2; i++) {

            if ((phi_ref + i) > (num1 - 1)) {
                lower_bound = 180.0;
                break;
            }
            tem = yax[phi_ref - i];
            if (yax[phi_ref - i] < yax[phi_ref + i - 1] && yax[phi_ref - i] < yax[phi_ref + i + 1]) {
                lower_bound = phi[phi_ref] - phi[phi_ref - 1];
                break;
            }

        }

        double bwfn = upper_bound - lower_bound;
        double y1 = 0.0;
        //The objective function for null control is calculated here
        for (int i = 0; i < num_null; i++) {
            y1 = y1 + (array_factorcirc(values, null_angles[i], phi_desired, distance, dim) / maxi);
        }

        double y2 = 0.0;
        double uavg = trapezoidalcirc(values, 0, 2 * Math.PI, 50, phi_desired, distance, dim);
        y2 = Math.abs((2.0 * Math.PI * maxi * maxi) / uavg);
        double directivity = 10.0 * Math.log10(y2);
        double y3 = Math.abs(phizero - phi_desired);
        if (y3 < 5) {
            y3 = 0;
        }

        fitness = 0.0;
        if (bwfn > 80.0) {
            fitness = Math.abs(bwfn - 80.0);
        }

        fitness += sllreturn + y1 + y3;
        return fitness;

    }

    private double trapezoidalcirc(double[] x, double upper, double lower, int N1, double phi_desired, double distance, int dim) {
        double q = 0.0;
        
        double h = (upper - lower)/N1;
        double x1 = lower;
        
        double[] y = new double[N1+1];
        y[0] = Math.abs(Math.pow(this.array_factorcirc(x, lower, phi_desired, distance, dim), 2.0)*Math.sin(lower - Math.PI/2.0));
        
        for (int i = 1; i <= N1; i++) {
        
            x1 = x1 + h;
            y[i] = Math.abs(Math.pow(this.array_factorcirc(x, x1, phi_desired, distance, dim), 2.0)*Math.sin(lower - Math.PI/2.0));
            
        }
        
        double s = 0.0;
        
        for (int i = 0; i <=N1; i++) {
            if (i == 0 || i == N1) {
                s += y[i];
            } else {
                s += 2.0*y[i];
            }
        }
        
        q = (h/2.0)*s;
        
        return q;
    }

    private double array_factorcirc(double[] x, double phi, double phi_desired, double distance, int dim) {

        double y = 0.0;
        double y1 = 0.0;
        double delphi, shi;

        for (int i = 0; i < dim / 2; i++) {
            delphi = (2 * Math.PI * i) / dim;
            shi = Math.cos(phi - delphi) - Math.cos(phi_desired * (Math.PI / 180.0) - delphi);
            shi = shi * dim * distance;
            y += x[i] * Math.cos(shi + x[(dim / 2) + i] * (Math.PI / 180.0));
        }

        for (int i = dim / 2; i < dim; i++) {
            delphi = (2 * Math.PI * i) / dim;
            shi = Math.cos(phi - delphi) - Math.cos(phi_desired * (Math.PI / 180.0) - delphi);
            shi = shi * dim * distance;
            y += x[i - dim / 2] * Math.cos(shi - x[i] * (Math.PI / 180.0));
        }

        for (int i = 0; i < dim / 2; i++) {
            delphi = (2 * Math.PI * i) / dim;
            shi = Math.cos(phi - delphi) - Math.cos(phi_desired * (Math.PI / 180.0) - delphi);
            shi = shi * dim * distance;
            y1 += x[i] * Math.sin(shi + x[dim / 2 + i] * (Math.PI / 180.0));
        }
        for (int i = dim / 2; i < dim; i++) {
            delphi = (2 * Math.PI * i) / dim;
            shi = Math.cos(phi - delphi) - Math.cos(phi_desired * (Math.PI / 180.0) - delphi);
            shi = shi * dim * distance;
            y1 += x[i - dim / 2] * Math.sin(shi - x[i] * (Math.PI / 180.0));
        }

        y = y*y + y1*y1;
        
        y = Math.sqrt(y);
        
        return y;

    }

    public double[] denormalize(double[] value) {

        //First N/2 from [-1:1] to [0.2,1.0]
        //The last N/2 from [-1:1] to [-pi, pi]

        double[] new_values = new double[value.length];

        for (int i = 0; i < value.length; i++) {

            if (i < value.length / 2) {
                new_values[i] = 0.8 * (value[i] + 1.0) / 2.0 + 0.2;
            } else {
                new_values[i] = value[i] * Math.PI;
            }

        }


        return new_values;

    }

    public double[] normalize(double[] values) {

        double[] new_values = new double[values.length];

        for (int i = 0; i < values.length; i++) {

            if (i < values.length / 2) {
                new_values[i] = 2.0 * (values[i] - 0.2) / 0.8 - 1.0;
            } else {
                new_values[i] = values[i] / Math.PI;
            }
        }

        return new_values;

    }

    @Override
    public void reset() {
        //Do nothing
    }
}
