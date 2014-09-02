/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g21;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G21ObjectiveFunctionTest {

    public G21ObjectiveFunctionTest() {
    }


    /**
     * Test of evaluate method, of class G21ObjectiveFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(21);
        G21Test.normalize(values);
        G21ObjectiveFunction instance = new G21ObjectiveFunction();
        double expResult = CEC2006Test.loadBestKnownResult(21);
        double result = instance.evaluate(values);
        boolean cond = CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon;
        assertTrue(cond);
    }

}