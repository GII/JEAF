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
public class F04Test {
    
    public F04Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }




    /**
     * Test of evaluate method, of class F04.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {0.4877};
        F04 instance = new F04();
        double expResult = 50.9061;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 1.0e-3);
    }

    /**
     * Test of reset method, of class F04.
//     */
//    @Test
//    public void testReset() {
//        System.out.println("reset");
//        F04 instance = new F04();
//        instance.reset();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }


}
