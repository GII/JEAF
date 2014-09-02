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
public class F116Test {

    public F116Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testEvaluate() {

        double[] values = {109.6932, 91.7288, 100.7532, 185.5319, 85.7667, 111.7563, 290.1204, 
            144.8409, 179.3375, 297.7356, 310.9901, 227.5746, 380.3373, 281.3505, 267.556, 204.9764, 
            327.2226, 228.307, 387.4749, 344.6788, 542.8501, 418.4113, 504.5059, 374.7868, 390.7574, 
            498.5868, 148.7684, 83.3527, 139.5612, 83.9511, 133.7659, 185.9411, 167.1849, 195.557, 
            161.0981, 131.7531, 65.5088, 102.5106, 26.2626, 290.2617};
        F116 instance = new F116();
        double expResult = 1.2982723021847676E8;
        double result = instance.evaluate(instance.normalize(values));
        assertEquals(expResult, result, 0.0);
    }
}
