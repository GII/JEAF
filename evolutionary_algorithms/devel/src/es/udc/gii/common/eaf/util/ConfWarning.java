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


package es.udc.gii.common.eaf.util;

/**
 * Represents a warning that is raised in the library while configuration.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ConfWarning {    
    private static final String template = "WARNING: The missing " +
            "configuration parameter %s is set to %s.%n";
    
    private String param = null;
    private String value = null;

    public ConfWarning(String param, String value) {
        this.param = param;
        this.value = value;
    }
    
    public ConfWarning(String param, int value) {
        this.param = param;
        this.value = String.valueOf(value);
    }
    
    public ConfWarning(String param, double value) {
        this.param = param;
        this.value = String.valueOf(value);
    }
    
    public void warn() {
        System.err.printf(template, param, value);
    }

    @Override
    public String toString() {
        return template.replaceFirst("%s", this.param).replaceFirst("%s", this.value);
    }        
}
