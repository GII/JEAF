 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.log.diversity;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.log.LogTool;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 * Corriveau, G., Guilbault, R., Tahan, A., & Sabourin, R. (2012). Review and
 * study of genotypic diversity measures for real-coded representations.
 * Evolutionary Computation, IEEE Transactions on, 16(5), 695-710.
 *
 * @author pilar
 */
public class PairwiseDiversityMeasurementLogTool extends LogTool {

    private List<Double> diversityList;

    private List<Integer> FEsList;
    
    private Double NMDF;

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg); //To change body of generated methods, choose Tools | Templates.

        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm) o;
        double diversity, distance;
        int N, dim;
        double[] xi, xj;

        if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE) {

            if (diversityList == null) {
                diversityList = new ArrayList<>();
                FEsList = new ArrayList<>();
                NMDF = -Double.MAX_VALUE;
            }

            diversity = 0.0;
            N = algorithm.getPopulation().getSize();
            dim = algorithm.getPopulation().getIndividual(0).getDimension();

            for (int i = 1; i < N; i++) {

                xi = algorithm.getPopulation().getIndividual(i).getChromosomeAt(0);

                for (int j = 0; j < i - 1; j++) {

                    xj = algorithm.getPopulation().getIndividual(j).getChromosomeAt(0);

                    distance = 0.0;
                    for (int k = 0; k < dim; k++) {

                        distance += Math.pow(xi[k] - xj[k], 2.0);

                    }

                    distance = Math.sqrt(distance);
                    diversity += distance;
                }
            }

            diversity *= (2.0 / (N * (N - 1)));

            NMDF = (diversity > NMDF ? diversity : NMDF);
            diversityList.add(diversity);
            FEsList.add(algorithm.getFEs());

        } else if (algorithm.getState() == EvolutionaryAlgorithm.FINAL_STATE) {


            for (int i = 0; i < diversityList.size(); i++) {

                diversity = diversityList.get(i) / NMDF;
                super.getLog().println(FEsList.get(i) + "\t" + diversity);
            }
        }

    }

    @Override
    public String getLogID() {
        return "pairwisediversity"; //To change body of generated methods, choose Tools | Templates.
    }

}
