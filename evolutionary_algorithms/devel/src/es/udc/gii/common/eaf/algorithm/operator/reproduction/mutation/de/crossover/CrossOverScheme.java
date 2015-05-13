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
 * CrossOverScheme.java
 *
 * Created on 30 de agosto de 2007, 12:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.crossover;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.config.Configurable;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.plugin.parameter.Constant;
import es.udc.gii.common.eaf.plugin.parameter.Parameter;
import org.apache.commons.configuration.Configuration;

/**
 * In order to increase the diveristy of the population, the DE algorithm uses a crossover operator.
 * After the generation of the mutated individual, a crossover operator is execute to the mutated vector
 * and the target vector.<p>
 *
 * This operator is applied over these two vectors and it generates a new vector, the trial one.
 * This trial vector has some genes from the mutated vector and other from the target vector.<p>
 *
 * As this is an abstract class, it could not be instanciate, but when we use a subclass that inherit
 * from it, its xml configuration code should have at least a configuration for the CR parameter,
 * among other specific configuration parameters of the subclass. So the xml code should be like this:<p>
 *
 *
 * <pre>
 * {@code
 * <CrossOverScheme>
 *      <Class>value</Class>
 *      <CR>value</CR>
 *      ...
 * <CrossOverScheme>
 * }
 * </pre>
 *
 * where the tag lt;Class&gt; indicates a subclass of this abstract class and
 * CR indicates the plugin used. If some of the parameters do not appear in the configuration, they are set
 * to their default values.<p>
 *
 * Default values:
 * <ul>
 * <li>CR is set to Constant parameter with value 0.1</li>
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class CrossOverScheme implements Configurable {

    private Parameter CR_plugin = new Constant(0.1);

    public CrossOverScheme() {
    }

    public CrossOverScheme(Parameter CR_plugin) {
        this.CR_plugin = CR_plugin;
    }

    public Parameter getCRPlugin() {
        return this.CR_plugin;
    }

    public void setCRPlugin(Parameter CR_plugin) {
        this.CR_plugin = CR_plugin;
    }

    @Override
    public void configure(Configuration conf) {

        try {
            if (conf.containsKey("CR.Class")) {
                this.CR_plugin = (Parameter) Class.forName(conf.getString("CR.Class")).newInstance();
                this.CR_plugin.configure(conf.subset("CR"));
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new ConfigurationException(this.getClass(), ex);
        }
    }

    public abstract Individual crossOver(EvolutionaryAlgorithm ea, Individual target, Individual v);

    
}
