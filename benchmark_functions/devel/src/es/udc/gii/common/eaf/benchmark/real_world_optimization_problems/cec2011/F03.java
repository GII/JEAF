/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.DormandPrince54Integrator;
import org.apache.commons.math.ode.nonstiff.EulerIntegrator;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;

/**
 * Problem F03: The Bifunctional Catalyst blend optimal control problem
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F03 extends BenchmarkObjectiveFunction implements FirstOrderDifferentialEquations {

    private double u;
    
    private double upper_bound = 0.9;
    private double lower_bound = 0.6;
    
    private double[][] c  = {
        {2.918487e-3, -8.045787e-3, 6.749947e-3, -1.416647e-3},
        {9.509977e+0, -3.500994e+1, 4.283329e+1, -1.733333e+1},
        {2.682093e+1, -9.556079e+1, 1.130398e+2, -4.429997e+1},
        {2.087241e+2, -7.198052e+2, 8.277466e+2, -3.166655e+2},
        {1.350005e+0, -6.850027e+0, 1.216671e+1, -6.666689e+0},
        {1.921995e-2, -7.945320e-2, 1.105660e-1, -5.033333e-2},
        {1.323596e-1, -4.692550e-1, 5.539323e-1, -2.166664e-1},
        {7.339981e+0, -2.527328e+1, 2.993329e+1, -1.199999e+1},
        {-3.950534e-1, 1.679353e+0, -1.777829e+0, 4.974987e-1},
        {-2.504665e-5, 1.005854e-2, -1.986696e-2, 9.833470e-3}
    };
    
    @Override
    public double[][] getOptimum(int dim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double evaluate(double[] values) {
        
        double fitness = Double.MAX_VALUE;
        double[] x0 = {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        double[] x = new double[x0.length];
        //RungeKuttaIntegrator rk_integrator = new EulerIntegrator(1.0);
        AbstractIntegrator rk_integrator = new DormandPrince54Integrator(0.0, 1.0, 1.0e-3, 1.0e-3);
        u = (upper_bound - lower_bound) * (values[0] + 1.0) / 2.0 + lower_bound;
        try {
            rk_integrator.integrate(this, 0.0, x0, 2000.0, x);
        } catch (DerivativeException ex) {
            Logger.getLogger(F03.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntegratorException ex) {
            Logger.getLogger(F03.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        fitness = x[x.length-1]*1.0e+3;
        
        if (Double.isNaN(fitness)) {
            fitness = 0.0;
        }
        
        return -fitness;
    }

    @Override
    public void reset() {
        //Do nothing
    }
    
    public double[] normalize(double[] value) {
        double[] new_value;
        
        new_value = new double[value.length];
        
        new_value[0] = (value[0] - lower_bound) / (upper_bound - lower_bound) * 2.0 - 1.0;
        
        return new_value;
    }

    @Override
    /**
     * t - current value of the independent time variable
     * y - array containing the current value of the state vector
     * yDot - placeholder array where to put the time derivative of the state vector
     */
    public void computeDerivatives(double t, double[] x, double[] xDot) throws DerivativeException {
        
        double[] k = new double[10];
        
        //Calculate k values:
        for (int i = 0; i < k.length; i++) {
        
            k[i] = c[i][0] + c[i][1]*u + c[i][2]*u*u + c[i][3]*u*u*u;
        }
        
        xDot[0] = -k[0]*x[0];
        xDot[1] = k[0]*x[0] - (k[1] + k[2])*x[1] + k[3]*x[4];
        xDot[2] = k[1]*x[1];
        xDot[3] = -k[5]*x[3] + k[4]*x[4];
        xDot[4] = -k[2]*x[1] + k[5]*x[3] - (k[3] + k[4] + k[7] + k[8])*x[4] + k[6]*x[5] + k[9]*x[6];
        xDot[5] = k[7]*x[4] - k[6]*x[5];
        xDot[6] = k[8]*x[4] - k[9]*x[6];
        
        
    }

    public int getDimension() {
        return 7;
    }

    
}
