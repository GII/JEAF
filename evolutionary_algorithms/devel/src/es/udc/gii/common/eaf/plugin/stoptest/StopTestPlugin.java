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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.plugin.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.plugin.Plugin;
import es.udc.gii.common.eaf.stoptest.CompositeStopTest;
import es.udc.gii.common.eaf.stoptest.SimpleStopTest;
import es.udc.gii.common.eaf.stoptest.StopTest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class StopTestPlugin implements Plugin {

    public abstract int getCurrent(EvolutionaryAlgorithm algorithm);

    public abstract int getMax(EvolutionaryAlgorithm algorithm);

    protected List<StopTest> getStopTestList(EvolutionaryAlgorithm algorithm) {

        List<StopTest> list_test;
        StopTest stopTest;
        
        stopTest = algorithm.getStopTest();
        
        list_test = new ArrayList<StopTest>();

        if (stopTest instanceof SimpleStopTest) {
            list_test.add(stopTest);
        } else {
            list_test.addAll(((CompositeStopTest) stopTest).getStopTests());
        }

        return list_test;

    }
}
