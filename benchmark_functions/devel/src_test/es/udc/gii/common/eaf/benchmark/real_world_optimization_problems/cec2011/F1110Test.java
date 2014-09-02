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
public class F1110Test {
    
    public F1110Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of evaluate method, of class F1110.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {3.381,4.1458,14.9263,7.124,0.48447,7.5112,12.0694,13.3132,7.2971,7.8653,15.6275,10.4557,6.9253,6.51,7.9304,7.7391,7.8018,4.5383,12.1573,15.895,1.0481,2.651,10.9908,12.8228,8.9048,8.6906,14.6868,7.616,0.72885,2.2967,15.967,18.3161,6.8372,2.6887,14.4545,8.3242,1.1749,7.2665,6.5763,14.8457,7.4913,6.7487,14.8006,9.8179,7.3496,10.2354,17.3386,8.0348,3.6644,4.8228,13.7006,14.1753,7.8936,4.8089,4.1206,8.04,7.7193,3.3511,7.7654,13.6213,2.2895,7.2775,9.6896,8.8221,7.8193,2.4055,5.8813,9.8485,5.3087,2.3235,8.1063,8.2582,1.1228,8.5599,5.8314,14.2424,9.6442,5.3924,13.895,16.0972,4.3264,7.3995,2.1951,18.2051,1.8746,3.8956,15.9566,12.8512,7.6896,5.0641,5.4588,7.4468,6.7329,5.3661,9.0348,14.3183}
;
        F1110 instance = new F1110();
        double expResult = 3.1668350488462234E8;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 0.0);
    }
}
