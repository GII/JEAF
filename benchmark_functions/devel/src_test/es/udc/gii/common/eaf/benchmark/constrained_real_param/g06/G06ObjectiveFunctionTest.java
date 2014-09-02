/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g06;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G06ObjectiveFunctionTest {

    public G06ObjectiveFunctionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class G06ConstraintFunction_g2.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(6);
        G06Test.normalize(values);
        G06ObjectiveFunction instance = new G06ObjectiveFunction();
        double expResult = CEC2006Test.loadBestKnownResult(6);
        double result = instance.evaluate(values);
        boolean cond = CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon;
        assertTrue(cond);
    }
}