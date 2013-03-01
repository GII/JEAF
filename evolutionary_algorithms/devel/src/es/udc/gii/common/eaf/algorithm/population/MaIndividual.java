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
package es.udc.gii.common.eaf.algorithm.population;

import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.MaximizingFitnessComparator;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.MinimizingFitnessComparator;
import es.udc.gii.common.eaf.exception.WrongComparatorException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class represents an individual for a macroevolutionary algorithm.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MaIndividual extends Individual {

    private static final long serialVersionUID = -6655436865465687929L;
    private double bestFitness;
    private boolean survivor;

    public MaIndividual() {
        survivor = false;
    }

    @Override
    public Object clone() {
        MaIndividual clone = null;

        clone = (MaIndividual) super.clone();
        clone.setBestFitness(this.bestFitness);
        clone.setSurvivor(this.survivor);
        return clone;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        super.readExternal(in);
        setBestFitness(in.readDouble());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeDouble(getBestFitness());
    }

    @Override
    public void setFitness(double fitness) {
        super.setFitness(fitness);
        if (getComparator() instanceof MaximizingFitnessComparator) {
            if (fitness > this.bestFitness) {
                this.bestFitness = fitness;
            }
        } else if (getComparator() instanceof MinimizingFitnessComparator) {
            if (fitness < this.bestFitness) {
                this.bestFitness = fitness;
            }
        } else {
            if (getComparator() != null) {
                throw new WrongComparatorException(getComparator().getClass());
            } else {
                throw new WrongComparatorException("null");
            }
        }
    }

    /**
     * @return The best fitness achived by this individual.
     */
    public double getBestFitness() {
        return this.bestFitness;
    }

    /**
     * Sets the best fitness achived by this individual.
     * @param fitness The new best fitness.
     */
    public void setBestFitness(double fitness) {
        this.bestFitness = fitness;
    }

    /**
     * @return {@code true} if this individual is a survivor.
     * {@code false} otherwise.
     */
    public boolean isSurvivor() {
        return this.survivor;
    }

    /**
     * Sets if this individual is a survivor.
     * @param survivor {@code true} if this individual is a survivor.
     * {@code false} otherwise.
     */
    public void setSurvivor(boolean survivor) {
        this.survivor = survivor;
    }

    @Override
    public void generate() {
        super.generate();
        this.survivor = false;
    }

    @Override
    public void setComparator(FitnessComparator<Individual> comparator) {
        super.setComparator(comparator);
        if (comparator instanceof MinimizingFitnessComparator) {
            this.bestFitness = Double.MAX_VALUE;
        } else if (comparator instanceof MaximizingFitnessComparator) {
            this.bestFitness = -Double.MAX_VALUE;
        } else {
            if (comparator != null) {
                throw new WrongComparatorException(getComparator().getClass());
            } else {
                throw new WrongComparatorException("null");
            }
        }
    }

    @Override
    public void copyEvalResults(Individual other) {
        super.copyEvalResults(other);
        if (other instanceof MaIndividual) {
            ((MaIndividual) other).setBestFitness(getBestFitness());
        }
    }
}
