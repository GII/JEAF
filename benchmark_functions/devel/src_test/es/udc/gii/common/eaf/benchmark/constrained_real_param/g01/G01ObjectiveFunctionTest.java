/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g01;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G01ObjectiveFunctionTest {

    public G01ObjectiveFunctionTest() {
    }

    /**
     * Test of evaluate method, of class G01ObjectiveFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(1);
        G01Test.normalize(values);
        G01ObjectiveFunction instance = new G01ObjectiveFunction();
        double expResult = CEC2006Test.loadBestKnownResult(1);
        double result = instance.evaluate(values);
        boolean cond = CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon;
        assertTrue(cond);
    }

}