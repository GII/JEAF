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
public class F115Test {
    
    public F115Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {249.4405,245.7889,118.3951,47.1727,249.4173,267.8824,368.6436,94.473,
            144.3711,36.226,47.7043,21.8233,70.192,43.0017,23.5805}
;
        F115 instance = new F115();
        double expResult = 2.5990325730553504E7;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 0.0);
    }
}
