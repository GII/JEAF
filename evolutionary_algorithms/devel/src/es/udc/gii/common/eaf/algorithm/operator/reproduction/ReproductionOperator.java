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
 * ReproductionOperator.java
 *
 * Created on 20 de noviembre de 2006, 18:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.Operator;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class ReproductionOperator implements Operator {

    /** Creates a new instance of ReproductionOperator */
    public ReproductionOperator() {
    }

    @Override
    public void configure(Configuration conf) {
    }

    final protected double checkBounds(EvolutionaryAlgorithm ea, double value) {

        if (ea != null && ea.getProblem() != null) {
            if (ea.getProblem().isCheckBounds()) {
                if (value > 1) {
                    return 1;
                } else if (value < -1) {
                    return -1;
                } else {
                    return value;
                }
            } else {
                return value;
            }
        } else {
            return value;
        }
    }
}
