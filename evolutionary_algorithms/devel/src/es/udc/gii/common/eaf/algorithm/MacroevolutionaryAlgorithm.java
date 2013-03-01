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
 * MacroevolutionaryAlgorithm.java
 *
 * Created on 18 de diciembre de 2006, 17:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.MaIndividual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.exception.WrongIndividualException;
import es.udc.gii.common.eaf.problem.Problem;
import java.util.ArrayList;
import java.util.List;
import no.uib.cipr.matrix.UpperTriangPackMatrix;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MacroevolutionaryAlgorithm extends EvolutionaryAlgorithm {

    private UpperTriangPackMatrix W;

    public MacroevolutionaryAlgorithm() {
    }

    public double getWxy(int x, int y) {
        return this.W.get(x, y);
    }

    public void setWxy(int x, int y, double value) {
        this.W.set(x, y, value);
    }

    @Override
    protected void init() {
        super.init();
        this.W = new UpperTriangPackMatrix(super.getPopulation().getSize());
    }

    @Override
    protected void select(Population population) {
        super.getSelectionChain().execute(this, super.getPopulation());
    }

    @Override
    protected void reproduce(Population population) {
        super.getReproductionChain().execute(this, super.getPopulation());
    }

    @Override
    protected void evaluate(Problem problem, Population population) {
        List<Individual> individuals = super.getPopulation().getIndividuals();
        List<Individual> extincts = new ArrayList<Individual>(individuals.size() / 2);

        for (Individual individual : individuals) {
            if (!((MaIndividual) individual).isSurvivor()) {
                extincts.add(individual);
            }
        }
        super.evaluate(problem, new Population(extincts));
    }

    @Override
    protected void replace(Population population) {
    }

    @Override
    public void setPopulation(Population population) {
        Individual individual = population.getIndividual(0);

        if (individual instanceof MaIndividual) {
            super.setPopulation(population);
        } else {
            throw new WrongIndividualException(MaIndividual.class, individual.getClass());
        }
    }

    @Override
    public String getAlgorithmID() {
        return "MA";
    }
}
