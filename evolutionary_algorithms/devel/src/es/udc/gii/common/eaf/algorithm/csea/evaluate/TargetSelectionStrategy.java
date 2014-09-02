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
package es.udc.gii.common.eaf.algorithm.csea.evaluate;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.List;

/**
 *
 * @author pilar
 */
public interface TargetSelectionStrategy {
    
    public Individual selectTarget(List<Individual> targetPopulation, Individual reference);
    
    public abstract void reset();
    
}
