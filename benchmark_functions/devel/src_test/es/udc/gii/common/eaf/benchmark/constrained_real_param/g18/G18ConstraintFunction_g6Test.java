/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g18;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G18ConstraintFunction_g6Test {

    public G18ConstraintFunction_g6Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class G18ConstraintFunction_g6.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(18);
        G18Test.normalize(values);
        G18ConstraintFunction_g6 instance = new G18ConstraintFunction_g6();
        double expResult = 0.0;
        double result = instance.evaluate(values);
        boolean cond = (result <= expResult);
        assertTrue(cond);
    }

}