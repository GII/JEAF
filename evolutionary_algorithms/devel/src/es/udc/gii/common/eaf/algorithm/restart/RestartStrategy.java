/*
 * Copyright (C) 2011 Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
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
package es.udc.gii.common.eaf.algorithm.restart;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.config.Configurable;

/**
 * A <tt>RestartStrategy</tt> represents a way to restart an algorithm when the restart criteria is
 * fullfil.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public interface RestartStrategy extends Configurable  {

    /**
     * This method receive an evolutionary algorithm and it performs over it a restart strategy.
     * This strategy could be, for example, re-sample the population or increase the number of
     * individuals.
     * @param algorithm An evolutionary algorithm to be restarted. 
     */
    public void restart(EvolutionaryAlgorithm algorithm);
    
}
