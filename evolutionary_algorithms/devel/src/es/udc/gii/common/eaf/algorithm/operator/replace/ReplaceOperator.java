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
 * ReplaceOperator.java
 *
 * Created on 30 de noviembre de 2006, 18:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.algorithm.operator.replace;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.exception.OperatorException;
import es.udc.gii.common.eaf.algorithm.operator.Operator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.List;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 *
 *
 */

/* Rafa: añadi el extends para poder observar el operador de migracion (lo haria 
 en MigrationOperator, pero la herencia multiple no esta permitida) */
public abstract class ReplaceOperator extends Observable implements Operator { 
    
    /** Creates a new instance of ReplaceOperator */
    public ReplaceOperator() {
    }

    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm, 
            List<Individual> individuals) throws OperatorException {
        
        List<Individual> newIndividuals;
        
        newIndividuals = this.replace(algorithm,individuals);
        
        return newIndividuals;
    }
    
    protected abstract List<Individual> replace(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation);

    @Override
    public void configure(Configuration conf) {        
    }        
}
