 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.log.diversity;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.log.LogTool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

/**
 *
 * Guillaume Corriveau, Raynald Guilbault, Antoine Tahan, Robert Sabourin,
 * Review of phenotypic diversity formulations for diagnostic tool, Applied Soft
 * Computing, Volume 13, Issue 1, January 2013, Pages 9-26, ISSN 1568-4946
 *
 * @author pilar
 */
public class PhenotypicConvergenceMeasurementLogTool extends LogTool {

    private double fWorst;

    private double fBest;

    private List<Double> convergenceList;

    private List<Double> VMD;

    private List<Double> fWorstList;

    private List<Double> fBestList;

    private List<Integer> FESList;

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg); //To change body of generated methods, choose Tools | Templates.

        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm) o;
        double convergence;
        int N;
        double fI, fI1;
        Individual indI, indI1;

        if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE) {

            if (convergenceList == null) {
                convergenceList = new ArrayList<>();
                FESList = new ArrayList<>();
                fWorstList = new ArrayList<>();
                fBestList = new ArrayList<>();
                VMD = new ArrayList<>();
                fWorst = -Double.MAX_VALUE;
                fBest = Double.MAX_VALUE;

            }

            //Hay que ordenar los individuos por calidad:
            List<Individual> sortedIndividuals = algorithm.getPopulation().getIndividualsCopy();
            Collections.sort(sortedIndividuals, algorithm.getComparator());

            convergence = 0.0;
            N = algorithm.getPopulation().getSize();

            for (int i = 0; i < N - 1; i++) {
                indI = sortedIndividuals.get(i);
                indI1 = sortedIndividuals.get(i + 1);

                fI = indI.getFitness();
                fI1 = indI1.getFitness();

                fWorst = (fWorst < fI ? fI : fWorst);
                fWorst = (fWorst < fI1 ? fI1 : fWorst);
                fBest = (fBest > fI ? fI : fBest);
                fBest = (fBest > fI1 ? fI1 : fBest);
                convergence += Math.log(1.0 + Math.abs(fI - fI1));

            }

            VMD.add(Math.abs(fWorst - fBest) / (N - 1));
            fWorstList.add(fWorst);
            fBestList.add(fBest);
            convergenceList.add(convergence);
            FESList.add(algorithm.getFEs());

            fWorst = -Double.MAX_VALUE;
            fBest = Double.MAX_VALUE;
        } else if (algorithm.getState() == EvolutionaryAlgorithm.FINAL_STATE) {
            N = algorithm.getPopulation().getSize();

            double finalVMD = Math.abs(fWorst - fBest) / (N - 1);

            for (int i = 0; i < convergenceList.size(); i++) {
                convergence = 1.0 - convergenceList.get(i) / VMD.get(i);
                super.getLog().println(FESList.get(i)
                        + "\t" + convergence
                        + "\t" + convergenceList.get(i)
                        + "\t" + VMD.get(i));

            }
        }

    }

    @Override
    public String getLogID() {
        return "phenotypicconvergence"; //To change body of generated methods, choose Tools | Templates.
    }

}
