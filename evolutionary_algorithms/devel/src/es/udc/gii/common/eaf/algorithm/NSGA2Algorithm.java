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
 * NSGA2Algorithm.java
 *
 * Created on 20 de diciembre de 2006, 17:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.NSGA2Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.exception.WrongIndividualException;
import es.udc.gii.common.eaf.plugin.multiobjective.NSGA2Ranking;
import es.udc.gii.common.eaf.plugin.multiobjective.crowding.Crowding;
import es.udc.gii.common.eaf.plugin.multiobjective.crowding.ObjectiveSpaceCrowding;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements the NSGA2 algorithm. It's an algorithm for 
 * multiobjective problems.<p>
 * 
 * The crowding and ranking might be configured by the user 
 * (see {@link Crowding} and {@link NSGA2Ranking}). If nothing is configured,
 * then {@link ObjectiveSpaceCrowding} for crowding and {@link NSGA2Ranking} for
 * ranking are used as defaults.<p/>
 *
 * Configuration example:
 * <pre>
 * &lt;Crowding&gt;
 *    &lt;Class&gt; ... &lt;/Class&gt;
 *    ...
 * &lt;/Crowding&gt;
 * &lt;Ranking&gt;
 *    &lt;Class&gt; ... &lt;/Class&gt;
 *    ...
 * &lt;/Ranking&gt;
 * </pre>
 *
 * {@code Crowding} must be an instance of {@link Crowding} and {@code Ranking}
 * must be and instance of {@link NSGA2Ranking}.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class NSGA2Algorithm extends EvolutionaryAlgorithm {

    private Crowding crowding = null;
    private NSGA2Ranking ranking = null;

    public NSGA2Algorithm() {
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        try {
            if (conf.containsKey("Crowding.Class")) {
                this.setCrowding((Crowding) Class.forName(conf.getString("Crowding.Class")).newInstance());
                this.getCrowding().configure(conf);
            } else {
                this.setCrowding(new ObjectiveSpaceCrowding());
                (new ConfWarning("Crowding", "ObjectiveSpaceCrowding")).warn();
            }

            if (conf.containsKey("Ranking.Class")) {
                this.setRanking((NSGA2Ranking) Class.forName(conf.getString("Ranking.Class")).newInstance());
                this.getRanking().configure(conf);
            } else {
                this.setRanking(new NSGA2Ranking());
                (new ConfWarning("Ranking", "NSGA2Ranking")).warn();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void evaluate(Problem problem, Population population) {
        super.evaluate(problem, population);

        /* If we are evaluating the initial population, we have to asign
         * the ranks and the crowding distances. */
        if (this.state == INIT_EVALUATE_STATE) {
            List<Individual> pop = population.getIndividuals();

            getRanking().calculate(pop);

            List<NSGA2Individual> popNSGA2 =
                    new ArrayList<NSGA2Individual>(pop.size());

            for (Individual ind : pop) {
                assert ind instanceof NSGA2Individual :
                        "NSGA2Algorithm: Not a NSGA2Individual";
                popNSGA2.add((NSGA2Individual) ind);
            }

            getCrowding().calculate(popNSGA2);
        }
    }

    @Override
    public void setPopulation(Population population) {
        Individual individual = population.getIndividual(0);

        if (individual instanceof NSGA2Individual) {
            super.setPopulation(population);
        } else {
            throw new WrongIndividualException(NSGA2Individual.class,
                    individual.getClass());
        }
    }

    @Override
    public String getAlgorithmID() {
        return "NSGA2";
    }

    /**
     * @return The crowding plugin used for computing the crowding distances.
     */
    public Crowding getCrowding() {
        return crowding;
    }

    /**
     * Sets the crowding plugin for computing the crowding distances.
     * @param crowding The new crowding plugin.
     */
    public void setCrowding(Crowding crowding) {
        this.crowding = crowding;
    }

    /**
     * @return The plugin used for calculating the rank of each individual.
     */
    public NSGA2Ranking getRanking() {
        return ranking;
    }

    /**
     * Sets the plugin used for calculating the rank of each individual.
     * @param ranking The new ranking plugin.
     */
    public void setRanking(NSGA2Ranking ranking) {
        this.ranking = ranking;
    }
}
