/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g07;

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
    G07ConstraintFunction_g1Test.class,
    G07ConstraintFunction_g2Test.class,
    G07ConstraintFunction_g3Test.class,
    G07ConstraintFunction_g4Test.class,
    G07ConstraintFunction_g5Test.class,
    G07ConstraintFunction_g6Test.class,
    G07ConstraintFunction_g7Test.class,
    G07ConstraintFunction_g8Test.class,
    G07ObjectiveFunctionTest.class
})
public class G07Suite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}