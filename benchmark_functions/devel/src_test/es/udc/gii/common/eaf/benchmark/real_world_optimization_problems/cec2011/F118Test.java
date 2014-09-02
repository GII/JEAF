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
public class F118Test {
    
    public F118Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class F118.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {7.6885,3.0053,17.2396,18.8785,5.1442,9.4585,11.7605,8.857,1.9986,5.1626,14.9741,16.907,7.8996,4.3667,10.6813,8.0794,1.1171,2.7266,13.573,12.9421,1.8971,5.9551,2.9522,7.6597,8.5071,6.545,18.5922,15.36,5.8279,8.8386,17.5803,18.8669,0.0052238,9.2889,12.2513,18.8794,5.2768,5.8157,16.027,9.7341,4.9809,9.6077,11.4932,17.1421,7.3864,6.7739,4.9347,14.997,0.83483,7.1336,13.2189,15.757,8.9075,10.3407,15.3806,13.9774,9.2831,6.7208,0.33966,8.4503,8.6271,5.8587,16.8971,9.5129,5.5229,7.169,0.63982,14.3766,3.6241,1.9458,9.7914,9.3101,1.2308,3.3494,2.9303,9.2689,0.42652,7.2168,5.6373,13.4632,6.9516,5.992,10.716,12.3422,1.2393,5.9132,17.06,17.4871,2.7029,3.3762,11.2996,14.6837,4.1703,3.3538,18.9587,7.9849}
;
        F118 instance = new F118();
        double expResult = 3.8857902327753377E8;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 0.0);
    }
}
