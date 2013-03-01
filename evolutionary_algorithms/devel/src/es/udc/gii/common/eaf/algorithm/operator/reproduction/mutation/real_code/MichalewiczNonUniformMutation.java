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
 * FloatNonUniformMutation.java
 *
 * Created on 22 de mayo de 2007, 20:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.real_code;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.MutationOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.plugin.stoptest.StopTestPlugin;
import es.udc.gii.common.eaf.plugin.stoptest.GenerationsPlugin;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * If this operator is applied in a generation <i>t</i> and <i>g<sub>max</sub></i>
 * is the maximum number of generations then<p>
 * 
 * <i>c'<sub>i</sub>=</i><p>
 * <i>c<sub>i</sub>+&delta(t,b<sub>i</sub>-c<sub>i</sub>)</i> if &tau = 0<p>
 * <i>c<sub>i</sub>-&delta(t,c<sub>i</sub>-a<sub>i</sub>)</i> if &tau = 1<p>
 * 
 * with &tau being a random number which may have a value of zero or one, and:<p><p>
 * 
 * <i>&delta(t,y)=y(1-r<sup>(1-t/g<sub>max</sub>)<sup>b</sup></sup>)</i><p>
 * 
 * Where <i>r</i> is a random number from the interval [0,1] and <i>b</i> is a
 * parameter chosen by the user, which determines the degree of dependency on the
 * number of iterations. This function gives a value in the range of [0,y] such
 * that the probability of returning a number close to 0 increases as the algorithm
 * advances. The size of the gene generation interval shall be lower with the passing
 * of generations. This property causes this operator to make a uniform search in
 * the initial space when <i>t</i> is small, and very locally at a later stage,
 * favouring local tunning.
 * 
 * To configure this operator the tag at the xml config file would be like:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.real_code.MichalewiczNonUniformMutation</Class>
 *      <Probability>value</Probability>
 *      <B>value</B>
 * </Operator>
 * }
 * </pre>
 * Where, <i>Class</i> is the mandatory tag which indicates the class to be used. The <i>Probability</i>
 * tag is a double value that indicates the probability of a gene to be mutated. The tag <i>B</i> is a
 * double value which represents the b parameter of the Michalewicz non uniform mutation operator. If these
 * to last tags do not appear in the configuration file, their values are set to their default values. <p>
 *
 * Default values:
 * <ul>
 * <li>Probability default value is 0.5.
 * <li>B default value is 5.
 * </ul>
 * 
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MichalewiczNonUniformMutation extends MutationOperator {

    private double b = 5;
    
    private StopTestPlugin plugin = new GenerationsPlugin();

    /** Creates a new instance of FloatNonUniformMutation */
    public MichalewiczNonUniformMutation() {
    }

    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);
            if (conf.containsKey("B")) {
                this.b = conf.getDouble("B");
            }
            if (conf.containsKey("Plugin")) {
                this.plugin = (StopTestPlugin) Class.forName(
                        conf.getString("Plugin")).newInstance();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * If this operator is applied in a generation t, and gmax is the maximum
     * number of generations then:
     *
     *  c'i = ci + delta(t,bi-ci) if tau = 0;
     *  c'i = ci - delta(t,ci-ai) if tau = 1;
     *
     *  with tau being a random number which may have a value of zero or one
     *  and:
     *
     *      delta (t,y) = y*(a-r^(1-(t/gmax)^b))
     *  where r is a random number from the interval [0,1] and b is a parameter
     *  chosen by the user, which determines the degree of dependency on the number
     *  of iterations. This function gives a value in the ranger [0,y] such that
     *  the probability of returning a number close to zero increases as the
     *  algorithm advances. The size of the gene generation interval shall be
     *  lower with the passing of generations. This property causes this operator
     *  to make a uniform search in the initial space when t is small, and very
     *  locally at a later stage, favouring local tuning.
     */
    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm, Individual individual) {

        //Generation:
        int t,  gmax;
        boolean tau;
        double[] genes = individual.getChromosomeAt(0);
        Double newValue,a ,c, d ;
        double mutationGene;
        List<Individual> mutated_individual = new ArrayList<Individual>();

        t = plugin.getCurrent(algorithm);
        gmax = plugin.getMax(algorithm);

//        t = algorithm.getGenerations();
//        gmax = algorithm.getMaxGenerations();

        for (int i = 0; i < genes.length; i++) {

            if (super.getProbability() >= EAFRandom.nextDouble() * 100) {

                //Gen a mutar:
                mutationGene = genes[i];

                tau = EAFRandom.nextBoolean();

                a = -1.0;
                d = 1.0;
                c = mutationGene;

                if (tau) {

                    newValue = c -
                            this.delta(t, c - a, gmax, EAFRandom.nextDouble());
                } else {
                    newValue = c + this.delta(t, d - c, gmax, EAFRandom.nextDouble());
                }

                genes[i] = checkBounds(algorithm, newValue);

            }
        }

        individual.setChromosomeAt(0, genes);
        mutated_individual.add(individual);
        return mutated_individual;
    }

    private double delta(int t, double y, int gmax, double r) {

        double delta = 0.0f;

        delta = (double) (y * (1 - Math.pow(r, Math.pow(1 - (t / gmax), this.b))));

        return delta;

    }
}
