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
 * DistributedEvaluationLogTool.java
 *
 * Created on April 2, 2008, 10:55 AM
 *
 */
package es.udc.gii.common.eaf.log.parallel;

import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.evaluation.DistributedEvaluation;
import es.udc.gii.common.eaf.exception.WrongAlgorithmException;
import java.util.Observable;

/**
 * Logs the evaluations in a parallel evolutionary algorithm.
 *
 * The output for a master node reads as follows (although the sequence might
 * differ because of parallel processing):
 *
 * <pre>
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * TOTAL INDIVIDUALS TO EVALUATE:
 * ... list of individuals to evaluate in this generation ...
 * 
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * Sending EvaluationObject:
 * ... evaluation object sent to slave or to self ...
 *
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * Received EvaluationObject:
 * ... evaluation object received from slave or from self ...
 *
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * Individuals to evaluate:
 * ... chunk of individuals to evaluate ...
 *
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * Evaluated individuals:
 * ... chunk of evaluated individuals ...
 *
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * STATS:
 * ... statistics collected by the master node ...
 *
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * TOTAL EVALUATED INDIVIDUALS:
 * ... total evaluated individuals in this generation ...
 *
 * -------------------
 * </pre>
 *
 * Several send and receive are logged as the distribution of the evaluation
 * goes on. The statistics are only print if the distributed evaluation
 * strategy has been configured for collecting them (see 
 * {@link DistributedEvaluation}).<p/>
 *
 * The output for a slave node reads as follows (although the sequence might
 * differ because of parallel processing):
 *
 * <pre>
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * Received EvaluationObject:
 * ... evaluation object received from slave or from self ...
 *
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * Individuals to evaluate:
 * ... chunk of individuals to evaluate ...
 *
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * Evaluated individuals:
 * ... chunk of evaluated individuals ...
 * 
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * Sending EvaluationObject:
 * ... evaluation object sent to slave or to self ...
 *
 * &lt; time ms &gt; [topology rank ... rank of the node ... ]
 * STATS:
 * ... statistics collected by the master node ...
 * </pre>
 *
 * Several send and receive are logged as the distribution of the evaluation
 * goes on. The statistics are only print if the distributed evaluation
 * strategy has been configured for collecting them (see
 * {@link DistributedEvaluation}).<p/>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 * @see DistributedEvaluation
 */
public class DistributedEvaluationLogTool extends ParallelLogTool {

    /** Creates a new instance of DistributedEvaluationLogTool */
    public DistributedEvaluationLogTool() {
    }

    @Override
    public String getLogID() {
        return "DistributedEvaluationLogTool";
    }

    @Override
    public void update(Observable o, Object arg) {

        if (!(o instanceof ParallelEvolutionaryAlgorithm)) {
            throw new WrongAlgorithmException(
                    ParallelEvolutionaryAlgorithm.class, o.getClass());
        }

        ParallelEvolutionaryAlgorithm pea = (ParallelEvolutionaryAlgorithm) o;

        if (pea.getCurrentObservable() instanceof DistributedEvaluation) {
            super.update(o, arg);
            DistributedEvaluation de = (DistributedEvaluation) pea.getCurrentObservable();

            long t1 = System.currentTimeMillis();

            String head = "<" + (t1 - t0) + " ms> [topology rank " + de.getTopology().getTopologyRank() + "]\n";

            switch (de.getState()) {
                case DistributedEvaluation.SENT:
                    getLog().println(head + "Sending EvaluationObject:\n" + arg + "\n");
                    break;
                case DistributedEvaluation.RECEIVED:
                    getLog().println(head + "Received EvaluationObject:\n" + arg + "\n");
                    break;
                case DistributedEvaluation.TO_EVALUATE:
                    getLog().println(head + "Individuals to evaluate:\n" + arg + "\n");
                    break;
                case DistributedEvaluation.EVALUATED:
                    getLog().println(head + "Evaluated individuals:\n " + arg + "\n");
                    break;
                case DistributedEvaluation.CURRENT_EVALUATION_ENDED:
                    if (de.isCollectStatistics()) {
                        getLog().println(head + "STATS:\n");
                        getLog().println(de.getStatistics());
                    }
                    if (de.isMaster()) {
                        getLog().println(head + "TOTAL EVALUATED INDIVIDUALS:\n" + de.getIndividualsToEvaluate() + "\n");
                        getLog().println("\n-------------------\n");
                    }
                    break;
                case DistributedEvaluation.CURRENT_EVALUATION_STARTED:
                    if (de.isMaster()) {
                        getLog().println(head + "TOTAL INDIVIDUALS TO EVALUATE:\n" + de.getIndividualsToEvaluate() + "\n");
                    }
            }
        }
    }
}
