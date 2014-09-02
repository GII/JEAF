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
public class G01ConstraintFunction_g1Test {

    public G01ConstraintFunction_g1Test() {
    }

    /**
     * Test of evaluate method, of class G01ConstraintFunction_g1.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = CEC2006Test.loadBestKnownSolution(1);
        G01ConstraintFunction_g1 instance = new G01ConstraintFunction_g1();
        double expResult = 0.0;
        G01Test.normalize(values);
        double result = instance.evaluate(values);
        boolean cond = CEC2006Test.error(expResult, result) <= CEC2006Test.epsilon;
        assertTrue(cond);
    }


}