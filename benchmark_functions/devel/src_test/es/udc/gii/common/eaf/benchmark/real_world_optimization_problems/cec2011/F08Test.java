/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F08Test {
    
    public F08Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F08.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F08 instance = new F08();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F08.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {13.6993,11.9428,1.4807,3.9281,5.0304,10.1959,2.0483};
        F08 instance = new F08();
        double expResult = 1.2516e+4;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 1.0e-3);
    }

    /**
     * Test of denormalize method, of class F08.
     */
//    @Test
//    public void testDenormalize_double() {
//        System.out.println("denormalize");
//        double value = 0.0;
//        F08 instance = new F08();
//        double expResult = 0.0;
//        double result = instance.denormalize(value);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of normalize method, of class F08.
     */
//    @Test
//    public void testNormalize_double() {
//        System.out.println("normalize");
//        double value = 0.0;
//        F08 instance = new F08();
//        double expResult = 0.0;
//        double result = instance.normalize(value);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of normalize method, of class F08.
     */
//    @Test
//    public void testNormalize_doubleArr() {
//        System.out.println("normalize");
//        double[] values = null;
//        F08 instance = new F08();
//        double[] expResult = null;
//        double[] result = instance.normalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of denormalize method, of class F08.
     */
//    @Test
//    public void testDenormalize_doubleArr() {
//        System.out.println("denormalize");
//        double[] values = null;
//        F08 instance = new F08();
//        double[] expResult = null;
//        double[] result = instance.denormalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of reset method, of class F08.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F08 instance = new F08();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
