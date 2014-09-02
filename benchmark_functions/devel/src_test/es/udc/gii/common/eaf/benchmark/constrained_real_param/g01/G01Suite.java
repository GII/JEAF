/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g01;

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
    G01ConstraintFunction_g1Test.class,
    G01ConstraintFunction_g2Test.class,
    G01ConstraintFunction_g3Test.class,
    G01ConstraintFunction_g4Test.class,
    G01ConstraintFunction_g5Test.class,
    G01ConstraintFunction_g6Test.class,
    G01ConstraintFunction_g7Test.class,
    G01ConstraintFunction_g8Test.class,
    G01ConstraintFunction_g9Test.class,
    G01ObjectiveFunctionTest.class})
public class G01Suite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}