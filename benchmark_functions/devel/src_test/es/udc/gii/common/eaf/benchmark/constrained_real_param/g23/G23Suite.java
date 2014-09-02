/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g23;

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
    G23ConstraintFunction_g1Test.class,
    G23ConstraintFunction_g2Test.class,
    G23ConstraintFunction_h1Test.class,
    G23ConstraintFunction_h2Test.class,
    G23ConstraintFunction_h3Test.class,
    G23ConstraintFunction_h4Test.class,
    G23ObjectiveFunctionTest.class
})
public class G23Suite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}