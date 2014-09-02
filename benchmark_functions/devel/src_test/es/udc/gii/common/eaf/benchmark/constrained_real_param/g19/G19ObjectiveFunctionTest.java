/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g19;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G19ObjectiveFunctionTest {

    public G19ObjectiveFunctionTest() {
    }

    /**
     * Test of evaluate method, of class G19ObjectiveFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(19);
        G19Test.normalize(values);
        G19ObjectiveFunction instance = new G19ObjectiveFunction();
        double expResult = CEC2006Test.loadBestKnownResult(19);
        double result = instance.evaluate(values);
        boolean cond = CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon;
        assertTrue(cond);
    }

}