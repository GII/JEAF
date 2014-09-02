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
public class F05Test {
    
    public F05Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F05.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F05 instance = new F05();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F05.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {2.0406,3.6255,1.9758,-3.1877,-0.87316,-3.5631,0.010905,-0.58037,4.2293,2.8044,-0.12913,3.55,-3.4433,-1.045,4.0599,4.1749,2.1357,1.1834,-1.6455,4.5783,-3.9399,2.5364,1.6113,3.6647,-1.1698,2.873,3.855,-2.1305,0.62714,5.7495}
;

        F05 instance = new F05();
        double expResult = 5.968898506506635;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 1.0e-3);
    }

    /**
     * Test of denormalize method, of class F05.
     */
//    @Test
//    public void testDenormalize() {
//        System.out.println("denormalize");
//        double[] values = null;
//        F05 instance = new F05();
//        double[] expResult = null;
//        double[] result = instance.denormalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of normalize method, of class F05.
     */
//    @Test
//    public void testNormalize() {
//        System.out.println("normalize");
//        double[] values = null;
//        F05 instance = new F05();
//        double[] expResult = null;
//        double[] result = instance.normalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of reset method, of class F05.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F05 instance = new F05();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
