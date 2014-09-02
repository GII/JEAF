/*
 * Copyright (C) 2012 Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
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
package es.udc.gii.common.eaf.stoptest.cma;

import es.udc.gii.common.eaf.algorithm.CMAEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.stoptest.SimpleStopTest;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a
 * href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class CMAConditionNumberStopTest extends SimpleStopTest {

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {

        CMAEvolutionaryAlgorithm cma = (CMAEvolutionaryAlgorithm) algorithm;
        int N = cma.getPopulation().getIndividual(0).getDimension();
        

        if (StatUtils.min(cma.getDiag()) <= 0.0) {
            return true;
        } else if (StatUtils.max(cma.getDiag()) > 1e7) {
            return true;
        }
        

        return false;

    }

    @Override
    public void configure(Configuration conf) {
        //Do nothing
    }

}
