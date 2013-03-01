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


package es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import java.util.List;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import org.apache.commons.configuration.Configuration;

/**
 * Random Crossover is a fairly simple crossover operator. Basically, the operator choose <i>n</i>
 * random points in the chromosomes of the individuals and generate two new individuals by exchanging
 * the genes between the individuals about this <i>n</i> points.<p>
 *
 * To config this operator the xml code is:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.RandomCrossOver</Class>
 *      <Probability>value</Probability>
 * </Operator>
 * }
 * </pre>
 *
 * Where probability is a double value in the range [0, 100] which represents the probability of crossover.
 * If this does not appear in the configuration, the parameter is set to the default values. <p>
 *
 * Default values: <p>
 * <ul>
 * <li>Probability is set to 60.0</li>
 * </ul>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class RandomCrossOver extends CrossOverOperator {

    private int points = 1;

    public RandomCrossOver() {
    }

    public RandomCrossOver(int probability, int points) {

        super(probability);
        this.points = points;

    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        if (conf.containsKey("Points")) {
            this.points = conf.getInt("Points");
        } else {
            ConfWarning w = new ConfWarning("Points", this.points);
            w.warn();
        }
    }

    @Override
    protected List<Individual> crossOver(EvolutionaryAlgorithm ea, List<Individual> individuals) {

        int k, init, end, chromSize;
        List<Individual> children;
        int[] crossPoints;
        double[] genes1, genes2;
        double auxGene;

        init = 0;
        end = 0;
        k = 0;

        genes1 = individuals.get(0).getChromosomeAt(0);
        genes2 = individuals.get(1).getChromosomeAt(0);


        crossPoints = super.getCrossPoints(true, this.points,
                individuals.get(0).getChromosomeAt(0).length);


        //Solo los impares:
        for (int i = 1; i < crossPoints.length - 1; i = i + 2) {

            init = crossPoints[i];
            end = crossPoints[i + 1];

            for (int j = init; j < end; j++) {

                auxGene = genes1[j];
                genes1[j] = genes2[j];
                genes2[j] = auxGene;

            }


        }

        individuals.get(0).setChromosomeAt(0, genes1);
        individuals.get(1).setChromosomeAt(0, genes2);
        //Despues de cruzar:
        return individuals;

    }
}
