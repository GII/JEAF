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
 * MOPReplaceOperator.java
 *
 * Created on 21 de diciembre de 2006, 16:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.replace;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.NSGA2Algorithm;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.MultiobjectiveIndividual;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.NSGA2Individual;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.exception.WrongAlgorithmException;
import es.udc.gii.common.eaf.plugin.multiobjective.remainingfront.NSGA2RemainingFrontSelection;
import es.udc.gii.common.eaf.plugin.multiobjective.remainingfront.RemainingFrontSelectionPlugin;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * The replace operator for the {@link NSGA2Algorithm}.
 *
 * @author Grupo Integrado de Ingeniería (<a
 * href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class NSGA2ReplaceOperator extends ReplaceOperator {

    private RemainingFrontSelectionPlugin remainingFrontPlugin =
            new NSGA2RemainingFrontSelection();

    /**
     * Creates a new instance of MOPReplaceOperator
     */
    public NSGA2ReplaceOperator() {
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);

        try {
            if (conf.containsKey("RemainingFrontSelectionPlugin.Class")) {
                this.remainingFrontPlugin = (RemainingFrontSelectionPlugin) Class.forName(
                        conf.getString("RemainingFrontSelectionPlugin.Class")).newInstance();
                this.remainingFrontPlugin.configure(conf.subset("RemainingFrontSelectionPlugin"));
            } else {
                ConfWarning w = new ConfWarning("RemainingFrontSelectionPlugin",
                        this.getClass().getSimpleName());
                w.warn();
            }

        } catch (Exception ex) {
            throw new ConfigurationException(this.getClass(), ex);
        }

    }

    @Override
    protected List<Individual> replace(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation) {

        if (!(algorithm instanceof NSGA2Algorithm)) {
            throw new WrongAlgorithmException(NSGA2Algorithm.class,
                    algorithm.getClass());
        }

        NSGA2Algorithm nsga2 = (NSGA2Algorithm) algorithm;

        int popSize = nsga2.getPopulation().getSize();
        List<Individual> newPopulation = new ArrayList<Individual>(popSize);
        List<Individual> wholePop = new ArrayList<Individual>(2 * popSize);

        wholePop.addAll(nsga2.getPopulation().getIndividuals());
        wholePop.addAll(toPopulation);

        
        //22 - 08 - 2013 : Los parámetros no se calculan aquí, se calculan en el "remaining"
        //por efinicienia, dependiendo de la implementación los parámetros se calculan para
        //toda o parte de la población (por ej., en el NSGA2 sólo se calcula el crowding para
        //el último frente).
        //nsga2.getParametersPlugin().calculateParameters(wholePop);
        int nFronts = nsga2.getRanking().calculate(wholePop);

        List<List<Individual>> fronts = getFronts(wholePop, nFronts);

        for (int f = 0; (f < nFronts) && (newPopulation.size() < popSize); f++) {
            List<Individual> front = fronts.get(f);
            
            if (fronts.get(f).size() <= (popSize - newPopulation.size())) {
                newPopulation.addAll(front);
            } else {
                //10-06-2013: The RemainingFrontSelectionPlugin were added to JEAF
                //with the aim of providing an interface which subclases implements
                //the necessary methods to select individuals of the last front to
                //be part of the new population. Different methods could be implemented,
                //for that reason we have created an interface.
                remainingFrontPlugin.selection(algorithm, 
                        newPopulation, front, popSize - newPopulation.size());
            }
        }

        return newPopulation;
    }

    private List<List<Individual>> getFronts(List<Individual> pop, int nFronts) {

        List<List<Individual>> fronts = new ArrayList<List<Individual>>(nFronts);

        for (int i = 0; i < nFronts; i++) {
            fronts.add(new ArrayList<Individual>());
        }

        for (Individual ind : pop) {
            fronts.get(((MultiobjectiveIndividual) ind).getRank()).add((MultiobjectiveIndividual) ind);
        }

        return fronts;
    }

    @Override
    public String toString() {
        return "NSGA2 Replace Operator";
    }
}
