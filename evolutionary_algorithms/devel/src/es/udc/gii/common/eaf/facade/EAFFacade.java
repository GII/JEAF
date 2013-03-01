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
 * ProblemFacade.java
 *
 * Created on 6 de noviembre de 2006, 18:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.facade;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.factory.SimpleFactory;
import es.udc.gii.common.eaf.factory.XMLSimpleFactory;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.stoptest.StopTest;
import java.io.InputStream;

/**
 * This facade is the responsible of create an algorithm and a stop test. It also has a method to
 * execute the algorithm.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EAFFacade {

    /**
     * Creates a new instance of ProblemFacade
     */
    public EAFFacade() {
    }

    public StopTest createStopTest(InputStream stream) {

        StopTest stopTest = null;
        SimpleFactory factory = new XMLSimpleFactory(stream);
        stopTest = factory.createStopTest();
        return stopTest;

    }

    public StopTest createStopTest(String configFile) {
        StopTest stopTest = null;
        SimpleFactory factory = new XMLSimpleFactory(configFile);
        stopTest = factory.createStopTest();
        return stopTest;
    }

    public EvolutionaryAlgorithm createAlgorithm(String configFile) {
        EvolutionaryAlgorithm algorithm = null;
        SimpleFactory factory = new XMLSimpleFactory(configFile);
        algorithm = factory.createAlgorithm();
        return algorithm;
    }

    public EvolutionaryAlgorithm createAlgorithm(InputStream stream) {
        EvolutionaryAlgorithm algorithm = null;
        SimpleFactory factory = new XMLSimpleFactory(stream);
        algorithm = factory.createAlgorithm();
        return algorithm;
    }
    
    public EvolutionaryAlgorithm createAlgorithmWithStopTest(InputStream stream) {
        EvolutionaryAlgorithm algorithm = null;
        StopTest stopTest = null;
        SimpleFactory factory = new XMLSimpleFactory(stream);
        
        algorithm = factory.createAlgorithm();
        stopTest = factory.createStopTest();
        algorithm.setStopTest(stopTest);
        
        return algorithm;
    }
    
    public void resolve(EvolutionaryAlgorithm algorithm) {
        this.resolve(algorithm.getStopTest(), algorithm);
    }
    
    public void resolve(EvolutionaryAlgorithm algorithm, int maxGen) {
        this.resolve(algorithm.getStopTest(), algorithm, maxGen);
    }

    public void resolve(StopTest stopTest, EvolutionaryAlgorithm algorithm) {
        algorithm.resolve(stopTest);
    }

    public void resolve(String configFile) {

        EvolutionaryAlgorithm algorithm;
        StopTest stopTest;
        Problem problem;

        SimpleFactory factory = new XMLSimpleFactory(configFile);
        algorithm = factory.createAlgorithm();
        stopTest = factory.createStopTest();

        this.resolve(stopTest, algorithm);

    }

    public void resolve(StopTest stopTest,
            EvolutionaryAlgorithm algorithm, int maxGen) {

        algorithm.resolve(stopTest, maxGen);
    }
}
