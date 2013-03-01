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
 * Created on 18 de diciembre de 2006, 11:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.problem.constraint;

import es.udc.gii.common.eaf.util.ConfWarning;
import org.apache.commons.configuration.Configuration;


/**
 * This abstract class implements an equality constraint. <p>
 *
 * To configure a subclass of this class the xml code should be like:<p>
 *
 * <pre>
 * {@code
 * <Constraint>
 *      <Class>value</Class>
 *      <Threshold>value</Threshold>
 * </Constraint>
 * }
 * <pre>
 *
 * Where the tag <i>Class</i> is mandatory and it should be the name of a class which implements a constraint.
 * The tag <i>Threshold</i> should be a double value which represent the threshold allowed to this constraint.
 * If this tag does not appear in the configuration, the value of the threshold parameter is set to its
 * default value.<p>
 *
 * Default values:
 * <ul>
 * <li>Threshold default value is 1.0e-6</li>
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class EqualityConstraint extends Constraint {

    private double threshold = 1.0e-6;

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        if (conf.containsKey("Threshold")) {
            this.threshold = conf.getDouble("Threshold");
        } else {
            ConfWarning w = new ConfWarning("Threshold", this.threshold);
            w.warn();
        }
    }

    

    @Override
    public boolean isViolated(double value) {
        if (Math.abs(value - this.getConstraintValue()) > this.threshold)
            return true;
        return false;
    }
    
    
    
}
