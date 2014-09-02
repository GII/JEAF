/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g20;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G20ConstraintFunction_h13Test {

    public G20ConstraintFunction_h13Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class G20ConstraintFunction_h13.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = null;
        G20ConstraintFunction_h13 instance = new G20ConstraintFunction_h13();
        double expResult = 0.0;
        double result = instance.evaluate(values);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}