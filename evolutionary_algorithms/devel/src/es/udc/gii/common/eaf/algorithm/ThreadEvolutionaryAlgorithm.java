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
package es.udc.gii.common.eaf.algorithm;

import es.udc.gii.common.eaf.stoptest.StopTest;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class ThreadEvolutionaryAlgorithm extends Thread {
    
    private EvolutionaryAlgorithm ea;

    private StopTest stop_test;

    public ThreadEvolutionaryAlgorithm(EvolutionaryAlgorithm ea,
            StopTest stop_test) {

        this.ea = ea;
        this.stop_test = stop_test;
        
    }

    @Override
    public void run() {
        this.ea.resolve(stop_test);
    }

    public EvolutionaryAlgorithm getEa() {
        return ea;
    }

    public void setEa(EvolutionaryAlgorithm ea) {
        this.ea = ea;
    }

    
}
