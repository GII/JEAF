/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g10;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author pilar
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    G10ConstraintFunction_g1Test.class,
    G10ConstraintFunction_g2Test.class,
    G10ConstraintFunction_g3Test.class,
    G10ConstraintFunction_g4Test.class,
    G10ConstraintFunction_g5Test.class,
    G10ConstraintFunction_g6Test.class,
    G10ObjectiveFunctionTest.class
})
public class G10Suite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}