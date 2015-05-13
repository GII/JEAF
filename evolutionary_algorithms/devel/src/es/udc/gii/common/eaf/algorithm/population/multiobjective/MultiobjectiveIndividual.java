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
package es.udc.gii.common.eaf.algorithm.population.multiobjective;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.math.util.DoubleArray;

/**
 *
 * @author pilar
 */
public class MultiobjectiveIndividual extends Individual {
    /**
     * List of individuals dominated by this instance.
     */
    private List<MultiobjectiveIndividual> dominatedIndividuals;
    /**
     * Number of individuals that dominate this instance.
     */
    private int dominationCount;
    /**
     * Rank of this instance, i.e. the number of the Pareto-front to which this
     * instance belongs. Lesser values are better.
     */
    private int rank;

    public MultiobjectiveIndividual() {
        super();
        initialize();
    }

    public MultiobjectiveIndividual(Map<Integer, double[]> chromosomes) {
        super(chromosomes);
        initialize();
    }

    /**
     * Adds an individual to the list of dominated individuals by this instance.
     * @param ind - Individual dominated by this instance.
     */
    public void addDominatedIndividual(MultiobjectiveIndividual ind) {
        this.dominatedIndividuals.add(ind);
    }

    /**
     * Removes all individuals from the dominated individuals' list.
     */
    public void clearDominatedIndividuals() {
        if (this.dominatedIndividuals != null) {
            this.dominatedIndividuals.clear();
        }
    }

    /**
     * Clones this instance.
     * @return A new instance of this class containing the same state as this
     * instance.
     */
    @Override
    public Object clone() {
        /* Clone the state. */
        MultiobjectiveIndividual clone = (MultiobjectiveIndividual) super.clone();
        /* Clone the dominated individuals list.
         * NOTE: The list is cloned, but not
         * each individual in the list !!
         */
        List<MultiobjectiveIndividual> clonedDominatedIndividuals = new ArrayList<MultiobjectiveIndividual>(this.dominatedIndividuals.size());
        clonedDominatedIndividuals.addAll(this.dominatedIndividuals);
        clone.setDominatedIndividuals(clonedDominatedIndividuals);
        return clone;
    }

    /**
     * Decreases the count of individuals that dominate this instance by one.
     * It is ensured that the domination count is always greather or equal cero.
     */
    public void decreaseDominationCount() {
        if (!(this.dominationCount - 1 < 0)) {
            this.dominationCount--;
        }
    }

    /**
     * Decreases the domination count of each individual dominated by this
     * instance by one.
     */
    public void decreaseDominationCountOfDominatedIndividuals() {
        if (!this.dominatedIndividuals.isEmpty()) {
            for (MultiobjectiveIndividual ind : this.dominatedIndividuals) {
                ind.decreaseDominationCount();
            }
        }
    }

    /**
     * Clears the list of dominated individuals by this individual.
     */
    public void emptyDominatedIndividualsList() {
        if (!(this.dominatedIndividuals.isEmpty())) {
            this.dominatedIndividuals.clear();
        }
    }

    /**
     * Returns the list of dominated individuals by this instance.
     */
    public List<MultiobjectiveIndividual> getDominatedIndividuals() {
        return this.dominatedIndividuals;
    }

    /**
     * @return The number of individuals of the current population that dominate
     * this individual.
     */
    public int getDominationCount() {
        return this.dominationCount;
    }

    /**
     * @return The rank of this instance. I.e. it returns the number of the
     * Pareto-front to which this instance belongs. Lesser values are better.
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Increases the count of individuals that dominate this instance by one.
     */
    public void increaseDominationCount() {
        this.dominationCount++;
    }

    /**
     * Initialized this instance.
     */
    protected void initialize() {
        this.dominatedIndividuals = new ArrayList<MultiobjectiveIndividual>();
        this.rank = Integer.MAX_VALUE;
        this.dominationCount = 0;
    }

    /**
     * Sets the list of individuals dominated by this instance.
     * @param dominatedIndividuals - A list containing individuals dominated by
     * this instance.
     */
    public void setDominatedIndividuals(List<MultiobjectiveIndividual> dominatedIndividuals) {
        this.dominatedIndividuals = dominatedIndividuals;
    }

    /**
     * Sets the number of individuals that dominated this instance.
     * @param dominationCount - Number of individuals that dominated this instance.
     */
    public void setDominationCount(int dominationCount) {
        this.dominationCount = dominationCount;
    }

    /**
     * Sets the rank of this individual.
     * @param rank - The rank of the individual, i.e. the number of the
     * Pareto-front to which this instance belongs.
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        String ret = "";
        String chrom = "chromosomes: ";
        String fit = "fitness: " + getFitness();
        String obj = getObjectives() != null ? "obj: " + getObjectives().toString() : "?";
        String constr = getConstraints() != null ? "constr: " + getConstraints().toString() : "?";
        String r = "rank: " + getRank();
        if (getChromosomes() != null) {
            chrom += "[";
            Map<Integer, double[]> c = getChromosomes();
            for (int i = 0; i < c.size(); i++) {
                chrom += " ( ";
                for (int j = 0; j < c.get(i).length - 1; j++) {
                    chrom += c.get(i)[j] + ", ";
                }
                chrom += c.get(i)[c.get(i).length - 1];
                chrom += " )";
            }
            chrom += " ]";
        } else {
            chrom += "[ ? ]";
        }
        ret += chrom + " - " + fit + " - " + obj + " - " + constr + " - " + r;
        return ret;
    }
    
}
