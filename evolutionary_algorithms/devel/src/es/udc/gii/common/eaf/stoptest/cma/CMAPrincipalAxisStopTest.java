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
import es.udc.gii.common.eaf.util.ConfWarning;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a
 * href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class CMAPrincipalAxisStopTest extends SimpleStopTest {

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {

        
        CMAEvolutionaryAlgorithm cma = (CMAEvolutionaryAlgorithm) algorithm;
        int N = cma.getPopulation().getIndividual(0).getDimension();
        boolean flgdiag = cma.getFlgDiag();

        for (int iAchse = 0; iAchse < N; ++iAchse) {
            int iKoo;
            int l = flgdiag ? iAchse : 0;
            int u = flgdiag ? iAchse + 1 : N;
            double fac = 0.1 * cma.getSigma() * cma.getDiag()[iAchse];
            for (iKoo = l; iKoo < u; ++iKoo) {
                if (cma.getxMean()[iKoo] != cma.getxMean()[iKoo] + fac * cma.getB()[iKoo][iAchse]) {
                    break; // is OK for this iAchse
                }
            }
            if (iKoo == u) // no break, therefore no change for axis iAchse
            {
                return true;
            }
        } /* for iAchse */

        return false;

    }

    @Override
    public void configure(Configuration conf) {
      //Do nothing
    }

}
