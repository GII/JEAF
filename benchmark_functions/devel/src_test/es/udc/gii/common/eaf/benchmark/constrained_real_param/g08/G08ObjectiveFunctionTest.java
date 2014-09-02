/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g08;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G08ObjectiveFunctionTest {

    public G08ObjectiveFunctionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class G08ObjectiveFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(8);
        G08Test.normalize(values);
        G08ObjectiveFunction instance = new G08ObjectiveFunction();
        double expResult = CEC2006Test.loadBestKnownResult(8);
        double result = instance.evaluate(values);
        boolean cond = (CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon);
        assertTrue(cond);
    }
}