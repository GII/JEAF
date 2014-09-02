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
public class F09Test {
    
    public F09Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    /**
     * Test of evaluate method, of class F09.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        double[] values = {-0.83113,-0.20043,-0.48026,0.60014,-0.13717,0.8213,-0.63631,-0.47239,-0.70892,-0.72786,0.73858,0.15941,0.09972,-0.71009,0.70606,0.24411,-0.2981,0.026499,-0.19638,-0.84807,-0.52017,-0.75336,-0.63218,-0.52009,-0.16547,-0.90069,0.80543,0.88957,-0.018272,-0.021495,-0.32456,0.80011,-0.26151,-0.77759,0.5605,-0.22052,-0.51662,-0.19218,-0.80709,-0.73605,0.8841,0.91227,0.15042,-0.88044,-0.53044,-0.29368,0.64239,-0.96919,-0.91395,-0.66202,0.29823,0.46344,0.29549,-0.098153,0.094018,-0.40736,0.48939,-0.62209,0.37355,-0.63298,-0.26303,0.25124,0.56045,-0.83775,0.85877,0.55143,-0.026417,-0.12828,-0.10643,-0.3873,0.017017,0.021543,0.63526,0.58966,0.28864,-0.24278,0.62316,0.065651,-0.29855,0.878,0.75189,0.10031,0.24495,0.17409,-0.58452,-0.39751,-0.058153,-0.53902,0.68862,-0.61047,-0.54816,-0.65858,-0.54467,-0.1286,-0.3778,0.84676,-0.13959,-0.63037,0.80976,0.9595,-0.12226,-0.77776,-0.48387,-0.18256,0.18979,-0.47558,0.20569,0.42243,-0.55651,-0.76516,-0.40665,-0.36244,-0.15167,0.015717,-0.82897,-0.47504,0.60203,-0.94156,0.85771,0.46066,-0.022782,0.15705,-0.52543,-0.082302,0.92618,0.093611}
;
        F09 instance = new F09();
        double expResult = 3423733.501835257;
        double result = instance.evaluate(values);
        assertEquals(expResult, result, 0.0);
    }

 
}
