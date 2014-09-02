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
public class F114Test {
    
    public F114Test() {
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
        double[] values = {563.1864,63.5467,46.6271,165.586,65.2895,142.4064,148.0527,112.4607,105.5807,118.3726,71.9195,83.6121,65.1925};
        F114 instance = new F114();
        double expResult = 4835530.4894882385;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 0.0);
    }
}
