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
 * SimpleFactory.java
 *
 * Created on 28 de noviembre de 2006, 18:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.factory;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.evaluate.EvaluationStrategy;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.operator.OperatorChain;
import es.udc.gii.common.eaf.algorithm.operator.evaluate.EvaluationOperator;
import es.udc.gii.common.eaf.algorithm.operator.replace.ReplaceOperator;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.ReproductionOperator;
import es.udc.gii.common.eaf.algorithm.operator.selection.SelectionOperator;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.algorithm.restart.RestartStrategy;
import es.udc.gii.common.eaf.config.EAFConfiguration;
import es.udc.gii.common.eaf.log.LogTool;
import es.udc.gii.common.eaf.problem.Problem;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class SimpleFactory {
    
    protected File configFile = null;

    public SimpleFactory(String configFileName) {
        this.configFile = new File(configFileName);
        EAFConfiguration.getInstance().loadConfiguration(this.configFile);
    }
    
    public SimpleFactory(InputStream stream) {
        EAFConfiguration.getInstance().loadConfiguration(stream);
    }
        
    public abstract EvolutionaryAlgorithm createAlgorithm();

    public abstract StopTest createStopTest();

    public abstract Problem createProblem();

    public abstract Population createPopulation(FitnessComparator<Individual> comparator);

    public abstract Individual createIndividual(FitnessComparator<Individual> comparator);

    public abstract OperatorChain<SelectionOperator> createSelectionChain();

    public abstract OperatorChain<ReplaceOperator> createReplaceChain();

    public abstract OperatorChain<ReproductionOperator> createReproductionChain();    
    
    public abstract OperatorChain<EvaluationOperator> createEvaluationChain();
    
    public abstract List<LogTool> createLogs();
    
    public abstract EvaluationStrategy createEvaluationStrategy();
    
    //01 - 07 - 2011 - Añadida la estrategía de restart y los test de restart.
    public abstract RestartStrategy createRestartStrategy();
    
    public abstract StopTest createRestartTest();
}
