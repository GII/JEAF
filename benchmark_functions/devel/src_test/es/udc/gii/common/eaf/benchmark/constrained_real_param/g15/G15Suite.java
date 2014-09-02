/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g15;

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
    G15ConstraintFunction_h1Test.class,
    G15ConstraintFunction_h2Test.class,
    G15ObjectiveFunctionTest.class
})
public class G15Suite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

}