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
package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.mutationStrategy;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.jade.JADEIndividual;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * The mutation operator of the Differential Evolution Algorithm use different
 * mutation strategies to create the individuals of the population. This class
 * implements the Current-to-<i>p</i>best strategy. Following this strategy a
 * new mutation vector is generated as follows:
 * <p>
 *
 * v<sub>i,g</sub> = x<sub>i,g</sub> + F&sdot;(x<sup>p</sup><sub>best,g</sub> -
 * x<sub>i,g</sub>) + F&sdot;(x<sub>r1,g</sub> - x<sub>r2,g</sub>)
 * <p>
 *
 * where x<sup>p</sup><sub>best,g</sub> is randomly chosen as one of the
 * 100<i>p</i>% individuals in the current population with p &isin;(0, 1]. This
 * strategy is a generalization of current-to-best strategy. Any of the top
 * 100<i>p</i>% solution can be randomly chosen to play the role of tje single
 * best solution in current-to-best.<p>
 *
 * To configure this mutation strategy the xml code should be:
 *
 * <pre>
 * {@code
 * <MutationStrategy>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.mutationStrategy.CurrentToPBestMutationStrategy</Class>
 *      <p>value</p>
 *      <F>value</F>
 *      <diffVector>value</diffVector>
 * <MutationStrategy>
 * }
 * </pre>
 *
 * where <i>p</i> is the value of the parameter that control the greediness of
 * the mutation strategy given as a fraction of unity. <i>F</i> and
 * <i>diffVector</i> are parameters inherit from the DEMutationStrategy class.
 * If some of the parameters do not appear in the configuration, they are set to
 * their default values.<p>
 *
 * Default values:
 * <ul>
 * <li>p = 0.05</li>
 * <li>F as a constant parameter with value 0.5</li>
 * <li>diffVector = 1 </li>
 * </ul>
 *
 * This mutation strategy was first presented in "JADE: Adaptive Differential
 * Evolution with Optional External Archive", Jinqiao Zhang amd Arthur C.
 * Sanderson, IEEE Transacions on Evolutionary Computation, Vol. 13, No. 5,
 * October 2009.
 * <p>
 *
 * @author Grupo Integrado de Ingeniería
 * (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0.
 */
public class CurrentToPBestMutationStrategy extends DEMutationStrategy {

    /**
     * Parameter which controls the greediness of the mutation strategy. The
     * authors recommend p &isin; [5%, 20%].
     */
    private double p = 0.05;

    /**
     *
     */
    private boolean archive = false;

    @Override
    public Individual getMutatedIndividual(EvolutionaryAlgorithm algorithm, Individual target) {

        BestIndividualSpecification bestSpec
                = new BestIndividualSpecification();
        List<Individual> individuals, best_p_individuals, listInd;
        List<Integer> index_list;
        Individual best_p;
        double[] base;
        int n, random_p_pos, randomPos;
        double auxGeneValue, x1, x2, x3, x4;
        double F;

        individuals = new ArrayList<>();
        individuals.addAll(algorithm.getPopulation().getIndividuals());

        //Si hay archivo y no está creado se crea:
        if (this.archive && algorithm.getPopulation().getArchive() == null) {
            algorithm.getPopulation().setArchive(new ArrayList<Individual>());
        }

        //Si hay archivo --> se añaden los elementos del archivo:
        if (this.archive) {
            individuals.addAll(algorithm.getPopulation().getArchive());
        }

        base = ((Individual) target).getChromosomeAt(0);

        //Se escoge x_p_best = un aleatorio de los 100*p% individuos de la poblacion:
        n = (int) (this.p * individuals.size());
        best_p_individuals = bestSpec.get(individuals, n, algorithm.getComparator());
        random_p_pos = (int) Math.floor(EAFRandom.nextDouble() * best_p_individuals.size());
        best_p = best_p_individuals.get(random_p_pos);

        F = this.getFPlugin().get(algorithm);

        //se eligen los vectores diferenciales:
        listInd = new ArrayList<>();

        index_list = new ArrayList<>();
        index_list.add(individuals.indexOf(target));

        for (int i = 0; i < this.getDiffVector() * 2; i++) {

            do {

                //Si es una posición par 0, 2, ... es decir, el primer elemento de la resta,
                //Se elige de la posición de individuos de la población sin archivo:
                if (!archive || i % 2 == 0) {
                    randomPos = EAFRandom.nextInt(algorithm.getPopulation().getSize());
                } else {
                    randomPos = EAFRandom.nextInt(individuals.size());
                }

            } while (index_list.contains(randomPos));

            listInd.add(individuals.get(randomPos));
            index_list.add(randomPos);

        }

        if (base != null) {
            //Recorremos el numero de genes:

            for (int i = 0; i < base.length; i++) {

                auxGeneValue = base[i];

                for (int j = 0; j < this.getDiffVector(); j += 2) {

                    x1 = listInd.get(j).getChromosomeAt(0)[i];
                    x2 = listInd.get(j + 1).getChromosomeAt(0)[i];

                    auxGeneValue += F * (x1 - x2);

                }

                x3 = best_p.getChromosomeAt(0)[i];
                x4 = base[i];

                auxGeneValue += F * (x3 - x4);

                base[i] = auxGeneValue;

            }

        }

        Individual mutatedIndividual = new Individual();
        mutatedIndividual.setChromosomeAt(0, base);

        return mutatedIndividual;

    }

    @Override
    public void configure(Configuration conf) {

        ConfWarning w = null;

        super.configure(conf);
        if (conf.containsKey("p")) {
            this.p = conf.getDouble("p");
        } else {
            w = new ConfWarning(this.getClass().getSimpleName()
                    + ".p", this.p);

        }

        if (conf.containsKey("archive")) {
            this.archive = conf.getBoolean("archive");
        } else {
            w = new ConfWarning(this.getClass().getSimpleName()
                    + ".archive", Boolean.toString(this.archive));

        }

        if (w != null) {
            w.warn();
        }

    }
}
