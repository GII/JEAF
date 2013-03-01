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


package es.udc.gii.common.eaf.algorithm.operator.selection;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import org.apache.commons.configuration.Configuration;

/**
 * 
 * This method of selection runs a tournament among a few individuals chosen
 * at random from the population and selects the winner for the reproduction
 * phase. <p>
 * Selection pressure can be easily adjusted by changing the tournament size. If
 * the tournament size is larger, weak individuals have a smaller change to be
 * selected.<p>
 * 
 * To configure this operator the xml code is:<p>
 *
 * <pre>
 * {@code
 * <Operator><p>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.selection.TournamentSelection</Class>
 *      <PoolSize>value</PoolSize>
 * </Operator>
 * }
 * </pre>
 *
 * Where the tag <i>Class</i> is mandatory and the tag <i>PoolSize</i> is the number of individuals 
 * that are choosen to compete. If this tag does not appear in the configuration, the parameter <i>poolSize</i>
 * is set to its default value.<p>
 *
 * Default values:
 * <ul>
 * <li> PoolSize default value is 2.</li>
 * </ul>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class TournamentSelection extends SelectionOperator {

    private int poolSize = 2;

    public TournamentSelection() {
    }

    @Override
    protected Individual select(EvolutionaryAlgorithm algorithm, List<Individual> individuals) {

        Individual[] pool = null;
        Individual[] individualsArray = null;
        Individual selected;

        int maxRandom = individuals.size() - 1;
        int selectedIndividual;

        pool = new Individual[poolSize];
        individualsArray = new Individual[individuals.size()];
        individualsArray = individuals.toArray(individualsArray);

        //LLeno la ventana:
        for (int j = 0; j < pool.length; j++) {

            selectedIndividual = (int) Math.round(EAFRandom.nextDouble() * maxRandom);
            pool[j] =
                    (Individual) individualsArray[selectedIndividual].clone();

        }

        return Collections.min(Arrays.asList(pool), algorithm.getComparator());

    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        if (conf.containsKey("PoolSize")) {
            this.poolSize = conf.getInt("PoolSize");
        } else {
            ConfWarning w = new ConfWarning("PoolSize", this.poolSize);
        }
    }
}
