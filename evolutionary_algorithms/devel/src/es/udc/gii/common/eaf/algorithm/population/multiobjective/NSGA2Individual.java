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
package es.udc.gii.common.eaf.algorithm.population.multiobjective;

import java.util.Map;

/**
 * This class represents an individual for the NSGA2 algorithm.
 *
 * @author Grupo Integrado de Ingeniería (<a
 * href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class NSGA2Individual extends MultiobjectiveIndividual {

    /**
     * Crowding distance value. Greater values are better.
     */
    private double crowdingDistance;

    public NSGA2Individual() {
        super();
    }

    public NSGA2Individual(Map<Integer, double[]> chromosomes) {
        super(chromosomes);
    }

    /**
     * Initialized this instance.
     */
    @Override
    protected void initialize() {
        super.initialize();
        this.crowdingDistance = 0;
    }

    /**
     * Sets the crowding distance.
     *
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
     * Increases the crowding distance by an specified amount.
     *
     * @param increase - Amount to increase the crowding distance.
     */
    public void increaseCrowdingDistance(double increase) {
        this.crowdingDistance += increase;
    }

    @Override
    public String toString() {
        String ret = "";
        String cd = "crowdd: " + getCrowdingDistance();

        ret = super.toString() + " - " + cd;

        return ret;
    }
}
