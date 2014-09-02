/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g01;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.benchmark.constrained_real_param.CEC2006Test;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pilar
 */
public class G01Test {

    public static void normalize(double[] values) {

        for (int i = 0; i < 9; i++) {
            values[i] = (values[i] * 2) - 1.0;
        }

        for (int i = 9; i < 12; i++) {
            values[i] = (values[i] / 50.0) - 1.0;
        }

        values[12] = values[12] * 2 - 1.0;

    }

}
