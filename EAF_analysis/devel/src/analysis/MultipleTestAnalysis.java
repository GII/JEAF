/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.stoptest.StopTest;
import java.util.List;
import util.LoadProblemsFromFile;

/**
 *
 * @author pilar
 */
public class MultipleTestAnalysis extends CompositeAnalysis {

    private String tests_file;
    private List<Problem> problems;

    public MultipleTestAnalysis(String tests_file, EAFAnalysis succesor) {
        super(succesor);
        this.tests_file = tests_file;
        this.problems = LoadProblemsFromFile.readProblems(this.tests_file);
    }

    @Override
    public void run(EvolutionaryAlgorithm algorithm, StopTest stopTest) {

        int[] dimension = new int[1];

        for (Problem p : this.problems) {

            algorithm.setProblem(p);

            if (p.getObjectiveFunctions().get(0).getDimension() != -Integer.MAX_VALUE) {
                dimension[0] = p.getObjectiveFunctions().get(0).getDimension();
                //Se establece la dimensionalidad del problema:
                algorithm.getPopulation().setDimension(dimension);
            }

            this.succesor.run(algorithm, stopTest);
        }

    }
}
