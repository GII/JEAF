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
public class TargetPopulation {

    private static TargetPopulation instance;
    private List<Individual> targetPopulation;

    private TargetPopulation() {
        this.targetPopulation = new ArrayList<Individual>();
    }

    public static TargetPopulation getInstance() {
        if (instance == null) {
            instance = new TargetPopulation();
        }
        return instance;
    }

    public List<Individual> getTargetPopulation(int numberOfIndividuals) {

        while (targetPopulation.size() < numberOfIndividuals) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(TargetPopulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        synchronized (targetPopulation) {

            try {

                List<Individual> returnedPopulation = new ArrayList<Individual>();

                if (targetPopulation.size() < numberOfIndividuals) {
                    numberOfIndividuals = targetPopulation.size();
                }

                if (numberOfIndividuals > 0) {
                    for (int i = 0; i < numberOfIndividuals; i++) {
                        if (targetPopulation.get(i) != null) {
                            returnedPopulation.add((Individual) targetPopulation.get(i).clone());

                            targetPopulation.set(i, null);
                        }

                    }
                    //Clean-up target population:
                    if (targetPopulation != null && !targetPopulation.isEmpty()) {
                        targetPopulation.removeAll(Collections.singleton(null));
                    }

                }
                return returnedPopulation;

            } catch (NullPointerException ex) {
                System.out.println();

            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println();
            }
            return null;
        }

    }

    /**
     * Método encargado de inicializar la población objetivo para nuevas
     * ejecuciones del algoritmo.
     */
    public void reset() {
        instance = null;
    }

    public void setTargetPopulation(List<Individual> individuals) {

        synchronized (targetPopulation) {
            this.targetPopulation = new ArrayList<Individual>();
            for (Individual i : individuals) {
                this.targetPopulation.add((Individual) i.clone());
            }
        }

    }
}
