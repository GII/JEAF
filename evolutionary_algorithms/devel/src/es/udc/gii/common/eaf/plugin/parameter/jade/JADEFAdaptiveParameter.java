/*
 *  Copyright (C) 2010 Grupo Integrado de Ingeniería
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.udc.gii.common.eaf.plugin.parameter.jade;


import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.jade.JADEIndividual;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.plugin.parameter.Parameter;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.CauchyDistribution;
import org.apache.commons.math.distribution.CauchyDistributionImpl;
import org.apache.commons.math.random.RandomDataImpl;

/**
 * JADE is a differential evolution version which updates control parameters in an adaptive maner.
 * This class implements the adaptive strategy of the F parameter. At each generation g, the mutation factor
 * F<sub>i</sub> of each individual x<sub>i</sub> is independently generated according to
 * a Cauchy distribution with location parameter &micro;<sub>F</sub> and scale parameter 0.1 and then truncated to
 * 1 if F<sub>i</sub> &ge; 1 or regenerated if F<sub>i</sub> &le; 0.<p>
 *
 * The mean &micro;<sub>F</sub> is initialized to be 0.5 and then updated with the F<sub>i</sub> values
 * of the successful individuals, i.e, these that survive their parents, as: <p>
 *
 * &micro;<sub>CR</sub> = (1 - c)&sdot;&micro;<sub>CR</sub> + c&sdot;mean<sub>C</sub>(S<sub>F</sub>) <p>
 *
 * Where c is a positive constant between 0 and 1, S<sub>F</sub> is the set of successful individuals and
 * mean<sub>L</sub>(&sdot;) is the Lehmer mean:<p>
 *
 * mean<sub>L</sub>(S<sub>F</sub>) = &sum;<sub>(F &isin; S<sub>F</sub>)</sub> F<sup>2</sup>/&sum;<sub>(F &isin; S<sub>F</sub>)</sub> F<p>
 *
 * To configure this parameter the xml code should be: <p>
 *
 * <pre>
 * {@code
 * <Parameter>
 *      <Class>eaf.plugin.parameter.JADECRAdaptiveParameter</Class>
 *      <mu>value</mu>
 *      <std_f>value</std_f>
 *      <c>value</c>
 * </Parameter>
 * }
 * </pre>
 *
 * where, <i>Parameter</i> is the name of the parameter which is indicated in the Class where it is used,
 * <i>Class</i> is a mandatory tag and <i>value</i> should be change to the specific value which the
 * user wants to use. If some of the parameters (mu, std_f or c) are not indicated its value is set to
 * their default values.<p>
 *
 * Default values:
 *
 * <ul>
 * <li>mu = 0.5</li>
 * <li>std_f = 0.1</li>
 * <li>c = 0.1</li>
 * </ul>
 *
 * JADE is presented in "JADE: Adaptive Differential Evolution with Optional External Archive",
 * Jinqiao Zhang amd Arthur C. Sanderson, IEEE Transacions on Evolutionary Computation, Vol. 13,
 * No. 5, October 2009. <p>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0.
 */
public class JADEFAdaptiveParameter extends Parameter {

    private double init_mu_f = 0.5;
    private double c = 0.1;
    private double mu_f = 0.5;
    private double std_f = 0.1;
    private RandomDataImpl rand_imp;
    private int alg_generation = -1;

    public JADEFAdaptiveParameter() {

        this.rand_imp = new RandomDataImpl();

    }

    @Override
    public double get(EvolutionaryAlgorithm algorithm) {

        //Lehmer mean:
        double meanl_f;
        double sum_f_i, sum_f_i_2;
        List<Individual> individuals;
        int f_individuals;
        double f_ind, f_i;

        //Hay que "chequear" que los individuos sean del tipo JADE:

        sum_f_i = 0.0;
        sum_f_i_2 = 0.0;

        if (algorithm.getGenerations() > this.alg_generation) {

            //Calculamos mu;
            individuals = algorithm.getPopulation().getIndividuals();

            meanl_f = 0.0;
            f_individuals = 0;
            for (Individual i : individuals) {

                if (i instanceof JADEIndividual) {

                    f_ind = ((JADEIndividual) i).getF();
                    if (f_ind != -Double.MAX_VALUE) {
                        sum_f_i += f_ind;
                        sum_f_i_2 += f_ind * f_ind;
                        f_individuals++;
                    }
                } else {
                    throw new ConfigurationException(
                            "JADECRAdaptiveParameter requires individuals of type JADEIndividual");
                }

            }

            if (f_individuals > 0) {
                meanl_f = sum_f_i_2 / sum_f_i;
                this.mu_f = (1.0 - this.c) * this.mu_f + this.c * meanl_f;
            }



            this.mu_f = (this.mu_f > 1.0 ? 1.0 : (this.mu_f < 0.0 ? 0.0 : this.mu_f));

            this.alg_generation++;
        }


        CauchyDistribution d = new CauchyDistributionImpl(this.mu_f, this.std_f);
        f_i = 0.0;
        try {
            do {
                double r = EAFRandom.nextDouble();
                f_i = d.inverseCumulativeProbability(r);
            } while (f_i < 1.0e-8);
            f_i = (f_i > 1.0 ? 1.0 : (f_i < 0.0 ? 0.0 : f_i));
        } catch (MathException ex) {
            Logger.getLogger(JADEFAdaptiveParameter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return f_i;
    }

    @Override
    public void configure(Configuration conf) {


        if (conf.containsKey("c")) {
            this.c = conf.getDouble("c");
        } else {
            ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".c", this.c);
            w.warn();
        }

        if (conf.containsKey("std_f")) {
            this.std_f = conf.getDouble("std_f");
        } else {
            ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".std_f", this.std_f);
            w.warn();
        }

        if (conf.containsKey("mu")) {
            this.mu_f = conf.getDouble("mu");
        } else {
            ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".mu_f", this.mu_f);
            w.warn();
        }
    }

    public double getC() {
        return c;
    }

    public double getMu_f() {
        return mu_f;
    }

    public double getStd_f() {
        return std_f;
    }
}
