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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.exception;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;

/**
 * This exception is thrown whenever a wrong instance of an evolutionary
 * algorithm is found since some operators are only meaningful when used with
 * certain algorithms.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class WrongAlgorithmException extends RuntimeException {

    /**
     * Constructs an isntance of this class.
     * @param message The concrete cause of the exception.
     */
    public WrongAlgorithmException(String message) {
        super(message);
    }

    /**
     * Default constructor.
     */
    public WrongAlgorithmException() {
    }

    /**
     * Constructs an instance of <code>WrongAlgorithmException</code>.
     * 
     * @param requiredClass - Class of the required algorithm subtype required.
     * @param foundClass - Class of the algorithm subtype found.
     */
    public <T extends EvolutionaryAlgorithm> WrongAlgorithmException(
            Class<T> requiredClass, Class<?> foundClass) {
        super("Found " + foundClass.getCanonicalName()
                + ", required " + requiredClass.getCanonicalName());
    }
}
