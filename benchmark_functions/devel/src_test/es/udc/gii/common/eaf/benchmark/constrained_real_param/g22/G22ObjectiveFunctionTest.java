/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g22;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G22ObjectiveFunctionTest {

    public G22ObjectiveFunctionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    /**
     * Test of evaluate method, of class G22ObjectiveFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(22);
        G22Test.normalize(values);
        G22ObjectiveFunction instance = new G22ObjectiveFunction();
        double expResult = CEC2006Test.loadBestKnownResult(22);
        double result = instance.evaluate(values);
        boolean cond = CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon;
        assertTrue(cond);
    }

}