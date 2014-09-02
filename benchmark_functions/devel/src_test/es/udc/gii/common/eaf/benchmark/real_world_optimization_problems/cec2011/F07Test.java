/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F07Test {
    
    public F07Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F07.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F07 instance = new F07();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F07.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        //This test values were generated with the Matlab version of the benchmark set
        double[] values = {1.6215,2.5681,3.7378,1.6475,3.7878,4.4687,1.3933,0.73776,1.8641,2.0029,
            2.6651,3.191,0.53731,1.6492,5.0329,0.1836,5.8362,4.5888,3.07,3.635};
        F07 instance = new F07();
        double expResult = 5.3321;
        double result = instance.evaluate(instance.normalize(values));
        double threshold = 1.0e-3;
        assertEquals(expResult, result, threshold);
    }

    /**
     * Test of denormalize method, of class F07.
     */
//    @Test
//    public void testDenormalize() {
//        System.out.println("denormalize");
//        double value = 0.0;
//        F07 instance = new F07();
//        double expResult = 0.0;
//        double result = instance.denormalize(value);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of normalize method, of class F07.
     */
//    @Test
//    public void testNormalize() {
//        System.out.println("normalize");
//        double value = 0.0;
//        F07 instance = new F07();
//        double expResult = 0.0;
//        double result = instance.normalize(value);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of reset method, of class F07.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F07 instance = new F07();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
