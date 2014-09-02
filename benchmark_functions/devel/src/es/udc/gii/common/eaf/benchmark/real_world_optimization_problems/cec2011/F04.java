/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import java.util.Iterator;
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
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;

/**
 * Problem F03: The Bifunctional Catalyst blend optimal control problem
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F04 extends BenchmarkObjectiveFunction implements FirstOrderDifferentialEquations {

    private double u;
    private double upper_bound = 5.0;
    private double lower_bound = 0.0;
    private double fitness = 0.0;

    public int getDimension() {
        return 2;
    }

    @Override
    public double[][] getOptimum(int dim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double evaluate(double[] values) {

        double[] x = {0.09, 0.09};
        double[] xDot = new double[2];
        //RungeKuttaIntegrator rk_integrator = new EulerIntegrator(0.0086);
        AbstractIntegrator rk_integrator = new DormandPrince54Integrator(0.0, 0.72, 1.0e-1, 1.0e-1);
        u = ((values[0] + 1.0) / 2.0) * 5.0;

        StepHandler f04_step_handler = new StepHandler() {

            
            @Override
            public boolean requiresDenseOutput() {
                return true;
            }

            @Override
            public void reset() {
                //Do nothung
            }

            @Override
            public void handleStep(StepInterpolator si, boolean isLast) throws DerivativeException {
               
                //Here is where the fitness value is calculated because here we can get the values of each
                //step:
                double[] x = si.getInterpolatedState();
                
                System.out.println(si.getCurrentTime());
                fitness += (x[0] * x[0] + x[1] * x[1] + 0.1 * u * u);
                
            }
            
        };
        rk_integrator.addStepHandler(f04_step_handler);
        
        try {
            //First state:
            fitness += (x[0] * x[0] + x[1] * x[1] + 0.1 * u * u);
            rk_integrator.integrate(this, 0, x, 0.72, xDot);
        } catch (DerivativeException ex) {
            Logger.getLogger(F04.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntegratorException ex) {
            Logger.getLogger(F04.class.getName()).log(Level.SEVERE, null, ex);
        }

        //fitness = f04_step_handler.getFitnessValue();
        
        return fitness;
    }

    @Override
    public void reset() {
        //Do nothing
    }

    @Override
    public void computeDerivatives(double t, double[] x, double[] xDot) throws DerivativeException {

        xDot[0] = -(2.0 + u) * (x[0] + 0.25) + (x[1] + 0.5) * Math.exp((25.0 * x[0]) / (x[0] + 2.0));
        xDot[1] = 0.5 - x[1] - (x[1] + 0.5) * Math.exp((25.0 * x[0]) / (x[0] + 2.0));

    }

    public double[] normalize(double[] value) {
        double[] new_value;

        new_value = new double[value.length];

        new_value[0] = (value[0] - lower_bound) / (upper_bound - lower_bound) * 2.0 - 1.0;

        return new_value;
    }
}
