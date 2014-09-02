/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_param.cec2005;

import es.udc.gii.common.eaf.util.EAFRandom;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rafa
 */
public class F25Test extends CEC2005Test {

    private static final F25 instance = new F25(50);

    public F25Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        EAFRandom.init();
        instance.disableNoise();
    }

    /**
     * Test of getNumber method, of class F25.
     */
    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
        int expResult = 25;
        int result = instance.getNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of lowerBound method, of class F25.
     */
    @Test
    public void testLowerBound() {
        System.out.println("lowerBound");
        double expResult = 2.0;
        double result = instance.lowerBound();
        assertEquals(expResult, result);
    }

    /**
     * Test of upperBound method, of class F25.
     */
    @Test
    public void testUpperBound() {
        System.out.println("upperBound");
        double expResult = 5.0;
        double result = instance.upperBound();
        assertEquals(expResult, result);
    }

    /**
     * Test of evaluate method, of class F25.
     */
    @Test
    public void testGetBias() {
        System.out.println("getBias");
        double expResult = 260.0;
        double result = instance.getBias();
        assertEquals(expResult, result);
    }

    /**
     * Test of evaluate method, of class F25.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] results = getResults(instance.getNumber());
        double[][] vectors = getTestVectors(instance.getNumber());

        for (int i = 0; i < N; i++) {
            normalize(vectors[i], instance.lowerBound(), instance.upperBound());
            double result = instance.evaluate(vectors[i]);
            boolean cond = error(results[i], result) <= epsilon;
            assertTrue(cond);
            System.out.print("#");
        }
        System.out.println();
    }
}