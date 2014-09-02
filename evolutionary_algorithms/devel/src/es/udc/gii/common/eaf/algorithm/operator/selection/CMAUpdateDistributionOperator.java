/*
 * Copyright (C) 2012 Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
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
package es.udc.gii.common.eaf.algorithm.operator.selection;

import es.udc.gii.common.eaf.algorithm.CMAEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.OperatorException;
import java.util.Collections;
import java.util.List;

/**
 * This class implements the CMA method "updateDistribution".
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class CMAUpdateDistributionOperator extends SelectionOperator {

    private double[] xOld;
    private double[] BDz;
    
    private boolean iniphase = false;

    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) throws OperatorException {

        CMAEvolutionaryAlgorithm alg = (CMAEvolutionaryAlgorithm) algorithm;

        int N;
        double[] artmp;
        /*
         * sort individuals using fitness value
         */
        Collections.sort(individuals, algorithm.getComparator());



        N = individuals.get(0).getDimension();
        if (xOld == null) {
            xOld = new double[N];

        }

        if (BDz == null) {
            BDz = new double[N];
        }

        /*
         * Calculate xmean and BDz~N(0,C):
         */
        for (int i = 0; i < N; i++) {

            xOld[i] = alg.getxMean()[i];
            alg.getxMean()[i] = 0.0;

            for (int iNk = 0; iNk < alg.getMu(); iNk++) {

                alg.getxMean()[i] += alg.getWeights()[iNk] * individuals.get(iNk).getChromosomeAt(0)[i];


            }

            BDz[i] = Math.sqrt(alg.getMueff()) * (alg.getxMean()[i] - xOld[i]) / alg.getSigma();

        }

        /*
         * Cumulation for sigma (ps) using B*z
         */
        if (alg.getFlgDiag()) {

            /*
             * Given B = I we have B*z = z = D^-1 BDz
             */
            for (int i = 0; i < N; i++) {

                alg.getPs()[i] = (1.0 - alg.getCs()) * alg.getPs()[i]
                        + Math.sqrt(alg.getCs() * (2.0 - alg.getCs())) * BDz[i] / alg.getDiag()[i];

            }

        } else {

            /*
             * Calculate Z := D^-1 * B^-1 * BDZ into artmp
             */
            double sum;
            artmp = new double[N];
            for (int i = 0; i < N; i++) {

                sum = 0.0;
                for (int j = 0; j < N; j++) {

                    sum += alg.getB()[j][i] * BDz[j];
                }
                artmp[i] = sum / alg.getDiag()[i];
            }
            /*
             * cumulation for sigma (ps) using B*z
             */
            for (int i = 0; i < N; i++) {

                sum = 0.0;
                for (int j = 0; j < N; j++) {

                    sum += alg.getB()[i][j] * artmp[j];


                }
                alg.getPs()[i] = (1.0 - alg.getCs()) * alg.getPs()[i]
                        + Math.sqrt(alg.getCs() * (2.0 - alg.getCs())) * sum;

            }


        }

        /*
         * Calculate norm(ps) ^2
         */
        double psxps = 0.0;

        for (int i = 0; i < N; i++) {

            psxps += alg.getPs()[i] * alg.getPs()[i];

        }

        /*
         * Cumulation for covariance matrix (pc) using B*D*z ~ N(0, C)
         */
        int hsig = 0;
        if (Math.sqrt(psxps) / Math.pow(1.0 - alg.getCs(), 2.0 * alg.getGenerations())
                / alg.getChiN() < 1.4 + 2.0 / (N + 1)) {
            hsig = 1;
        }

        for (int i = 0; i < N; i++) {

            alg.getPc()[i] = (1.0 - alg.getCc()) * alg.getPc()[i] + hsig
                    * Math.sqrt(alg.getCc() * (2.0 - alg.getCc())) * BDz[i];

        }


        

        if (iniphase && alg.getGenerations() > Math.min(1 / alg.getCs(), 1 + N / alg.getMucov())) {
            if (psxps / alg.getDamps() / (1.0 - Math.pow((1.0 - alg.getCs()), alg.getGenerations())) < N * 1.05) {
                iniphase = false;
            }
        }

        /*
         * Update of C:
         */
        if (alg.getCcov() > 0.0 && iniphase == false) {

            alg.setCountCupdatesSinceEigenupdate(alg.getCountCupdatesSinceEigenupdate()+1);
            /*
             * Update of covariance matrix
             */
            for (int i = 0; i < N; i++) {

                for (int j = (alg.getFlgDiag() ? i : 0); j <= i; j++) {

                    alg.getC()[i][j] = (1.0 - alg.getCcov()) * alg.getC()[i][j]
                            + alg.getCcov()
                            * (1.0 / alg.getMucov())
                            * (alg.getPc()[i] * alg.getPc()[j] + (1 - hsig) * alg.getCc())
                            * (2.0 - alg.getCc()) * alg.getC()[i][j];
                    for (int k = 0; k < alg.getMu(); k++) {

                        alg.getC()[i][j] += alg.getCcov() * (1.0 - 1.0 / alg.getMucov())
                                * alg.getWeights()[k]
                                * (individuals.get(k).getChromosomeAt(0)[i] - xOld[i])
                                * (individuals.get(k).getChromosomeAt(0)[j] - xOld[j]) / alg.getSigma()
                                / alg.getSigma();

                    }

                }

            }

        }

        /*
         * update of sigma
         */
        double sigma = alg.getSigma();
        sigma *= Math.exp(((Math.sqrt(psxps) / alg.getChiN()) - 1.0) * alg.getCs() / alg.getDamps());
        alg.setSigma(sigma);

        return individuals;


    }

    @Override
    protected Individual select(EvolutionaryAlgorithm algorithm, List<Individual> individuals) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
