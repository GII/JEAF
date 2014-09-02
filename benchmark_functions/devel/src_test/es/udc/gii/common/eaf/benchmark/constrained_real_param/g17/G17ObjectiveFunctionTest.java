/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g17;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G17ObjectiveFunctionTest {

    public G17ObjectiveFunctionTest() {
    }

    /**
     * Test of evaluate method, of class G17ObjectiveFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(17);
        G17Test.normalize(values);
        G17ObjectiveFunction instance = new G17ObjectiveFunction();
        double expResult = CEC2006Test.loadBestKnownResult(17);
        double result = instance.evaluate(values);
        boolean cond = (CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon);
        assertTrue(cond);
    }

}