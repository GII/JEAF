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
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.config.Configurable;

/**
 * This interface represents a stop test. The represented condition by this test 
 * is the one that an algorithm has to reach to finish its execution when it is 
 * resolved a problem.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public interface StopTest extends Configurable {

    /**
     * Returns <tt>true</tt> if the problem reach the objective.
     * @param algorithm The algorithm wich has to reach the objective
     * @return <tt>true</tt> if the problem has reached the objective, 
     * <tt>false</tt> in other case.
     */
    public boolean isReach(EvolutionaryAlgorithm algorithm);

    /**
     * This method is meant for (re)initialization of the stop test.
     * @param algorithm The agorithm to use.
     */
    public void reset(EvolutionaryAlgorithm algorithm);
}
