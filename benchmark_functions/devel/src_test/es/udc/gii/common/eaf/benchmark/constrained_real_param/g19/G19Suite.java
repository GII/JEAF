/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g19;

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
    G19ConstraintFunction_g1Test.class,
    G19ConstraintFunction_g2Test.class,
    G19ConstraintFunction_g3Test.class,
    G19ConstraintFunction_g4Test.class,
    G19ConstraintFunction_g5Test.class,
    G19ObjectiveFunctionTest.class
})
public class G19Suite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}