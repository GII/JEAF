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
package es.udc.gii.common.eaf.plugin.parameter;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.util.EAFRandom;
import org.apache.commons.configuration.Configuration;

/**
 * A parameter that returns an evenly distributed random value between two given
 * bounds. <p/>
 *
 * Configuration:
 * <pre>
 * <LowerBound>0</LowerBound>
 * <UpperBound>1</UpperBound>
 * </pre>
 *
 * {@code LowerBound} sets the lower bound of the range. If not given, -1 is
 * assumed. {@code UpperBound} sets the upper bound of the range. If not given
 * 1 is assumed.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class RandomValue extends Parameter {

    private double lowerBound;
    private double upperBound;

    /**
     * Constructs an instance of this class.
     * @param lowerBound Lower bound of the range.
     * @param upperBound Upper bound of the range.
     */
    public RandomValue(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Default constructor. The range is set to [-1,1].
     */
    public RandomValue() {
        setLowerBound(-1);
        setUpperBound(1);
    }

    /**
     * @param algorithm Current algorithm. Not used and hence can be set to
     * {@code null}.
     * @return An evenly distributed random value.
     */
    @Override
    public double get(EvolutionaryAlgorithm algorithm) {
        return (this.upperBound - this.lowerBound) * EAFRandom.nextDouble() + this.lowerBound;
    }

    /**
     * Configures this class.
     * @param conf
     */
    @Override
    public void configure(Configuration conf) {
        if (conf.containsKey("UpperBound")) {
            setUpperBound(conf.getDouble("UpperBound"));
        } else {
            setUpperBound(1);
        }

        if(conf.containsKey("LowerBound")) {
            setLowerBound(conf.getDouble("LowerBound"));
        } else {
            setLowerBound(-1);
        }
    }

    /**
     * @return The lower bound of the range.
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * Sets the lower bound of the range.
     * @param lowerBound The new lower bound.
     */
    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * @return The upper bound of the range.
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * Sets the upper bound of the range.
     * @param upperBound The new upper bound.
     */
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }
}
