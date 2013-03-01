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
 * Configuration.java
 *
 * Created on 8 de enero de 2007, 17:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.config;

import java.io.File;
import java.io.InputStream;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EAFConfiguration extends XMLConfiguration {

    private static EAFConfiguration instance = new EAFConfiguration();

    /**
     * Creates a new instance of Configuration
     */
    private EAFConfiguration() {

        super();

    }

    public static EAFConfiguration getInstance() {

        return instance;

    }

    public void loadConfiguration(InputStream stream) {

        if (!this.isEmpty()) {
            this.clear();
        }
        try {

            this.load(stream);

        } catch (ConfigurationException ex) {
            ex.printStackTrace();
        }

    }

    public void loadConfiguration(File config_file) {

        if (!this.isEmpty()) {
            this.clear();
        }
        try {

            this.load(config_file);

        } catch (ConfigurationException ex) {
            ex.printStackTrace();
        }

    }

    public Object getObject(String parameter) {

        Object object = null;
        try {

            object = Class.forName(this.getString(parameter)).newInstance();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return object;

    }
}
