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

package es.udc.gii.common.eaf.algorithm.operator.replace.jade;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryStrategy;
import es.udc.gii.common.eaf.algorithm.operator.replace.ReplaceOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.jade.JADEIndividual;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a replace operator to be used in the JADE algorithm. This replace operator
 * uses JADEIndividual as the Individual class and after the replace stage reset the values of F and
 * CR of the individuals that are not selected to survive.<p>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0.
 */
public class JADEEvolutionaryStrategyReplaceOperator extends ReplaceOperator {

    @Override
    protected List<Individual> replace(
            EvolutionaryAlgorithm algorithm, List<Individual> toPopulation) {
        //Los hijos están en las lista "toPopulation" y los padres en
        //"algorithm.getPopulation", si hijo peor que padre se copia el padre:

        Individual parent, best_children;
        List<Individual> aux_list, aux_replace_list;
        BestIndividualSpecification bestSpecification =
                new BestIndividualSpecification();
        int lambda;

        lambda = ((EvolutionaryStrategy) algorithm).getLambda();
        aux_replace_list = new ArrayList<Individual>();

        for (int i = 0; i < algorithm.getPopulation().getSize(); i++) {

            //Se usa (0 --> lambda) porque con sublist se eliminan los elemnentos
            //que se obtienen de la lista toPopulation
            aux_list = toPopulation.subList(0, lambda);

            parent = algorithm.getPopulation().getIndividual(i);
            aux_list.add(parent);


            best_children = (Individual) bestSpecification.get(aux_list, 1,
                    algorithm.getComparator()).get(0).clone();

            //Cuando el padre sigue siendo mejor que el hijo, no se tienen el cuenta sus valores
            //de F y CR para la distribución por eso se borran:
            if (algorithm.getComparator().compare(parent, best_children) <= 0) {
                ((JADEIndividual)best_children).setCR(-Double.MAX_VALUE);
                ((JADEIndividual)best_children).setF(-Double.MAX_VALUE);
            } else {
                //Si el hijo es mejor que el padre y hay archivo --> se guarda el padre en el archivo:
                if (algorithm.getPopulation().getArchive() != null) {
                    algorithm.getPopulation().getArchive().add((JADEIndividual)parent.clone());
                }
            }

            aux_replace_list.add(best_children);
            aux_list.clear();
        }

        //Si la población tiene archivo y supera el tamaño máximo = NP (tamaño de la población)
        //borramos aleatoriamente elementos del archivo:
//        int random_pos;
//        while (algorithm.getPopulation().getArchive().size() > 
//                algorithm.getPopulation().getSize()) {
//                random_pos = EAFRandom.nextInt(algorithm.getPopulation().getArchive().size());
//                algorithm.getPopulation().getArchive().remove(random_pos);
//        }

        return aux_replace_list;


    }
}
