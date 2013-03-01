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
package es.udc.gii.common.eaf.algorithm.operator.reproduction;

import es.udc.gii.common.eaf.algorithm.CMAEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.OperatorException;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class CMASamplePopulationOperator extends ReproductionOperator {

    /*
     * TODO: configure lowerStandardDeviation and UpperStandardDeviarion
     */
    private double[] lowerStandardDeviation;
    private double[] upperStandardDeviation;
    private int countCUpdatesSinceEigenupdate = 0;

    @Override
    public void configure(Configuration conf) {

        super.configure(conf);
        if (conf.containsKey("LowerStandardDeviation")) {
            this.lowerStandardDeviation = parseDouble(conf.getString("LowerStandardDeviation").split(","));
        } else {
            this.lowerStandardDeviation = null;
            ConfWarning w = new ConfWarning("CMASamplePopulationOperator.LowerStandardDeviation",
                    null);
            w.warn();
        }

        if (conf.containsKey("UpperStandardDeviation")) {
            this.upperStandardDeviation = parseDouble(conf.getString("UpperStandardDeviation").split(","));
        } else {
            this.upperStandardDeviation = null;
            ConfWarning w = new ConfWarning("CMASamplePopulationOperator.UpperStandardDeviation",
                    null);
            w.warn();
        }

    }

    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm, List<Individual> individuals) throws OperatorException {

        List<Individual> new_population = new ArrayList<Individual>(individuals.size());
        CMAEvolutionaryAlgorithm alg = (CMAEvolutionaryAlgorithm) algorithm;

        double[] chromosome;

        int N = individuals.get(0).getDimension();

        //latest possibility to generate B and diagD:
        eidgendecomposition(alg, N, 0);



        //ensure minimal and maximal standard deviation:
        double minsqrtdiagC = Math.sqrt(StatUtils.min(alg.diag(alg.getC())));
        double maxsqrtdiagC = Math.sqrt(StatUtils.max(alg.diag(alg.getC())));

        /*
         * TODO: test if it is necessary *
         */
        for (int i = 0; i < individuals.size(); i++) {
            new_population.add((Individual) individuals.get(i).clone());
        }

        if (lowerStandardDeviation != null && lowerStandardDeviation.length > 0) {

            for (int i = 0; i < N; i++) {

                double d = lowerStandardDeviation[Math.min(i, lowerStandardDeviation.length - 1)];

                if (d > alg.getSigma() * minsqrtdiagC) {
                    alg.setSigma(d / minsqrtdiagC);
                }

            }

        }

        if (upperStandardDeviation != null && upperStandardDeviation.length > 0) {

            for (int i = 0; i < N; i++) {

                double d = upperStandardDeviation[Math.min(i, upperStandardDeviation.length - 1)];

                if (d < alg.getSigma() * maxsqrtdiagC) {
                    alg.setSigma(d / maxsqrtdiagC);
                }

            }

        }

        testAndCorrectNumerics(alg, N, new_population);

        double[] artmp = new double[N];

        /*
         * Sample the distribution
         */
        for (int iNk = 0; iNk < alg.getLambda(); iNk++) {

            chromosome = new_population.get(iNk).getChromosomeAt(0);

            if (alg.getFlgDiag()) {

                for (int i = 0; i < N; i++) {

                    chromosome[i] =
                            this.checkBounds(alg,
                            alg.getxMean()[i] + alg.getSigma() * alg.getDiag()[i] * EAFRandom.nextGaussian());

                }

            } else {

                for (int i = 0; i < N; i++) {

                    artmp[i] = alg.getDiag()[i] * EAFRandom.nextGaussian();

                }

                /*
                 * add mutation (sigma * B * (D*z))
                 */
                for (int i = 0; i < N; i++) {

                    double sum = 0.0;
                    for (int j = 0; j < N; j++) {

                        sum += alg.getB()[i][j] * artmp[j];

                    }

                    chromosome[i] = this.checkBounds(alg,
                            alg.getxMean()[i] + alg.getSigma() * sum);

                }

            }

            new_population.get(iNk).setChromosomeAt(0, chromosome);

        }

        return new_population;

    }



    private void eidgendecomposition(CMAEvolutionaryAlgorithm algorithm, int N, int flgforce) {

        if (countCUpdatesSinceEigenupdate == 0 && flgforce < 2) {
            return;
        }

        if (!algorithm.getFlgDiag() && flgforce <= 0
                && countCUpdatesSinceEigenupdate < 1.0 / algorithm.getCcov() / N / 5.0) {
            return;
        }

        if (algorithm.getFlgDiag()) {
            for (int i = 0; i < N; i++) {
                algorithm.getDiag()[i] = Math.sqrt(algorithm.getC()[i][i]);
            }

        } else {
            // set B <- C
            for (int i = 0; i < N; i++) {
                for (int j = 0; j <= i; j++) {
                    algorithm.getB()[i][j] = algorithm.getB()[i][i] = algorithm.getC()[i][i];
                }
            }

            //eigendecomposition:
            double[] offdiag = new double[N];
            tred2(N, algorithm.getB(), algorithm.getDiag(), offdiag);
            tql2(N, algorithm.getDiag(), offdiag, algorithm.getB());
            checkEigenSystem(N, algorithm.getC(), algorithm.getDiag(), algorithm.getB());

            for (int i = 0; i < N; i++) {
                if (algorithm.getDiag()[i] < 0.0) {
                    System.err.println("ERROR - An eigenvalue has become negative.");
                }
                algorithm.getDiag()[i] = Math.sqrt(algorithm.getDiag()[i]);
            }
        }

        /*
         * TODO: Check if necessary
         */
        if (StatUtils.min(algorithm.getDiag()) == 0.0) {
            algorithm.setAxisRatio(Double.POSITIVE_INFINITY);
        } else {
            algorithm.setAxisRatio(StatUtils.max(algorithm.getDiag()) / StatUtils.min(algorithm.getDiag()));
        }

    } //eigendecomposition

    int checkEigenSystem(int N, double C[][], double diag[], double Q[][]) /*
     * exhaustive test of the output of the eigendecomposition needs O(n^3) operations * produces
     * error returns number of detected inaccuracies
     */ {
        /*
         * compute Q diag Q^T and Q Q^T to check
         */
        int i, j, k, res = 0;
        double cc, dd;
        String s;

        for (i = 0; i < N; ++i) {
            for (j = 0; j < N; ++j) {
                for (cc = 0., dd = 0., k = 0; k < N; ++k) {
                    cc += diag[k] * Q[i][k] * Q[j][k];
                    dd += Q[i][k] * Q[j][k];
                }
                /*
                 * check here, is the normalization the right one?
                 */
                if (Math.abs(cc - C[i > j ? i : j][i > j ? j : i]) / Math.sqrt(C[i][i] * C[j][j]) > 1e-10
                        && Math.abs(cc - C[i > j ? i : j][i > j ? j : i]) > 1e-9) { /*
                     * quite large
                     */
                    s = " " + i + " " + j + " " + cc + " " + C[i > j ? i : j][i > j ? j : i] + " " + (cc - C[i > j ? i : j][i > j ? j : i]);
                    System.err.println("WARNING - cmaes_t:Eigen(): imprecise result detected " + s);
                    ++res;
                }
                if (Math.abs(dd - (i == j ? 1 : 0)) > 1e-10) {
                    s = i + " " + j + " " + dd;
                    System.err.println("WARNING - cmaes_t:Eigen(): imprecise result detected (Q not orthog.) " + s);
                    ++res;
                }
            }
        }
        return res;
    }

    private void testAndCorrectNumerics(CMAEvolutionaryAlgorithm algorithm, int N, List<Individual> individuals) {



        /*
         * Flat fitness, Test if funtion values are identical
         */
        /*
         * we should sort the individuals by fitness value
         */
        Collections.sort(individuals, algorithm.getComparator());

        if (algorithm.getGenerations() > 1) {
            if (individuals.get(0).getFitness() == individuals.get((int) Math.min(algorithm.getLambda() - 1,
                    algorithm.getLambda() / 2.0 + 1)).getFitness()) {
                System.err.println("WARNING - Flat fitness landscape, consider reformulation of fitness,"
                        + "step-size increased");
                algorithm.setSigma(algorithm.getSigma() * Math.exp((0.2 + algorithm.getCs() / algorithm.getDamps())));
            }
        }

        /*
         * Align (renormalize) scale C and (consequently sigma)
         */
        /*
         * e.g. for infinite stationary state simulations (noise handling needs to be introduces for
         * that)
         */
        /*
         * handling needs to be introduced for that)
         */
        double fac = 1;
        if (StatUtils.max(algorithm.getDiag()) < 1.0e-6) {
            fac = 1.0 / StatUtils.max(algorithm.getDiag());
        } else if (StatUtils.min(algorithm.getDiag()) > 1.0e+4) {
            fac = 1.0 / StatUtils.min(algorithm.getDiag());
        }

        if (fac != 1.0) {
            algorithm.setSigma(algorithm.getSigma() / fac);
            for (int i = 0; i < N; i++) {
                algorithm.getPc()[i] *= fac;
                algorithm.getDiag()[i] += fac;
                for (int j = 0; j <= i; j++) {
                    algorithm.getC()[i][j] *= fac * fac;
                }
            }
        }

    }

    // Symmetric Householder reduction to tridiagonal form, taken from JAMA package
    private void tred2(int n, double[][] V, double[] d, double[] e) {

        for (int j = 0; j < n; j++) {
            d[j] = V[n - 1][j];
        }

        // Householder reduction to tridiagonal form.

        for (int i = n - 1; i > 0; i--) {

            // Scale to avoid under/overflow.

            double scale = 0.0;
            double h = 0.0;
            for (int k = 0; k < i; k++) {
                scale = scale + Math.abs(d[k]);
            }
            if (scale == 0.0) {
                e[i] = d[i - 1];
                for (int j = 0; j < i; j++) {
                    d[j] = V[i - 1][j];
                    V[i][j] = 0.0;
                    V[j][i] = 0.0;
                }
            } else {

                // Generate Householder vector.

                for (int k = 0; k < i; k++) {
                    d[k] /= scale;
                    h += d[k] * d[k];
                }
                double f = d[i - 1];
                double g = Math.sqrt(h);
                if (f > 0) {
                    g = -g;
                }
                e[i] = scale * g;
                h = h - f * g;
                d[i - 1] = f - g;
                for (int j = 0; j < i; j++) {
                    e[j] = 0.0;
                }

                // Apply similarity transformation to remaining columns.

                for (int j = 0; j < i; j++) {
                    f = d[j];
                    V[j][i] = f;
                    g = e[j] + V[j][j] * f;
                    for (int k = j + 1; k <= i - 1; k++) {
                        g += V[k][j] * d[k];
                        e[k] += V[k][j] * f;
                    }
                    e[j] = g;
                }
                f = 0.0;
                for (int j = 0; j < i; j++) {
                    e[j] /= h;
                    f += e[j] * d[j];
                }
                double hh = f / (h + h);
                for (int j = 0; j < i; j++) {
                    e[j] -= hh * d[j];
                }
                for (int j = 0; j < i; j++) {
                    f = d[j];
                    g = e[j];
                    for (int k = j; k <= i - 1; k++) {
                        V[k][j] -= (f * e[k] + g * d[k]);
                    }
                    d[j] = V[i - 1][j];
                    V[i][j] = 0.0;
                }
            }
            d[i] = h;
        }

        // Accumulate transformations.

        for (int i = 0; i < n - 1; i++) {
            V[n - 1][i] = V[i][i];
            V[i][i] = 1.0;
            double h = d[i + 1];
            if (h != 0.0) {
                for (int k = 0; k <= i; k++) {
                    d[k] = V[k][i + 1] / h;
                }
                for (int j = 0; j <= i; j++) {
                    double g = 0.0;
                    for (int k = 0; k <= i; k++) {
                        g += V[k][i + 1] * V[k][j];
                    }
                    for (int k = 0; k <= i; k++) {
                        V[k][j] -= g * d[k];
                    }
                }
            }
            for (int k = 0; k <= i; k++) {
                V[k][i + 1] = 0.0;
            }
        }
        for (int j = 0; j < n; j++) {
            d[j] = V[n - 1][j];
            V[n - 1][j] = 0.0;
        }
        V[n - 1][n - 1] = 1.0;
        e[0] = 0.0;


    }

    // Symmetric tridiagonal QL algorithm, taken from JAMA package.
    private void tql2(int n, double d[], double e[], double V[][]) {

        //  This is derived from the Algol procedures tql2, by
        //  Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
        //  Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
        //  Fortran subroutine in EISPACK.

        for (int i = 1; i < n; i++) {
            e[i - 1] = e[i];
        }
        e[n - 1] = 0.0;

        double f = 0.0;
        double tst1 = 0.0;
        double eps = Math.pow(2.0, -52.0);
        for (int l = 0; l < n; l++) {

            // Find small subdiagonal element

            tst1 = Math.max(tst1, Math.abs(d[l]) + Math.abs(e[l]));
            int m = l;
            while (m < n) {
                if (Math.abs(e[m]) <= eps * tst1) {
                    break;
                }
                m++;
            }

            // If m == l, d[l] is an eigenvalue,
            // otherwise, iterate.

            if (m > l) {
                int iter = 0;
                do {
                    iter = iter + 1;  // (Could check iteration count here.)

                    // Compute implicit shift

                    double g = d[l];
                    double p = (d[l + 1] - g) / (2.0 * e[l]);
                    double r = hypot(p, 1.0);
                    if (p < 0) {
                        r = -r;
                    }
                    d[l] = e[l] / (p + r);
                    d[l + 1] = e[l] * (p + r);
                    double dl1 = d[l + 1];
                    double h = g - d[l];
                    for (int i = l + 2; i < n; i++) {
                        d[i] -= h;
                    }
                    f = f + h;

                    // Implicit QL transformation.

                    p = d[m];
                    double c = 1.0;
                    double c2 = c;
                    double c3 = c;
                    double el1 = e[l + 1];
                    double s = 0.0;
                    double s2 = 0.0;
                    for (int i = m - 1; i >= l; i--) {
                        c3 = c2;
                        c2 = c;
                        s2 = s;
                        g = c * e[i];
                        h = c * p;
                        r = hypot(p, e[i]);
                        e[i + 1] = s * r;
                        s = e[i] / r;
                        c = p / r;
                        p = c * d[i] - s * g;
                        d[i + 1] = h + s * (c * g + s * d[i]);

                        // Accumulate transformation.

                        for (int k = 0; k < n; k++) {
                            h = V[k][i + 1];
                            V[k][i + 1] = s * V[k][i] + c * h;
                            V[k][i] = c * V[k][i] - s * h;
                        }
                    }
                    p = -s * s2 * c3 * el1 * e[l] / dl1;
                    e[l] = s * p;
                    d[l] = c * p;

                    // Check for convergence.

                } while (Math.abs(e[l]) > eps * tst1);
            }
            d[l] = d[l] + f;
            e[l] = 0.0;
        }

        // Sort eigenvalues and corresponding vectors.

        for (int i = 0; i < n - 1; i++) {
            int k = i;
            double p = d[i];
            for (int j = i + 1; j < n; j++) {
                if (d[j] < p) { // NH find smallest k>i
                    k = j;
                    p = d[j];
                }
            }
            if (k != i) {
                d[k] = d[i]; // swap k and i 
                d[i] = p;
                for (int j = 0; j < n; j++) {
                    p = V[j][i];
                    V[j][i] = V[j][k];
                    V[j][k] = p;
                }
            }
        }
    } // tql2

    private double hypot(double a, double b) {
        double r = 0;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        }
        return r;
    }

    private double[] parseDouble(String[] ars) {
        double[] ard = new double[ars.length];
        for (int i = 0; i < ars.length; ++i) {
            ard[i] = Double.parseDouble(ars[i]);
        }
        return ard;
    }
}