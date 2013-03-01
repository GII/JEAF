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
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * JADE is a differential evolution version which updates control parameters in an adaptive maner.
 * This class implements the adaptive strategy of the CR parameter. At each generation g, the crossover
 * probability CR<sub>i</sub> of each individual x<sub>i</sub> is independently generated according to
 * a normal distribution of mean &micro;<sub>CR</sub> and standard deviation 0.1 and then truncated to
 * [0, 1].<p>
 *
 * The mean &micro;<sub>CR</sub> is initialized to be 0.5 and then updated with the CR<sub>i</sub> values
 * of the successful individuals, i.e, these that survive their parents, as: <p>
 *
 * &micro;<sub>CR</sub> = (1 - c)&sdot;&micro;<sub>CR</sub> + c&sdot;mean<sub>A</sub>(S<sub>CR</sub>) <p>
 *
 * Where c is a positive constant between 0 and 1, mean<sub>A</sub>(&sdot;) is the usual arithmetic mean
 * and S<sub>CR</sub> is the set of successful individuals. <p>
 *
 * To configure this parameter the xml code should be: <p>
 *
 * <pre>
 * {@code
 * <Parameter>
 *      <Class>eaf.plugin.parameter.JADECRAdaptiveParameter</Class>
 *      <mu>value</mu>
 *      <std_cr>value</std_cr>
 *      <c>value</c>
 * </Parameter>
 * }
 * </pre>
 *
 * where, <i>Parameter</i> is the name of the parameter which is indicated in the Class where it is used,
 * <i>Class</i> is a mandatory tag and <i>value</i> should be change to the specific value which the
 * user wants to use. If some of the parameters (mu, std_cr or c) are not indicated its value is set to
 * their default values.<p>
 *
 * Default values:
 *
 * <ul>
 * <li>mu = 0.5</li>
 * <li>std_cr = 0.1</li>
 * <li>c = 0.1</li>
 * </ul>
 *
 * JADE is presented in "JADE: Adaptive Differential Evolution with Optional External Archive",
 * Jinqiao Zhang amd Arthur C. Sanderson, IEEE Transacions on Evolutionary Computation, Vol. 13,
 * No. 5, October 2009. <p>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class JADECRAdaptiveParameter extends Parameter {

    private double c = 0.1;
    private double mu_cr = 0.5;
    private double std_cr = 0.1;

    private int alg_generation = -1;

    public JADECRAdaptiveParameter() {}

    @Override
    public double get(EvolutionaryAlgorithm algorithm) {

        double meana_cr;
        double cr_i;
        List<Individual> individuals;
        int cr_individuals;
        double cr_ind;

        //Hay que "chequear" que los individuos sean del tipo JADE:

        if (algorithm.getGenerations() > this.alg_generation) {

            //Calculamos mu;
            individuals = algorithm.getPopulation().getIndividuals();

            meana_cr = 0.0;
            cr_individuals = 0;
            for (Individual i : individuals) {

                if (i instanceof JADEIndividual) {

                    cr_ind = ((JADEIndividual) i).getCR();
                    if (cr_ind != -Double.MAX_VALUE) {
                        meana_cr += cr_ind;
                        cr_individuals++;
                    }
                } else {
                    throw new ConfigurationException(
                            "JADECRAdaptiveParameter requires individuals of type JADEIndividual");
                }

            }

            meana_cr /= cr_individuals;


            if (!Double.isNaN(meana_cr) && !Double.isInfinite(meana_cr)) {
                this.mu_cr = (1.0 - this.c) * this.mu_cr + this.c * meana_cr;

                this.mu_cr = (this.mu_cr > 1.0 ? 1.0 : (this.mu_cr < 0.0 ? 0.0 : this.mu_cr));
            }
            this.alg_generation++;
            //System.out.println(this.mu_cr);

        }

        cr_i = 0.0;
        NormalDistributionImpl n = new NormalDistributionImpl(this.mu_cr, this.std_cr);
        try {
            double r = EAFRandom.nextDouble();
            cr_i = n.inverseCumulativeProbability(r);
            cr_i = (cr_i > 1.0 ? 1.0 : (cr_i < 0.0 ? 0.0 : cr_i));
        } catch (MathException ex) {
            Logger.getLogger(JADECRAdaptiveParameter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cr_i;
    }

    @Override
    public void configure(Configuration conf) {



        if (conf.containsKey("c")) {
            this.c = conf.getDouble("c");
        } else {
            ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".c", this.c);
            w.warn();
        }

        if (conf.containsKey("std_cr")) {
            this.std_cr = conf.getDouble("std_cr");
        } else {
            ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".std_cr", this.std_cr);
            w.warn();
        }

        if (conf.containsKey("mu")) {
            this.mu_cr = conf.getDouble("mu");
        } else {
            ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".mu_cr", this.mu_cr);
            w.warn();
        }
    }

    public double getC() {
        return c;
    }

    public double getMu_cr() {
        return mu_cr;
    }

    public double getStd_cr() {
        return std_cr;
    }
}

