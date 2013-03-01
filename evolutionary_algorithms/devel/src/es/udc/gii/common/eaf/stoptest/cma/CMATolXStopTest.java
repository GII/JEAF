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
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class CMATolXStopTest extends SimpleStopTest {

    private double tol_x = 1.0e-11;
    private double tol_x_factor = 1.0e-11;

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {

        double current_tol_x, sigma, minstartsigma, maxsqrtdiagC, pc;
        CMAEvolutionaryAlgorithm cma = (CMAEvolutionaryAlgorithm)algorithm;
        
        minstartsigma = StatUtils.min(cma.getStartSigma());
        current_tol_x = Math.max(this.tol_x, this.tol_x_factor*minstartsigma);
        maxsqrtdiagC = Math.sqrt(StatUtils.max(cma.diag(cma.getC())));
        sigma = cma.getSigma();
        
        pc = -Double.MAX_VALUE;
        for (double d : cma.getPc()) {
            pc = Math.max(pc, Math.abs(d));
        }
        
        if (sigma * maxsqrtdiagC < current_tol_x && sigma * pc < current_tol_x) {
            return true;
        }
        
        return false;

    }

    @Override
    public void configure(Configuration conf) {

        if (conf.containsKey("TolX")) {
            this.tol_x = conf.getDouble("TolX");
        } else {
            ConfWarning w = new ConfWarning("TolX", this.tol_x);
            w.warn();
        }

        if (conf.containsKey("TolXFactor")) {
            this.tol_x_factor = conf.getDouble("TolXFactor");
        } else {
            ConfWarning w = new ConfWarning("TolXFactor", this.tol_x_factor);
            w.warn();
        }

    }
}
