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

import es.udc.gii.common.eaf.algorithm.population.Individual;

/**
 * Some algorithms require specific instances of {@link Individual} subclases.
 * Whenever those algorithms find an individual of a type different from that
 * required, this exception is thrown.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class WrongIndividualException extends RuntimeException {

    /**
     * Creates a new instance of <code>WrongIndividualException</code> without detail message.
     */
    public WrongIndividualException() {
    }

    /**
     * Constructs an instance of <code>WrongIndividualException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public WrongIndividualException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>WrongIndividualException</code>.
     * 
     * @param requiredClass - Class of the required Individual subtype required.
     * @param foundClass - Class of the Individual subtype found.
     */
    public <T extends Individual> WrongIndividualException(Class<T> requiredClass, Class<?> foundClass) {
        super("Found " + foundClass.getCanonicalName()
                + ", required " + requiredClass.getCanonicalName());
    }
}
