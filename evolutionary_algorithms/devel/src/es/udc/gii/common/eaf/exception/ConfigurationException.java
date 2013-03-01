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

/**
 * This runtime exception is thrown when something goes wrong while performing
 * configuration.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ConfigurationException extends RuntimeException {

    /**
     * Creates a new instance of <code>ConfigurationException</code> without detail message.
     */
    public ConfigurationException() {
    }

    /**
     * Constructs an instance of <code>ConfigurationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConfigurationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>ConfigurationException</code>.
     * 
     * @param ex - Thrown exception while configuration.
     */
    public <T> ConfigurationException(Class<T> sender, Exception ex) {
        super("An exception has occurred while configuring the class " + 
                sender.getCanonicalName() + "\n" +
                "Thrown exception: " +
                (ex.getMessage() == null ? "?" : ex.getMessage()));
    }
}
