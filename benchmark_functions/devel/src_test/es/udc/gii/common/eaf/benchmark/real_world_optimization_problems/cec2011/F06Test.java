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
public class F06Test {
    
    public F06Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F06.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F06 instance = new F06();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F06.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {1.9472,1.7434,1.4036,1.5492,-0.068069,-0.086173,-2.6998,-2.5061,-1.2267,1.0925,-2.8042,-0.29543,1.4181,-4.1705,-3.5715,-0.50156,-1.2248,-0.87045,3.0687,2.0869,0.3053,2.9646,-3.7874,3.3576,3.1519,3.7869,3.1319,0.77162,2.2668,-5.0806};
        F06 instance = new F06();
        double expResult = Double.NaN;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 1.0e-3);
    }

    /**
     * Test of denormalize method, of class F06.
     */
//    @Test
//    public void testDenormalize() {
//        System.out.println("denormalize");
//        double[] values = null;
//        F06 instance = new F06();
//        double[] expResult = null;
//        double[] result = instance.denormalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of normalize method, of class F06.
     */
//    @Test
//    public void testNormalize() {
//        System.out.println("normalize");
//        double[] values = null;
//        F06 instance = new F06();
//        double[] expResult = null;
//        double[] result = instance.normalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of reset method, of class F06.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F06 instance = new F06();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
