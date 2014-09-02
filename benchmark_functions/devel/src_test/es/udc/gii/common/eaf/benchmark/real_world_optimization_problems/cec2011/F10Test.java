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
public class F10Test {
    
    public F10Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F10.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F10 instance = new F10();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F10.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {0.4500, 0.3500, 0.1000, 0.9900, 0.2100, 0.4300,
            -0.4916, 2.6121, 1.8360, 2.8871, 0.9785, -2.9172};
        F10 instance = new F10();
        double expResult = -5.3890;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 1.0e-3);
    }

    /**
     * Test of denormalize method, of class F10.
     */
//    @Test
//    public void testDenormalize() {
//        System.out.println("denormalize");
//        double[] value = null;
//        F10 instance = new F10();
//        double[] expResult = null;
//        double[] result = instance.denormalize(value);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of normalize method, of class F10.
     */
//    @Test
//    public void testNormalize() {
//        System.out.println("normalize");
//        double[] values = null;
//        F10 instance = new F10();
//        double[] expResult = null;
//        double[] result = instance.normalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of reset method, of class F10.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F10 instance = new F10();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
