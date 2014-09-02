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
public class F02Test {
    
    public F02Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F02.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F02 instance = new F02();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F02.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {0.72739,1.0552,0.45722,2.9115,-2.9543,-0.63764,-0.42381,3.0179,-3.0008,
            -1.0985,1.3414,-0.11925,0.93282,4.0283,2.4708,3.7668,3.1609,2.6005,0.8687,4.7286,-4.2285,
            -4.8927,0.10049,0.11822,1.8662,-4.6006,1.5037,4.6656,-3.363,1.3231};

        F02 instance = new F02();
        double expResult = -0.0653;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 1.0e-3);
    }

    /**
     * Test of denormalize method, of class F02.
     */
//    @Test
//    public void testDenormalize() {
//        System.out.println("denormalize");
//        double[] values = null;
//        F02 instance = new F02();
//        double[] expResult = null;
//        double[] result = instance.denormalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of reset method, of class F02.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F02 instance = new F02();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
