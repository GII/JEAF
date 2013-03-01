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

import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;

/**
 * Some clases require a concrete type of comparator. This exception is thrown
 * whenever a not supported comparator is found by those classes.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class WrongComparatorException extends RuntimeException {

    /**
     * Constructs an instance of <code>WrongAlgorithmException</code>.
    
     * @param requiredClass - Class of the required comparator subtype required.
     */
    public <T extends FitnessComparator> WrongComparatorException(
            Class<?> foundClass) {
        super("Found wrong comparator " + foundClass.getCanonicalName());
    }

    /**
     * Constructs an isntance of this class.
     * @param message The concrete cause of the exception.
     */
    public WrongComparatorException(String message) {
        super(message);
    }
}
