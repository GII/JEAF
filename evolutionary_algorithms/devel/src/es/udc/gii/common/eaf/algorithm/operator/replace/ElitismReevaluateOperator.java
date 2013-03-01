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
 * ElitismOperator.java
 *
 * Created on 30 de noviembre de 2006, 18:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.replace;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.WorstIndividualSpecification;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This operator implements a replace operator with elitism. From the 
 * <i>i-population</i> the operator choose the <i>elitism</i> best individuals
 * and change it from the <i>elitism</i> worst individuals of the 
 * <i>(i+1)-population</i>. So the parameter <i>elitism</i> indicates the number
 * of individuals that survive to the next generation.<p>
 * 
 * The survived individuals are re-evaluated.<p>
 * 
 * The code to config this operator is:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.replace.ElitismReevaluateOperator</Class>
 *      <Elitism>value</Elitism>
 * </Operator>
 * }
 * </pre>
 *
 * Where Elitism is an integer that indicates the number of individuals that survive from one
 * generation to the next one. If this parameter does not appear in the configuration, it is set
 * to its default value. <p>
 *
 * Default values:<p>
 * <ul>
 * <li> Elitism is set to 1</li>
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ElitismReevaluateOperator extends ReplaceOperator {

    private int elitism = 1;

    /** Creates a new instance of ElitismOperator */
    public ElitismReevaluateOperator() {
    }

    public ElitismReevaluateOperator(int elitism) {
        this.elitism = elitism;
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        if (conf.containsKey("Elitism")) {
            this.elitism = conf.getInt("Elitism");
        } else {
            ConfWarning w = new ConfWarning("Elitism", this.elitism);
            w.warn();
        }
    }

    @Override
    protected List<Individual> replace(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation) {

        //Borro los "n" peores de la poblacion de destino:
        List<Individual> fromPopulation =
                algorithm.getPopulation().getIndividuals();
        List<Individual> betterList;

        //Reemplazo:
        //Borro los peores de esta generacion:
        List<Individual> worstList = IndividualsProductTrader.get(new WorstIndividualSpecification(),
                toPopulation, this.elitism, algorithm.getComparator());

        for (int i = 0; i < worstList.size(); i++) {
            toPopulation.remove(worstList.get(i));
        }

        //A�ado los mejores de la generacion anterior:
        betterList = IndividualsProductTrader.get(
                new BestIndividualSpecification(),
                fromPopulation, this.elitism, algorithm.getComparator());

        //Se reevaluan los mejores individuos:
        algorithm.getEvaluationStrategy().evaluate(algorithm, betterList,
                algorithm.getProblem().getObjectiveFunctions(),
                algorithm.getProblem().getConstraints());
        algorithm.setFEs(algorithm.getFEs() + this.elitism);

        for (int i = 0; i < betterList.size(); i++) {
            toPopulation.add((Individual) betterList.get(i).clone());
        }

        return toPopulation;

    }
}
