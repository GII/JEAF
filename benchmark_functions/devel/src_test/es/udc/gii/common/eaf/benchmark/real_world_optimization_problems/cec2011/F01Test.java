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
public class F01Test {
    
    public F01Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F01.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F01 instance = new F01();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F01.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {0.91486,4.0436,-5.1372,-6.0918,0.80441,4.9832};
        F01 instance = new F01();
        double expResult = 93.426;
        double threshold = 1.0e-3;
        
        for (int i = 0; i<values.length; i++) {
            values[i] = instance.normalize(values[i]);
        }
        
        double result = instance.evaluate(values);
        assertEquals(expResult, result, threshold);
    }

    /**
     * Test of denormalize method, of class F01.
     */
//    @Test
//    public void testDenormalize() {
//        System.out.println("denormalize");
//        double value = 0.0;
//        F01 instance = new F01();
//        double expResult = 0.0;
//        double result = instance.denormalize(value);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of normalize method, of class F01.
     */
//    @Test
//    public void testNormalize() {
//        System.out.println("normalize");
//        double value = 0.0;
//        F01 instance = new F01();
//        double expResult = 0.0;
//        double result = instance.normalize(value);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of reset method, of class F01.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F01 instance = new F01();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
