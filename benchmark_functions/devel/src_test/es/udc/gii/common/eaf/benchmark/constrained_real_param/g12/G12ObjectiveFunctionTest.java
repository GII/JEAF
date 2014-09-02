/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g12;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G12ObjectiveFunctionTest {

    public G12ObjectiveFunctionTest() {
    }

    /**
     * Test of evaluate method, of class G12ObjectiveFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(12);
        G12Test.normalize(values);
        G12ObjectiveFunction instance = new G12ObjectiveFunction();
        double expResult = CEC2006Test.loadBestKnownResult(12);
        double result = instance.evaluate(values);
        boolean cond = (CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon);
        assertTrue(cond);
    }

}