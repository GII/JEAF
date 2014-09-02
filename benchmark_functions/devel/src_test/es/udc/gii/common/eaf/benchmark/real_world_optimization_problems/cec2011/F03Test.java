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
public class F03Test {
    
    public F03Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F03.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F03 instance = new F03();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F03.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {0.78971};
        F03 instance = new F03();
        double expResult = 0.0;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of reset method, of class F03.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F03 instance = new F03();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of computeDerivatives method, of class F03.
     */
//    @Test
//    public void testComputeDerivatives() throws Exception {
//        System.out.println("computeDerivatives");
//        double t = 0.0;
//        double[] x = null;
//        double[] xDot = null;
//        F03 instance = new F03();
//        instance.computeDerivatives(t, x, xDot);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
