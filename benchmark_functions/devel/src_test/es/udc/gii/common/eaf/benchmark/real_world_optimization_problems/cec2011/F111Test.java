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
public class F111Test {
    
    public F111Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getOptimum method, of class F111.
     */
//    @Test
//    public void testGetOptimum() {
//        System.out.println("getOptimum");
//        int dim = 0;
//        F111 instance = new F111();
//        double[][] expResult = null;
//        double[][] result = instance.getOptimum(dim);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of evaluate method, of class F111.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {62.753,-58.3328,149.879,95.5909,-151.2298,-18.7952,29.8451,162.677,
            235.2532,-121.1935,71.1771,116.0051,25.7589,166.1178,-129.0568,-0.17103,107.3045,
            114.7401,232.9868,127.8703,-50.3575,93.3172,155.858,115.0688,178.8701,41.6072,
            -2.6323,75.0886,-98.1016,153.023,-50.8617,-26.8462,-101.6103,-129.2047,211.7289,
            35.3277,-18.4091,160.5644,-155.5326,19.3722,-5.3974,75.7585,115.608,-91.5135,
            44.8822,2.9262,50.7257,90.7158,146.9684,-61.9875,33.3613,52.5706,-67.8426,-120.021,
            49.182,69.7667,-13.519,54.7276,-75.999,175.6335,-21.8376,21.251,87.7322,204.1794,
            279.6457,16.138,-55.8889,-71.7047,-61.8465,220.3586,-21.9433,85.9998,-44.3778,220.2907,
            -25.0081,-29.4426,-32.2724,63.653,28.7813,-24.1702,53.0077,37.9055,44.4198,215.2213,
            -57.0805,43.436,73.2831,-4.6707,68.4851,-162.0729,-47.9865,26.4675,110.9585,222.2845,
            -135.0469,18.9471,13.572,-111.5484,-28.4085,-118.9088,48.257,-19.6448,38.2746,-100.4275,
            100.991,-20.8137,52.3566,84.8722,144.2237,25.2708,-44.1032,-36.9148,149.8678,-106.0012,
            212.9085,14.9845,124.1883,-92.3291,15.9249,-146.6736};
        F111 instance = new F111();
        double expResult = 1.9136280613309822E9;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of reset method, of class F111.
     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F111 instance = new F111();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of normalize method, of class F111.
     */
//    @Test
//    public void testNormalize() {
//        System.out.println("normalize");
//        double[] values = null;
//        F111 instance = new F111();
//        double[] expResult = null;
//        double[] result = instance.normalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of denormalize method, of class F111.
     */
//    @Test
//    public void testDenormalize() {
//        System.out.println("denormalize");
//        double[] values = null;
//        F111 instance = new F111();
//        double[] expResult = null;
//        double[] result = instance.denormalize(values);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
