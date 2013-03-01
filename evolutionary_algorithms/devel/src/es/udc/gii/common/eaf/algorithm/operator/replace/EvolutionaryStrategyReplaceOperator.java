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
package es.udc.gii.common.eaf.algorithm.operator.replace;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryStrategy;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the (1+&lambda;) replace operator for the {@see EvolutionaryStrategy} class. 
 * In this class, &lambda; children are generated from each parent and in the replace stage all the
 * &lambda; children and their <i>father</i> compete to survive.<p>
 *
 * This operator does not need configuration.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EvolutionaryStrategyReplaceOperator extends ReplaceOperator {

    @Override
    protected List<Individual> replace(
            EvolutionaryAlgorithm algorithm, List<Individual> toPopulation) {
        //Los hijos están en las lista "toPopulation" y los padres en 
        //"algorithm.getPopulation", si hijo peor que padre se copia el padre:

        Individual parent;
        List<Individual> aux_list, aux_replace_list;
        BestIndividualSpecification bestSpecification =
                new BestIndividualSpecification();
        int lambda;

        lambda = ((EvolutionaryStrategy) algorithm).getLambda();
        aux_replace_list = new ArrayList<Individual>();

        for (int i = 0; i < algorithm.getPopulation().getSize(); i++) {

            //Se usa ( 0 --> lambda) porque con sublist se eliminan los elemnentos
            //que se obtienen de la lista toPopulation
            aux_list = toPopulation.subList(0, lambda);

            parent = algorithm.getPopulation().getIndividual(i);
            aux_list.add(parent);

            aux_replace_list.add((Individual) bestSpecification.get(aux_list, 1,
                    algorithm.getComparator()).get(0).clone());
            aux_list.clear();
        }

        return aux_replace_list;
    }
}
