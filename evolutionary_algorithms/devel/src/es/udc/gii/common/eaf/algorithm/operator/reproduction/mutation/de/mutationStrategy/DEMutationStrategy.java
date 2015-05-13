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
package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.mutationStrategy;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.plugin.parameter.Constant;
import es.udc.gii.common.eaf.plugin.parameter.Parameter;
import es.udc.gii.common.eaf.util.ConfWarning;
import org.apache.commons.configuration.Configuration;

/**
 * The mutation operator of the Differential Evolution Algorithm use different mutation strategies to
 * create the individuals of the population. This abstract class represents these mutation strategies. 
 * Each specific mutation strategy should override the method getMutatedIndividual in order to generate a
 * new mutated individual.<p>
 *
 * All the mutation strategies share two parameters the mutation factor, <i>F</i> and the number of
 * difference vectors that are used to generate the new individual, <i>diffVector</i>. The parameter F
 * is implemented as a plugin so it could be set to a constat value, using the Constat class from the
 * parameter package, or it could be set as an adaptive parameter using some of the adaptive parameters classes.
 * In the canonical version of the DE algorithm the DE/rand/1/- configuration is used, where <i>rand</i>
 * indicates the random mutation strategy and <i>1</i> is the number of difference vectors. <p>
 *
 * As this is an abstract class, it could not be instanciate, but when we use a subclass that inherit
 * from it, its xml configuration code should have at least a configuration for the F parameter and the
 * diffVector parameter, among other specific configuration parameters of the subclass. So the xml code
 * should be like this:<p>
 *
 * <pre>
 * {@code
 * <MutationStrategy>
 *      <Class>value</Class<br>
 *      <F>value</F<<br>
 *      <diffVector>value</diffVector>
 *      ...
 * <MutationStrategy>
 * }
 * </pre>
 *
 * where the tag <i>Class</i> indicates a subclass of this abstract class, <i>F</i> indicates the plugin used
 * and <i>diffVector</i> is an integer which indicates the number of difference vector used to generate the
 * mutated individual. If some of the parameters do not appear in the configuration, they are set
 * to their default values.<p>
 *
 * Default values:
 * <ul>
 * <li>F as a constant parameter with value 0.5</li>
 * <li>diffVector defaults value is 1 </li>
 * </ul>
 *
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class DEMutationStrategy {

    /**
     * F is a real factor (F > 0 and F in [0,2]) which controls de amplification
     * of the differential variation. F should not be smaller than certain value to prevent
     * premature convergence. A larger F increases the probability for escaping a local optimum.
     * However for F > 1 the convergence speed decreases. It is difficult to converge
     * for a population when the perturbation is larger than the distance between to members.
     * F is a parameter plugin, it could be a constant or an adaptive parameter.
     */
    private Parameter F_plugin = new Constant(0.5);

    /**
     * Integer value which indicate the number of difference vectors used to generate a new solution.
     */
    private int diffVector = 1;

    public abstract Individual getMutatedIndividual(EvolutionaryAlgorithm algorithm, Individual target);

    public Parameter getFPlugin() {
        return this.F_plugin;
    }

    public int getDiffVector() {
        return this.diffVector;
    }

    public void configure(Configuration conf) {

        try {
            if (conf.containsKey("F.Class")) {
                this.F_plugin = (Parameter) Class.forName(conf.getString("F.Class")).newInstance();
                this.F_plugin.configure(conf.subset("F"));
            } else {
                ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".F",
                        this.F_plugin.getClass().getSimpleName());

                w.warn();
            }

            if (conf.containsKey("DiffVector")) {
                this.diffVector = conf.getInt("DiffVector");
            } else {
                ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".DiffVector",
                        this.diffVector);

                w.warn();
            }

        } catch (Exception ex) {
            throw new ConfigurationException(this.getClass(), ex);
        }

    }
    
}
