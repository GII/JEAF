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
 * MOPMutation.java
 *
 * Created on 19 de diciembre de 2006, 12:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * 
 * This operator were developed by Deb and Goyal [1]. The current value of a 
 * variable is changed to a neighboring value using a polynomial probability distribution having its
 * mean at the current value and its variance as a function of the distribution index (n).
 * 
 * To configure this operator the tag at the xml config file would be like:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.PolynomialMutation</Class>
 *      <Probability>value</Probability>
 *      <N>value</N>
 * </Operator>
 * }
 * </pre>
 * Where, <i>Class</i> is the mandatory tag which indicates the class to be used. The <i>Probability</i>
 * tag is a double value that indicates the probability of a gene to be mutated. The tag <i>N</i> is a
 * double value which represents the distribution index <i>n</i>. If these
 * to last tags do not appear in the configuration file, their values are set to their default values. <p>
 *
 * Default values:
 * <ul>
 * <li>Probability default value is 0.5.
 * <li>N default value is 1.
 * </ul>
 * 
 * [1] Kalyanmoy Deb and Mayank Goyal. A combined genetic adaptive search (GeneAS) for 
 *  engineering design. Computer Science and Informatics, 26:30–45, 1996.<p>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class PolynomialMutation extends MutationOperator {

    private double N = 1.0;

    /** Creates a new instance of MOPMutation */
    public PolynomialMutation() {
    }

    public PolynomialMutation(int probability) {
        super(probability);
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);

        if (conf.containsKey("N")) {
            this.N = conf.getInt("N");
        } else {
            this.N = 1;
            ConfWarning w = new ConfWarning("PolynomialMutation.N", this.N);
            w.warn();
        }
    }

    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm,
            Individual individual) {


        double y, yl, yu, deltaq, mut_pow, rnd;
        double[] gens = individual.getChromosomeAt(0);
        List<Individual> mutated_individual = new ArrayList<Individual>();

        
        for (int j = 0; j < gens.length; j++) {
            
            if (EAFRandom.nextDouble() * 100 <= super.getProbability()) {
                y = gens[j];
                yl = -1.0;
                yu = 1.0;
                rnd = EAFRandom.nextDouble();
                mut_pow = 1.0 / (this.N + 1.0);
                deltaq = 0.0;
                if (rnd < 0.5) {
                    deltaq = Math.pow(2.0 * rnd, mut_pow) - 1.0;
                } else {
                    deltaq = 1.0 - Math.pow(2.0 * (1.0 - rnd), mut_pow);
                }
                y = y + deltaq * (yu - yl);

                gens[j] = checkBounds(algorithm, y);

            }

        }

        individual.setChromosomeAt(0, gens);
        mutated_individual.add(individual);

        return mutated_individual;
    }

    @Override
    public String toString() {
        return "Polynomial Mutation Operator";
    }
}
