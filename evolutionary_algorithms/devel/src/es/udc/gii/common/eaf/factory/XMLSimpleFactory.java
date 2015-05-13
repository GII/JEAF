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
 * XMLSimpleFactory.java
 *
 * Created on 28 de noviembre de 2006, 18:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.factory;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.evaluate.EvaluationStrategy;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.algorithm.operator.replace.ReplaceOperator;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.ReproductionOperator;
import es.udc.gii.common.eaf.algorithm.operator.selection.SelectionOperator;
import es.udc.gii.common.eaf.algorithm.restart.RestartStrategy;
import es.udc.gii.common.eaf.log.LogTool;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.algorithm.operator.OperatorChain;
import es.udc.gii.common.eaf.algorithm.operator.evaluate.EvaluationOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.config.EAFConfiguration;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.stoptest.CompositeStopTest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.util.ResizableDoubleArray;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class XMLSimpleFactory extends SimpleFactory {

    public XMLSimpleFactory(String configFileName) {
        super(configFileName);
    }

    public XMLSimpleFactory(InputStream stream) {
        super(stream);
    }

    @Override
    public EvolutionaryAlgorithm createAlgorithm() {
        EAFConfiguration eafConfig = EAFConfiguration.getInstance();
        EvolutionaryAlgorithm ea =
                (EvolutionaryAlgorithm) eafConfig.getObject("Class");

        OperatorChain<SelectionOperator> selChain = createSelectionChain();
        OperatorChain<ReplaceOperator> replChain = createReplaceChain();
        OperatorChain<ReproductionOperator> reprChain = createReproductionChain();
        OperatorChain<EvaluationOperator> evalChain = createEvaluationChain();

        EvaluationStrategy es = createEvaluationStrategy();
        Problem pb = createProblem();
        List<LogTool> logs = createLogs();

        RestartStrategy rs = createRestartStrategy();
        ea.setRestartStrategy(rs);

        StopTest rsttst = createRestartTest();
        ea.setRestartTest(rsttst);

        ea.setSelectionChain(selChain);
        ea.setReplaceChain(replChain);
        ea.setReproductionChain(reprChain);
        ea.setEvalChain(evalChain);
        ea.setEvaluationStrategy(es);
        ea.setProblem(pb);

        for (LogTool log : logs) {
            ea.addObserver(log);
        }

        ea.configure(eafConfig);
        Population pop = createPopulation(ea.getComparator());
        ea.setPopulation(pop);

        return ea;
    }

    @Override
    public StopTest createStopTest() {
        Configuration conf = EAFConfiguration.getInstance().subset("StopTests");
        List stptsts = conf.getList("StopTest.Class");

        if (stptsts.size() > 1) {
            CompositeStopTest compSt = new CompositeStopTest();

            for (int i = 0; i < stptsts.size(); i++) {
                try {
                    StopTest st = (StopTest) Class.forName(
                            (String) stptsts.get(i)).newInstance();
                    st.configure(conf.subset("StopTest(" + i + ")"));
                    compSt.addStopTest(st);
//                    return compSt;
                } catch (Exception ex) {
                    throw new ConfigurationException(
                            "Wrong stop test configuration for " + (String) stptsts.get(i) + " ?" + " \n Thrown exception: \n" + ex);
                }
            }
            return compSt;
        } else if (stptsts.size() == 1) {
            try {
                StopTest st = (StopTest) Class.forName((String) stptsts.get(0)).newInstance();
                st.configure(conf.subset("StopTest"));
                return st;
            } catch (Exception ex) {
                throw new ConfigurationException(
                        "Wrong stop test configuration for " + (String) stptsts.get(0) + " ?" + " \n Thrown exception: \n" + ex);
            }
        } else if (stptsts.isEmpty()) {
            return null;
        }

        throw new ConfigurationException("No stop test class specified.");
    
    }

    @Override
    public Problem createProblem() {
        Configuration conf = EAFConfiguration.getInstance().subset("Objective");
        Problem pb = new Problem();
        pb.configure(conf);
        return pb;
    }

    @Override
    public Population createPopulation(FitnessComparator<Individual> comparator) {
        Configuration conf = EAFConfiguration.getInstance().subset("Population");

        int size = conf.getInt("Size");
        Individual ind = createIndividual(comparator);

        List<Individual> listInd = new ArrayList<Individual>();
        for (int i = 0; i < size; i++) {
            listInd.add((Individual) ind.clone());
        }

        Population pop = new Population();
        pop.setIndividuals(listInd);

        return pop;
    }

    @Override
    public Individual createIndividual(FitnessComparator<Individual> comparator) {
        Configuration conf =
                EAFConfiguration.getInstance().subset("Population.Individual");

        try {
            Individual ind =
                    (Individual) Class.forName(
                    conf.getString("Class")).newInstance();

            List chromosomes = conf.getList("Chromosome[@size]");

            if (chromosomes.isEmpty()) {
                throw new ConfigurationException("No chromosome specified.");
            }

            Map<Integer, double[]> chrms = new HashMap<>();
            double[] c;
            
            for (int i = 0; i < chromosomes.size(); i++) {
                int chrom_size = conf.getInt("Chromosome(" + i + ")[@size]");
                c = new double[chrom_size];
                chrms.put(i, c);
            }

            ind.setChromosomes(chrms);
            ind.setComparator(comparator);
            ind.configure(conf);
//            ind.generate();

            return ind;

        } catch (Exception ex) {
            throw new ConfigurationException(
                    "Wrong individual configuration for " + conf.getString("Class") + " ?" + " \n Thrown exception: \n" + ex);
        }
    }

    @Override
    public OperatorChain<SelectionOperator> createSelectionChain() {

        OperatorChain<SelectionOperator> oChain = null;
        Configuration conf =
                EAFConfiguration.getInstance().subset(
                "OperatorChains.SelectionChain");
        List ops = conf.getList("Operator.Class");


        if (ops.size() > 0) {
            oChain = new OperatorChain<SelectionOperator>();


            for (int i = 0; i < ops.size(); i++) {
                try {
                    SelectionOperator op =
                            (SelectionOperator) Class.forName(
                            (String) ops.get(i)).newInstance();

                    op.configure(conf.subset("Operator(" + i + ")"));
                    oChain.addOperators(op);
                } catch (Exception ex) {
                    throw new ConfigurationException(
                            "Wrong selection operator configuration for " + (String) ops.get(i) + " ?" + " \n Thrown exception: \n" + ex);
                }


            }

        }

        return oChain;
    }

    @Override
    public OperatorChain<ReplaceOperator> createReplaceChain() {
        Configuration conf =
                EAFConfiguration.getInstance().subset(
                "OperatorChains.ReplaceChain");

        OperatorChain<ReplaceOperator> oChain = null;

        List ops = conf.getList("Operator.Class");

        if (ops.size() > 0) {
            oChain =
                    new OperatorChain<ReplaceOperator>();

            for (int i = 0; i < ops.size(); i++) {
                try {
                    ReplaceOperator op =
                            (ReplaceOperator) Class.forName(
                            (String) ops.get(i)).newInstance();

                    op.configure(conf.subset("Operator(" + i + ")"));
                    oChain.addOperators(op);
                } catch (Exception ex) {
                    throw new ConfigurationException(
                            "Wrong replace operator configuration for " + (String) ops.get(i) + " ?" + " \n Thrown exception: \n" + ex);
                }
            }
        }

        return oChain;
    }

    @Override
    public OperatorChain<ReproductionOperator> createReproductionChain() {
        Configuration conf =
                EAFConfiguration.getInstance().subset(
                "OperatorChains.ReproductionChain");
        OperatorChain<ReproductionOperator> oChain = null;


        List ops = conf.getList("Operator.Class");

        if (ops.size() > 0) {

            oChain =
                    new OperatorChain<ReproductionOperator>();


            for (int i = 0; i < ops.size(); i++) {
                try {
                    ReproductionOperator op =
                            (ReproductionOperator) Class.forName(
                            (String) ops.get(i)).newInstance();

                    op.configure(conf.subset("Operator(" + i + ")"));
                    oChain.addOperators(op);
                } catch (Exception ex) {
                    throw new ConfigurationException(
                            "Wrong reproduction operator configuration for " + (String) ops.get(i) + " ?" + " \n Thrown exception: \n" + ex);
                }
            }

        }

        return oChain;
    }

    @Override
    public OperatorChain<EvaluationOperator> createEvaluationChain() {
        Configuration conf =
                EAFConfiguration.getInstance().subset(
                "OperatorChains.EvaluationChain");

        OperatorChain<EvaluationOperator> oChain = null;

        List ops = conf.getList("Operator.Class");

        if (ops.size() > 0) {

            oChain =
                    new OperatorChain<EvaluationOperator>();

            for (int i = 0; i < ops.size(); i++) {
                try {
                    EvaluationOperator op =
                            (EvaluationOperator) Class.forName(
                            (String) ops.get(i)).newInstance();

                    op.configure(conf.subset("Operator(" + i + ")"));
                    oChain.addOperators(op);
                } catch (Exception ex) {
                    throw new ConfigurationException(
                            "Wrong evaluation operator configuration for " + (String) ops.get(i) + " ?" + " \n Thrown exception: \n" + ex);
                }
            }

        }

        return oChain;
    }

    @Override
    public List<LogTool> createLogs() {
        Configuration conf =
                EAFConfiguration.getInstance().subset("LogTool");

        List<LogTool> listLogs = new ArrayList<LogTool>();
        List logs = conf.getList("Log.Class");

        for (int i = 0; i < logs.size(); i++) {
            try {
                LogTool log = (LogTool) Class.forName(
                        (String) logs.get(i)).newInstance();
                log.configure(conf.subset("Log(" + i + ")"));
                listLogs.add(log);
            } catch (Exception ex) {
                throw new ConfigurationException(
                        "Wrong log tool configuration for " + (String) logs.get(i) + " ?" + " \n Thrown exception: \n" + ex);
            }
        }

        return listLogs;
    }

    @Override
    public EvaluationStrategy createEvaluationStrategy() {

        //21 - 06 - 2011: Se añade la posibilidad de que no exista estrategia de evaluación.
        if (EAFConfiguration.getInstance().containsKey("EvaluationStrategy.Class")) {
            Configuration conf =
                    EAFConfiguration.getInstance().subset("EvaluationStrategy");
            try {

                EvaluationStrategy es = (EvaluationStrategy) Class.forName(
                        conf.getString("Class")).newInstance();

                es.configure(conf);

                return es;
            } catch (Exception ex) {
                throw new ConfigurationException(
                        "Wrong evaluation strategy configuration for  " + conf.getString("Class") + " ?" + " \n Thrown exception: \n" + ex);
            }
        } else {
            return null;
        }
    }

    @Override
    public RestartStrategy createRestartStrategy() {

        //01 - 07 - 2011 : Se añaden las estrategías de reinicio.
        if (EAFConfiguration.getInstance().containsKey("RestartStrategy.Class")) {
            Configuration conf =
                    EAFConfiguration.getInstance().subset("RestartStrategy");
            try {

                RestartStrategy rs = (RestartStrategy) Class.forName(
                        conf.getString("Class")).newInstance();

                rs.configure(conf);

                return rs;
            } catch (Exception ex) {
                throw new ConfigurationException(
                        "Wrong restart strategy configuration for  " + conf.getString("Class") + " ?" + " \n Thrown exception: \n" + ex);
            }
        } else {
            return null;
        }
    }

    @Override
    public StopTest createRestartTest() {

        //01 - 07 - 2011: se añaden los test de reinicio, pueden existir o no.

        if (EAFConfiguration.getInstance().containsKey("RestartTests.RestartTest.Class")) {

            Configuration conf = EAFConfiguration.getInstance().subset("RestartTests");
            List rststs = conf.getList("RestartTest.Class");

            if (rststs.size() > 1) {
                CompositeStopTest compSt = new CompositeStopTest();

                for (int i = 0; i < rststs.size(); i++) {
                    try {
                        StopTest st = (StopTest) Class.forName(
                                (String) rststs.get(i)).newInstance();
                        st.configure(conf.subset("RestartTest(" + i + ")"));
                        compSt.addStopTest(st);
//                    return compSt;
                    } catch (Exception ex) {
                        throw new ConfigurationException(
                                "Wrong restart test configuration for " + (String) rststs.get(i) + " ?" + " \n Thrown exception: \n" + ex);
                    }
                }
                return compSt;
            } else if (rststs.size() == 1) {
                try {
                    StopTest st = (StopTest) Class.forName((String) rststs.get(0)).newInstance();
                    st.configure(conf.subset("RestartTest"));
                    return st;
                } catch (Exception ex) {
                    throw new ConfigurationException(
                            "Wrong restart test configuration for " + (String) rststs.get(0) + " ?" + " \n Thrown exception: \n" + ex);
                }
            }
        }

        return null;
    }
}
