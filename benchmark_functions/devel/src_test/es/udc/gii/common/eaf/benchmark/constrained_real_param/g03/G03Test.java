/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g03;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.g01.*;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G03Test {

    public static void normalize(double[] values) {

        for (int i = 0; i<values.length; i++) {

            values[i] = (values[i]*2.0)-1.0;

        }

    }

}
