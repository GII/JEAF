/*
 * Copyright (C) 2013 pilar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.common.eaf.algorithm.csea.population;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pilar
 */
public class EvaluatedPopulation {

    private static EvaluatedPopulation instance;
    private List<Individual> evaluatedPopulation = new ArrayList<Individual>();

    private int maxNumberOfIndividuals;

    private EvaluatedPopulation() {
    }

    public static EvaluatedPopulation getInstance() {
        if (instance == null) {
            instance = new EvaluatedPopulation();
        }
        return instance;
    }

    public void addEvaluatedIndividual(Individual evaluatedIndividual) throws InterruptedException {

        while (evaluatedPopulation.size() >= maxNumberOfIndividuals) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(TargetPopulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        synchronized (evaluatedPopulation) {
            evaluatedPopulation.add((Individual) evaluatedIndividual.clone());
        }

    }

    public List<Individual> getEvaluatedPopulation(int numberOfIndividuals) {

        while (evaluatedPopulation.size() < numberOfIndividuals) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(TargetPopulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        synchronized (evaluatedPopulation) {
            try {
                List<Individual> returnedPopulation = new ArrayList<Individual>();

                if (evaluatedPopulation.size() < numberOfIndividuals) {
                    numberOfIndividuals = evaluatedPopulation.size();
                }
                if (numberOfIndividuals > 0) {

                    for (int i = 0; i < numberOfIndividuals; i++) {
                        if (evaluatedPopulation.get(i) != null) {
                            returnedPopulation.add((Individual) evaluatedPopulation.get(i).clone());
                            evaluatedPopulation.set(i, null);
                        }

                    }

                    //Clean-up evaluated population:
                    if (evaluatedPopulation != null && !evaluatedPopulation.isEmpty()) {
                        evaluatedPopulation.removeAll(Collections.singleton(null));
                    }

                }
                return returnedPopulation;
            } catch (NullPointerException ex) {
                System.out.println();

                return null;
            }

        }

    }

    public void reset() {
        this.evaluatedPopulation.removeAll(evaluatedPopulation);
        this.evaluatedPopulation = null;
        this.instance = null;
    }

    public void setMaximumSize(int size) {
        this.maxNumberOfIndividuals = size;
    }
}
