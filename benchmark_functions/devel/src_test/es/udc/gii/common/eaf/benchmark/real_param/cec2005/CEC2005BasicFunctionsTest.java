/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_param.cec2005;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rafa
 */
public class CEC2005BasicFunctionsTest extends CEC2005ObjectiveFunction {

    private final double[] vector;
    private double lB = -2;
    private double uB = 10;
    private static final double epsilon = 1e-15;

    public CEC2005BasicFunctionsTest() {
        vector = new double[10];
        Arrays.fill(vector, 0.0);
    }

    private double error(double a, double b) {
        return Math.abs(a - b);
    }

    @Test
    public void testSphere() {
        System.out.println("sphere");
        double expResult = 0.0;
        double result = sphere(vector);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testRastrigins() {
        System.out.println("rastrigins");
        double expResult = 0.0;
        double result = rastrigins(vector);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testWeierstrass() {
        System.out.println("weiertrass");
        double expResult = 0.0;
        double result = weierstrass(vector, F11.a, F11.b, F11.kMax);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testGriewank() {
        System.out.println("griewank");
        double expResult = 0.0;
        double result = griewank(vector);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testAckley() {
        System.out.println("ackley");
        double expResult = 0.0;
        double result = ackley(vector);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testF6() {
        System.out.println("F6");
        double expResult = 0.0;
        double result = F6(vector);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testF8F2() {
        System.out.println("F8F2");
        double[] v = new double[vector.length];
        Arrays.fill(v, 1.0);
        double expResult = 0.0;
        double result = F8F2(v);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testElliptic() {
        System.out.println("elliptic");
        double expResult = 0.0;
        double result = elliptic(vector);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testSchwefel_1_2() {
        System.out.println("schwefel_1_2");
        double expResult = 0.0;
        double result = schwefel_1_2(vector);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testRosenbrock() {
        System.out.println("rosenbrock");
        double[] v = new double[vector.length];
        Arrays.fill(v, 1.0);
        double expResult = 0.0;
        double result = rosenbrock(v);
        assertTrue(error(expResult, result) <= epsilon);
    }

    @Test
    public void testDenormalize() {
        System.out.println("denormalize");
        lB = -5;
        uB = 5;
        assertEquals(denormalize(0.0), 0.0);
        assertEquals(denormalize(-1.0), -5.0);
        assertEquals(denormalize(1.0), 5.0);
        lB = 2;
        uB = 5;
        assertEquals(denormalize(0.0), 3.5);
        assertEquals(denormalize(-1.0), 2.0);
        assertEquals(denormalize(1.0), 5.0);
        lB = -5;
        uB = -2;
        assertEquals(denormalize(0.0), -3.5);
        assertEquals(denormalize(-1.0), -5.0);
        assertEquals(denormalize(1.0), -2.0);
    }

    @Override
    protected void initData(int dimension) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumber() {
        return 1;
    }

    @Override
    public double lowerBound() {
        return lB;
    }

    @Override
    public double upperBound() {
        return uB;
    }

    @Override
    public double[][] getOptimum(int dim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double doEvaluate(double[] values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
