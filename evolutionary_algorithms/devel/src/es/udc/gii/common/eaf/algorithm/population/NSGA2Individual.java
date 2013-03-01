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
 * NSGA2Individual.java
 *
 * Created on 14 de diciembre de 2006, 17:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.population;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math.util.DoubleArray;

/**
 * This class represents an individual for the NSGA2 algorithm.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class NSGA2Individual extends Individual {

    /**
     * List of individuals dominated by this instance.
     */
    private List<NSGA2Individual> dominatedIndividuals;
    /**
     * Number of individuals that dominate this instance.
     */
    private int dominationCount;
    /**
     * Rank of this instance, i.e. the number of the Pareto-front to which this
     * instance belongs. Lesser values are better.
     */
    private int rank;
    /**
     * Crowding distance value. Greater values are better.
     */
    private double crowdingDistance;

    public NSGA2Individual() {
        super();
        initialize();
    }

    public NSGA2Individual(DoubleArray[] chromosomes) {
        super(chromosomes);
        initialize();
    }

    /**
     * Initialized this instance.
     */
    private void initialize() {
        this.dominatedIndividuals = new ArrayList<NSGA2Individual>();
        this.rank = Integer.MAX_VALUE;
        this.dominationCount = 0;
        this.crowdingDistance = 0;
    }

    /**
     * Returns the list of dominated individuals by this instance.
     */
    public List<NSGA2Individual> getDominatedIndividuals() {
        return this.dominatedIndividuals;
    }

    /**
     * Adds an individual to the list of dominated individuals by this instance.
     * @param ind - Individual dominated by this instance.
     */
    public void addDominatedIndividual(NSGA2Individual ind) {
        this.dominatedIndividuals.add(ind);
    }

    /**
     * Sets the list of individuals dominated by this instance.
     * @param dominatedIndividuals - A list containing individuals dominated by
     * this instance.
     */
    public void setDominatedIndividuals(List<NSGA2Individual> dominatedIndividuals) {
        this.dominatedIndividuals = dominatedIndividuals;
    }

    /**
     * Removes all individuals from the dominated individuals' list.
     */
    public void clearDominatedIndividuals() {
        if(this.dominatedIndividuals != null) {
            this.dominatedIndividuals.clear();
        }
    }
    
    /**
     * @return The rank of this instance. I.e. it returns the number of the
     * Pareto-front to which this instance belongs. Lesser values are better.
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Sets the rank of this individual.
     * @param rank - The rank of the individual, i.e. the number of the
     * Pareto-front to which this instance belongs.
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return The number of individuals of the current population that dominate
     * this individual.
     */
    public int getDominationCount() {
        return this.dominationCount;
    }

    /**
     * Sets the number of individuals that dominated this instance.
     * @param dominationCount - Number of individuals that dominated this instance.
     */
    public void setDominationCount(int dominationCount) {
        this.dominationCount = dominationCount;
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
     * Increases the count of individuals that dominate this instance by one.
     */
    public void increaseDominationCount() {
        this.dominationCount++;
    }

    /**
     * Decreases the domination count of each individual dominated by this
     * instance by one.
     */
    public void decreaseDominationCountOfDominatedIndividuals() {
        if (!this.dominatedIndividuals.isEmpty()) {
            for (NSGA2Individual ind : this.dominatedIndividuals) {
                ind.decreaseDominationCount();
            }
        }
    }

    /**
     * Increases the crowding distance by an specified amount.
     * @param increase - Amount to increase the crowding distance.
     */
    public void increaseCrowdingDistance(double increase) {
        this.crowdingDistance += increase;
    }

    /**
     * Sets the crowding distance.
     * @param crowdingDistance - The crowding distance.
     */
    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    /**
     * @return The current crowding distance of this instance.
     */
    public double getCrowdingDistance() {
        return this.crowdingDistance;
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
     * Clones this instance.
     * @return A new instance of this class containing the same state as this
     * instance.
     */
    @Override
    public Object clone() {

        /* Clone the state. */
        NSGA2Individual clone = (NSGA2Individual) super.clone();

        /* Clone the dominated individuals list.
         * NOTE: The list is cloned, but not
         * each individual in the list !!
         */
        List<NSGA2Individual> clonedDominatedIndividuals =
                new ArrayList<NSGA2Individual>(this.dominatedIndividuals.size());

        clonedDominatedIndividuals.addAll(this.dominatedIndividuals);

        clone.setDominatedIndividuals(clonedDominatedIndividuals);

        return clone;
    }

    @Override
    public String toString() {
        String ret = "";
        String chrom = "chromosomes: ";
        String fit = "fitness: " + getFitness();
        String obj = getObjectives() != null ? "obj: " + getObjectives().toString() : "?";
        String constr = getConstraints() != null ? "constr: " + getConstraints().toString() : "?";
        String r = "rank: " + getRank();
        String cd = "crowdd: " + getCrowdingDistance();

        if (getChromosomes() != null) {
            chrom += "[";
            DoubleArray[] c = getChromosomes();

            for (int i = 0; i < c.length; i++) {
                chrom += " ( ";
                for (int j = 0; j < c[i].getNumElements() - 1; j++) {
                    chrom += c[i].getElement(j) + ", ";
                }
                chrom += c[i].getElement(c[i].getNumElements() - 1);
                chrom += " )";
            }

            chrom += " ]";

        } else {
            chrom += "[ ? ]";
        }

        ret += chrom + " - " + fit + " - " + obj + " - " + constr + " - " + r + " - " + cd;

        return ret;
    }
}
