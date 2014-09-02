/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g09;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G09ConstraintFunction_g3Test {

    public G09ConstraintFunction_g3Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class G09ConstraintFunction_g3.
     */
    @Test
    public void testEvaluate() {
       System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(9);
        G09Test.normalize(values);
        G09ConstraintFunction_g3 instance = new G09ConstraintFunction_g3();
        double expResult = 0.0;
        double result = instance.evaluate(values);
        boolean cond = (result <= expResult);
        assertTrue(cond);
    }

}