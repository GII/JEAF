/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g09;

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
    G09ConstraintFunction_g1Test.class,
    G09ConstraintFunction_g2Test.class,
    G09ConstraintFunction_g3Test.class,
    G09ConstraintFunction_g4Test.class,
    G09ObjectiveFunctionTest.class
})
public class G09Suite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}