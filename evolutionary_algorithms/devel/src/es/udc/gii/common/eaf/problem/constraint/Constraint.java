/*
* Copyright (C) 2010 Grupo Integrado de Ingeniería
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/ 


/*
 * Constraint.java
 *
 * Created on 28 de agosto de 2007, 18:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.problem.constraint;

import es.udc.gii.common.eaf.config.Configurable;
import es.udc.gii.common.eaf.util.ConfWarning;
import org.apache.commons.configuration.Configuration;

/**
 *  This interface represents a constraint of a problem. The solution of a 
 * problen can't violate this constraint.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class Constraint implements Configurable {

    private double constraint_value = 0.0;

    /**
     * This methods evaluate a list of values with a function implemented by
     * a contraint.
     * 
     * @param values the values to be evaluated.
     * @return the constraint violation value of a list of values evaluated by
     * a implemented constraint.
     */
    public abstract double evaluate(double[] values);

    public abstract boolean isViolated(double value);

    public double getConstraintValue() {
        return this.constraint_value;
    }

    @Override
    public void configure(Configuration conf) {
        if (conf.containsKey("ConstraintValue")) {
            this.constraint_value = conf.getDouble("ConstraintValue");
        } else {
            (new ConfWarning("Constraint.ConstraintValue", constraint_value)).warn();
        }
    }
}
