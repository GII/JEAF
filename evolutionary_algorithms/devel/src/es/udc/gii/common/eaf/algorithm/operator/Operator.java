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



package es.udc.gii.common.eaf.algorithm.operator;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.exception.OperatorException;
import java.util.List;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.config.Configurable;

/**
 * An <tt>Operator</tt> represents an operation that takes place on
 * a list of indivuduals during the evolution process. Examples
 * of operators include reproduction, crossover, and mutation.<p>
 * 
 * This interface contains one methods: <tt>operate</tt>.
 * This method is responsible for performing the operator on the current
 * algorithm and the current list of individuals. <p>

 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public interface Operator extends Configurable {
    
    /**
     * Execute this operator over a group of individuals. This method will be 
     * invoked by the current algorithm during its execution. It will be applied on
     * a list of individuals and will return a list of modified individuals.<p>
     * 
     * This method will recive two parameters. The current algorithm, to visit it
     * if it is necessary, for example, to get some parameter of the current execution.
     * And a list of individuals on wich we will apply this operator.<p>
     * @param algorithm the current algorithm, to visit when it is necessary.
     * @param individuals list of individuals on wich we will apply the operator.
     * @return the result of apply this operator to a individuals' lisr.
     * @throws es.udc.gii.common.eaf.exception.OperatorException when occurs some type of error during the execution of this operator.
     */
    public List<Individual> operate(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals)
        throws OperatorException;

    
}

