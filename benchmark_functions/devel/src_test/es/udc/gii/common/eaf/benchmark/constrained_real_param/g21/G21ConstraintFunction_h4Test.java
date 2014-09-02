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
public class G21ConstraintFunction_h4Test {

    public G21ConstraintFunction_h4Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class G21ConstraintFunction_h4.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(21);
        G21Test.normalize(values);
        G21ConstraintFunction_h4 instance = new G21ConstraintFunction_h4();
        double expResult = 0.0;
        double result = instance.evaluate(values);
        boolean cond = CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon;
        assertTrue(cond);
    }

}