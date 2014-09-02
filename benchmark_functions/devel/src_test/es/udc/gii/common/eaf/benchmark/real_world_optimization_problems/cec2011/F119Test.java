/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F119Test {
    
    public F119Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class F119.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {1.0571,2.7784,3.3292,14.4515,5.7371,1.9687,18.624,15.7439,7.3784,2.0706,17.2088,18.2129,9.844,9.2304,15.7112,13.1605,1.776,5.0873,2.6786,7.3707,9.3914,4.2118,5.9107,10.9952,4.6707,7.3338,0.50456,17.1065,5.5903,9.1869,6.9576,12.3523,0.54239,3.094,13.2562,10.9699,8.9849,2.5634,19.7684,13.4798,7.0692,10.4954,5.757,11.9743,4.6484,8.3756,16.3641,8.2027,1.7812,4.7367,1.1341,13.2626,3.3585,3.081,4.1789,17.8618,6.7539,5.7162,18.2426,8.2481,7.4555,8.1264,11.2372,9.2103,5.9721,4.1994,2.6825,9.5512,8.9494,2.1431,4.8497,7.6451,4.4172,1.6195,17.9438,9.3599,0.93371,4.2663,9.1212,8.22,9.9539,4.4888,5.9469,7.7445,2.9824,1.9172,10.1086,16.1371,6.3107,2.309,1.6172,16.3269,9.0513,6.3039,2.1831,16.9097}
;
        F119 instance = new F119();
        double expResult = 4.9969478870063204E8;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 0.0);
    }
}
