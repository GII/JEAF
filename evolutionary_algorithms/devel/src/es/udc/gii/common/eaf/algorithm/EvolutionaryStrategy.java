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
 * EvolutionaryStrategy.java
 *
 * Created on 2 de octubre de 2007, 11:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm;

import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EvolutionaryStrategy extends EvolutionaryAlgorithm {

    /**
     * Number of children that is generated from each parent.
     */
    private int lambda = 1;

    /** Creates a new instance of EvolutionaryStrategy */
    public EvolutionaryStrategy() {
    }

    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);
            this.lambda = conf.getInt("Lambda");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getLambda() {
        return this.lambda;
    }

//    @Override
//    protected void select(Population toPopulation) {
//        //!!NO tienen seleccion!!!
//        toPopulation.setIndividuals(this.getPopulation().getIndividualsCopy());
//    }

    @Override
    public String getAlgorithmID() {
        return "ES";
    }
    
    public void setLambda(int lambda) {
        this.lambda = lambda;
    }
}
