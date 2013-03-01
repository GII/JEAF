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
 * LinearRankingSelection.java
 *
 * Created on 25 de mayo de 2007, 18:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.selection;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * In linear ranking selection, the selection probability of each individual
 * is defined as a linear function of the individuals'  rank. Linear ranking may
 * implemented by specifying the expected number of offspring of the best 
 * individual of each generation.<p>
 * 
 * Let us denote as <i>max</i> the expected number of offspring of the best
 * individual in the population and <i>min</i> the expected number of offspring
 * of the worst individual in the population.
 * 
 * If the population size is constant, we have:<p>
 * <i>min=2-max</i><p>
 * From the condition:<p>
 * <i>min&ge;0</i><p>
 * we obtaint:
 * <i>max&le;2</i><p>
 * Since it is natural to suppose that:<p>
 * <i>min&le;max</i><p>
 * so it follows:<p>
 * <i>max&ge;1</i><p>
 * 
 * The user has to define the value of <i>max</i> taking into account that
 * <i>1&le;max&ge;2</i>.<p>
 * 
 * The xml code for this operator in the config file is:<p>
 *
 * <pre>
 * {@code
 * <Operator><p>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.selection.LinearRankingSelection</Class>
 *      <Max>value</Max>
 * </Operator>
 * }
 * </pre>
 *
 * where the tag <i>Class</i> is mandatory and the tag <i>Max</i> is the expected number of offspring 
 * of the best individual. If this tag does not appear in the configuration, its value is set to the
 * default value. <p>
 *
 * Default values:
 * <ul>
 * <li> Max default value is 2.</li>
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class LinearRankingSelection extends SelectionOperator {

    private double max = 2;

    /** Creates a new instance of LinearRankingSelection */
    public LinearRankingSelection() {
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        if (conf.containsKey("Max")) {
            this.max = conf.getDouble("Max");
        } else {
            ConfWarning w = new ConfWarning("Max", this.max);
        }
    }

    @Override
    protected Individual select(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) {

        double random;
        double acum;
        int n = individuals.size();
        double min = 2 - this.max;
        double range = this.max - min;
        Individual individual;
        int i;

        //Se ordena la lista de individuos según el comparador en orden inverso:
        Collections.sort(individuals, algorithm.getComparator());
        Collections.reverse(individuals);


        //Ahora la posicion en la lista de cada individuo indica su rank:
        random = EAFRandom.nextDouble();

        acum = 0.0;

        i = 0;
        do {

            i++;
            acum += (1.0 / n) * (this.max - range * ((double) (i - 1.0) / (double) (n - 1.0)));

        } while (acum < random);

        individual = individuals.get(--i);

        return (Individual) individual.clone();

    }
}
