/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_param.cec2005;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rafa
 */
public class F03Test extends CEC2005Test {

    private static final F03 instance = new F03(50);

    public F03Test() {
    }

    /**
     * Test of getNumber method, of class F03.
     */
    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
        int expResult = 3;
        int result = instance.getNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of lowerBound method, of class F03.
     */
    @Test
    public void testLowerBound() {
        System.out.println("lowerBound");
        double expResult = -100.0;
        double result = instance.lowerBound();
        assertEquals(expResult, result);
    }

    /**
     * Test of upperBound method, of class F03.
     */
    @Test
    public void testUpperBound() {
        System.out.println("upperBound");
        double expResult = 100.0;
        double result = instance.upperBound();
        assertEquals(expResult, result);
    }

    /**
     * Test of evaluate method, of class F03.
     */
    @Test
    public void testGetBias() {
        System.out.println("getBias");
        double expResult = -450.0;
        double result = instance.getBias();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of evaluate method, of class F03.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] results = getResults(instance.getNumber());
        double[][] vectors = getTestVectors(instance.getNumber());

        for(int i = 0; i < N; i++) {
            normalize(vectors[i], instance.lowerBound(), instance.upperBound());
            double result = instance.evaluate(vectors[i]);
            boolean cond = (error(results[i], result) <= epsilon);
            assertTrue(cond);
            System.out.print("#");
        }
        System.out.println();
    }
}