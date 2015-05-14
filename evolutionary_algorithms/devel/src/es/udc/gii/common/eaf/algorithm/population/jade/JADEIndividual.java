/*
 *  Copyright (C) 2010 Grupo Integrado de Ingeniería
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.udc.gii.common.eaf.algorithm.population.jade;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import org.apache.commons.math.util.DoubleArray;

/**
 * JADE is a differential evolution version which updates control parameters in an adaptive maner.
 * This class implements an indivudial to be used in the JADE algorithm. This extension of the standard
 * individual is needed because in the JADE algorithm we need the values of the parameters F and CR
 * in order to adapt them during the evolution.<p>
 *
 * Among all the parameters of the Individual class, this class also has the parameters F and CR used
 * to generate the JADEIndividual instance. <p>
 *
 * This class override the method <i>clone</i> to clone the F and CR parameters.<p>
 *
 * JADE is presented in "JADE: Adaptive Differential Evolution with Optional External Archive",
 * Jinqiao Zhang amd Arthur C. Sanderson, IEEE Transacions on Evolutionary Computation, Vol. 13,
 * No. 5, October 2009. <p>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0.
 */
public class JADEIndividual extends Individual {

    /**
     * Value of the CR parameter used to generate this individual.
     */
    private double CR = 0.5;

    /**
     * Value of the F parameter used to generate this individual.
     */
    private double F = 0.5;

    public JADEIndividual(DoubleArray[] chromosomes, double CR, double F) {
        super(chromosomes);
        this.CR = CR;
        this.F = F;
    }

    public JADEIndividual(double CR, double F) {
        this.CR = CR;
        this.F = F;
    }

    public JADEIndividual() {}

    public double getCR() {
        return CR;
    }

    public void setCR(double CR) {
        this.CR = CR;
    }

    public double getF() {
        return F;
    }

    public void setF(double F) {
        this.F = F;
    }

    @Override
    public Object clone() {

        JADEIndividual copy = (JADEIndividual) super.clone();

        copy.setCR(this.CR);
        copy.setF(this.F);

        return copy;
    }




}
