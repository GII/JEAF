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
package es.udc.gii.common.eaf.algorithm.operator.evaluate;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.OperatorException;
import es.udc.gii.common.eaf.plugin.evaluation.IndividualImprover;
import es.udc.gii.common.eaf.plugin.individual.BestIndividual;
import es.udc.gii.common.eaf.plugin.individual.IndividualChooser;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class LocalSearchOperator extends EvaluationOperator {

    private IndividualImprover improver;
    private IndividualChooser chooser;
    private boolean evaluateReference;

    public LocalSearchOperator(IndividualImprover generator,
            IndividualChooser chooser) {
        this.improver = generator;
        this.chooser = chooser;
    }

    public LocalSearchOperator() {
        this.chooser = new BestIndividual();
    }

    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) throws OperatorException {

        if (!this.improver.doesEvaluate()) {
            List<Individual> toEvaluate = new ArrayList<Individual>();
            int[] improvedCount = new int[individuals.size()];
            int i = 0;

            int evals = 0;
            for (Individual ind : individuals) {
                IndividualImprover.Improvement improvement =
                        improver.improve(algorithm, ind);

                if (this.evaluateReference) {
                    toEvaluate.add(ind);
                }

                toEvaluate.addAll(improvement.improved);

                improvedCount[i++] = improvement.improved.size();
                evals += improvement.totalEvaluations;
            }

            /* Evaluate all individuals in the same step. */
            algorithm.getEvaluationStrategy().evaluate(algorithm, toEvaluate,
                    algorithm.getProblem().getObjectiveFunctions(),
                    algorithm.getProblem().getConstraints());

            int index = 0;

            for (int ind = 0; ind < individuals.size(); ind++) {

                Individual reference = individuals.get(ind);

                Individual chosen = null;

                if (this.evaluateReference) {
                    chosen = this.chooser.get(algorithm,
                            toEvaluate.subList(index, index + improvedCount[ind] + 1),
                            reference);

                    index += improvedCount[ind] + 1;
                } else {
                    List<Individual> pop = new ArrayList<Individual>();
                    pop.add(reference);
                    pop.addAll(toEvaluate.subList(index,
                            index + improvedCount[ind]));

                    index += improvedCount[ind];

                    chosen = this.chooser.get(algorithm, pop, reference);
                }

                chosen.copyEvalResults(reference);
                chosen.copyGenotype(reference);
            }

            algorithm.setFEs(algorithm.getFEs() + toEvaluate.size() + evals);
        } else {
            for (Individual ind : individuals) {

                /* The improver evaluates the parent and the improved children. */
                IndividualImprover.Improvement improvement =
                        improver.improve(algorithm, ind);

                algorithm.setFEs(
                        algorithm.getFEs() + improvement.totalEvaluations);

                List<Individual> pop =
                        new ArrayList<Individual>(improvement.improved.size() + 1);
                pop.add(ind);
                pop.addAll(improvement.improved);

                Individual chosen = chooser.get(algorithm, pop, ind);
                chosen.copyEvalResults(ind);
                chosen.copyGenotype(ind);
            }
        }

        return individuals;
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        try {
            if (conf.containsKey("IndividualImprover.Class")) {
                this.improver = (IndividualImprover) Class.forName(conf.getString("IndividualImprover.Class")).newInstance();
                this.improver.configure(conf.subset("IndividualImprover"));
            }

            if (conf.containsKey("IndividualChooser.Class")) {
                this.chooser = (IndividualChooser) Class.forName(conf.getString("IndividualChooser.Class")).newInstance();
                this.chooser.configure(conf.subset("IndividualChooser"));
            } else {
                this.chooser = new BestIndividual();
            }

            this.evaluateReference =
                    !conf.containsKey("IndividualImprover.DontEvaluateReference");

        } catch (Exception ex) {
            Logger.getLogger(LocalSearchOperator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public IndividualChooser getChooser() {
        return chooser;
    }

    public void setChooser(IndividualChooser chooser) {
        this.chooser = chooser;
    }

    public IndividualImprover getImprover() {
        return improver;
    }

    public void setImprover(IndividualImprover improver) {
        this.improver = improver;
    }
}
