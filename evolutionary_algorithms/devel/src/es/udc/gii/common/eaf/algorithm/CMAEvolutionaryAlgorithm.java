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
 * CMAEvolutionaryStrategy.java
 *
 * Created on 15 de octubre de 2007, 15:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.plugin.cma.RecombinationType;
import es.udc.gii.common.eaf.plugin.cma.SuperlinearRecombinationType;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.stat.StatUtils;

/**
 * This class represents a CMA-ES algorithm. It is used to store the parameters that are common to
 * the elementes necesary to run it like the operators or the StopTests.<p>
 * 
 * To configure this algorithm it is necessary to indicate the following parameters. None of them are
 * mandatory, if they do not appear in the configuration file, the algorithm set them to their default
 * values with the method <i>setDefaultParameters</i>.
 * 
 * <ul>
 * <li>InitialX: one or N values that indicate the initial value of xMean.</li>
 * <li>TypicalX: one or N values that indicate the initial value of typicalX used to generate the 
 * initial values of xMean.
 * <li>InitialStandardDeviation: one or N values that indicate the initial value of sigma.</li>
 * <li>Mu: an integer value that indicates the number of elements used to update the parameters of
 * the distribution that samples the population at each generation.</li>
 * <li>RecombinationType: Plugin used to generates the weights to update the distribution.</li>
 * <li>Cs: step-size cumulation parameter.</li>
 * <li>Damps: step-size damping parameter.</li>
 * <li>DiagonalCovarianceMatrix: an integer value which indicates the number of initial iterations
 * with diagonal covariance matrix, where 1 means always, -1 means default value 150*N/lambda 
 * and 0 means never.</li>
 * </ul>
 * where N is the dimension of the problem.<p>
 * 
 * As a subclass of EvolutionaryStratety, it is also necessary to indicate the value of the parameters
 * <i>Lambda</i>. Again, it is not necessary to specified its value, in this case the default value
 * is used. 
 * 
 * 
 * See <a href = "http://www.lri.fr/~hansen/cmaesintro.html">http://www.lri.fr/~hansen/cmaesintro.html</a>
 * for more information.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class CMAEvolutionaryAlgorithm extends EvolutionaryStrategy {

    //CMA - Parameters:
    private int mu;
    private double mucov = -1;
    private double mueff;
    private double[] weights;
    private double damps;
    private double cs;
    private double cc;
    private double ccov = -1;
    private double ccovsep = -1;
    private double chiN = -1;
    /**
     * Dimension of the problem to be solved, it is a class variable because most of the methods use
     * it.
     */
    private int N;
    private double[] diagD;
    /**
     * Initial standard deviation:
     */
    private double[] startsigma;
    /**
     * Standard deviation:
     */
    private double sigma;
    /**
     * Axis parallel distribution.
     */
    private double axisratio;
    private double[] typicalX;
    private double[] xMean;
    private double[] pc;
    private double[] ps;
    private double[][] B;
    private double[][] C;
    private double[] bestever_x;
    private double[] artmp;
    private double[] initialX;
    private int diagonalCovarianceMatrix = 0;
    private RecombinationType recombinationType = new SuperlinearRecombinationType();

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);

        try {

            if (conf.containsKey("InitialX")) {

                double x = conf.getDouble("InitialX");
                this.xMean = new double[]{x};

            } else {
                this.xMean = new double[]{0.0};
                ConfWarning w = new ConfWarning("EvolutionaryAlgorithm.InitialX",
                        0.0);
                w.warn();

            }

            if (conf.containsKey("TypicalX")) {
                double x = conf.getDouble("TypicalX");
                this.typicalX = new double[]{x};
            } else {
                this.typicalX = new double[]{0.0};
                ConfWarning w = new ConfWarning("EvolutionaryAlgorithm.TypicalX",
                        0.0);
                w.warn();

            }


            if (conf.containsKey("InitialStandardDeviation")) {

                this.startsigma = new double[]{conf.getDouble("InitialStandardDeviation")};

            } else {

                this.startsigma = new double[]{1.0};
                ConfWarning w = new ConfWarning("EvolutionaryAlgorithm.InitialStandardDeviation",
                        1.0);
                w.warn();

            }

            if (conf.containsKey("Mu")) {

                this.mu = conf.getInt("Mu");

            } else {

                this.mu = -1;
                ConfWarning w = new ConfWarning("EvolutionaryAlgorithm.Mu",
                        this.mu);
                w.warn();

            }

            if (conf.containsKey("RecombinationType")) {

                this.recombinationType = (RecombinationType) Class.forName(conf.getString("RecombinationType.Class")).newInstance();
                this.recombinationType.configure(conf.subset("RecombinationType"));
            } else {
                ConfWarning w = new ConfWarning("EvolutionaryAlgorithm.RecombinationType",
                        this.recombinationType.toString());
                w.warn();
            }

            if (conf.containsKey("Cs")) {
                this.cs = conf.getDouble("Cs");
            } else {
                this.cs = -1;
                ConfWarning w = new ConfWarning("CMAEvolutionaryAlgorithm.Cs",
                        this.cs);
                w.warn();
            }

            if (conf.containsKey("Damps")) {
                this.damps = conf.getDouble("Damps");
            } else {

                this.damps = -1;
                ConfWarning w = new ConfWarning("CMAEvolutionaryAlgorithm.Damps",
                        this.damps);
                w.warn();

            }

            if (conf.containsKey("DiagonalCovarianceMatrix")) {
                this.diagonalCovarianceMatrix = conf.getInt("DiagonalCovarianceMatrix");

            } else {
                this.diagonalCovarianceMatrix = -1;
                ConfWarning w = new ConfWarning("CMAEvolutionaryAlgorithm.DiagonalCovarianceMatrix",
                        this.diagonalCovarianceMatrix);
                w.warn();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

   
    @Override
    protected void init() {



        N = this.getPopulation().getIndividual(0).getDimension();

        //The first call is to set the parameters to default values is they are not set up during
        //configuration.
        setDefaultParameters();

        if (this.getFitnessHistory() == null) {
            this.setFitnessHistoryCapacity((int) (10.0 + 30.0 * N / this.getLambda()));
            this.setFitnessHistory(new ArrayList<Individual>());
        }

        /*
         * Init weights and mueff
         */
        this.weights = this.recombinationType.getWeights(this.getMu());

        //Mueff:
        if (this.weights != null) {
            double sum1, sum2;
            sum1 = sum2 = 0.0;
            for (int i = 0; i < this.mu; i++) {
                sum1 += weights[i];
                sum2 += weights[i] * weights[i];
            }
            this.mueff = sum1 * sum1 / sum2;
        }

        this.diagD = new double[N];
        Arrays.fill(this.diagD, 1.0);

        //The original code set the boundaries (lower and upper here) but in our implementation, it
        //is not necessary to be done, after the samplePopulation call, the boundaries are checked 
        //if "checkBounds" is set to true.

        //Initialization of sigmas:
        if (startsigma != null) {
            if (startsigma.length == 1) {

                this.sigma = startsigma[0];

            } else if (startsigma.length == N) {

                this.sigma = StatUtils.max(startsigma);
                if (this.sigma <= 0.0) {
                    System.err.println("WARNING - Initial Standard Deviation sigma must be positive");

                }
                for (int i = 0; i < N; i++) {
                    this.diagD[i] = startsigma[i] / sigma;
                }

            } else {
                assert false;
            }
        } else {
            System.err.println("WARNING - no inital standard deviation specified or use "
                    + "setInitialStandardDeviation()");
            this.sigma = 0.5;
        }

        if (this.sigma <= 0.0 || StatUtils.min(diagD) <= 0.0) {
            System.err.println("WARNING - initial standard deviation no specified or non-positive,"
                    + "useSetInitialStandardDeviation()");
            this.sigma = 1.0;

        }

        /*
         * Save initial standard deviation
         */
        if (startsigma == null || startsigma.length == 1) {
            startsigma = new double[N];
            for (int i = 0; i < N; i++) {
                startsigma[i] = sigma * diagD[i];
            }
        }

        double maxstartsigma = StatUtils.max(startsigma);
        double minstartsigma = StatUtils.min(startsigma);
        axisratio = maxstartsigma / minstartsigma;

        /*
         * expand TypicalX, might still be null afterwards
         */
        typicalX = expandToDimension(typicalX, N);

        /*
         * Initialization of xmean
         */
        xMean = expandToDimension(xMean, N);
        if (xMean == null) {
            /*
             * set via TypicalX
             */
            if (typicalX != null) {
                xMean = typicalX.clone();

                for (int i = 0; i < N; i++) {
                    xMean[i] += sigma * diagD[i] * EAFRandom.nextGaussian();
                }
            } else if (this.getProblem().isCheckBounds()) {

                System.err.println("WARNING - No initial search point (solution) X or typical X specified");
                xMean = new double[N];
                for (int i = 0; i < N; i++) {

                    double offset = sigma * diagD[i];
                    double range = (2.0 - 2.0 * sigma * diagD[i]);
                    if (offset > 0.4 * 2.0) {
                        offset = 0.4 * 2.0;
                        range = 0.2 * 2.0;
                    }

                    xMean[i] = -1.0 + offset + EAFRandom.nextGaussian() * range;

                }

            } else {
                System.err.println("WARNING - No initial search point (solution) or typical X specidied");
                xMean = new double[N];
                for (int i = 0; i < N; i++) {
                    xMean[i] = EAFRandom.nextDouble();

                }

            }
        }

        if (xMean == null || sigma <= 0.0) {
            System.err.println("ERROR - No correct xMean or sigma values");
            System.exit(-1);
        }

        /*
         * non-settable parameters
         */
        pc = new double[N];
        ps = new double[N];

        B = new double[N][N];
        C = new double[N][N];

        bestever_x = xMean.clone();
        artmp = new double[N];

        /*
         * Initialization
         */
        for (int i = 0; i < N; i++) {
            pc[i] = 0.0;
            ps[i] = 0.0;
            for (int j = 0; j < N; j++) {
                B[i][j] = 0.0;
            }
            for (int j = i; j < N; j++) {
                C[i][j] = 0.0;
            }
            B[i][i] = 1.0;
            C[i][i] = diagD[i] * diagD[i];
        }

        initialX = xMean.clone();

    }

    /*
     * This method is used to set the value of the parameters to their default values when the user
     * does not specified them in the configuration file.
     */
    private void setDefaultParameters() {

        this.chiN = Math.sqrt(N)
                * (1.0 - 1.0 / (4.0 * N) + 1.0 / (21.0 * N * N));

        //set parameters to its default values if they were not set before:
        if (this.getLambda() <= 0) {
            this.setLambda((int) (4.0 + 3.0 * Math.log(N)));
        } 
        this.setPopulationSize(this.getLambda());

        if (this.mu <= 0) {
            this.mu = (int) Math.floor(this.getLambda() / 2.0);
        }

        if (this.weights == null || this.weights.length == 0) {
            this.weights = this.recombinationType.getWeights(this.getMu());
            //Mueff:
            double sum1, sum2;
            sum1 = sum2 = 0.0;
            for (int i = 0; i < this.mu; i++) {
                sum1 += weights[i];
                sum2 += weights[i] * weights[i];
            }
            this.mueff = sum1 * sum1 / sum2;
        }

        if (this.cs <= 0.0) {
            this.cs = (this.mueff + 2.0) / (N + this.mueff + 3.0);
        }

        if (this.damps <= 0.0) {
            this.damps = (1.0 + 2.0 * Math.max(0, Math.sqrt((this.mueff - 1.0) / (N + 1.0)) - 1.0))
                    * Math.max(0.3, 1.0 - N / (1e-6 + Math.min(this.getMaxGenerations(),
                    this.getMaxEvaluations() / this.getLambda()))) + this.cs;


        }

        if (this.cc <= 0.0) {
            this.cc = 4.0 / (N + 4.0);
            
        }

        if (this.mucov < 0.0) {
            this.mucov = this.mueff;
        }

        if (this.ccov < 0) {
            this.ccov = 2.0 / (N + 1.41) / (N + 1.41) / this.mucov + (1 - (1.0 / this.mucov))
                    * Math.min(1, (2.0 * this.mueff - 1) / (this.mueff + (N + 2) * (N + 2)));
            this.ccovsep = Math.min(1, ccov * (N + 1.5) / 3.0);
        }
    }

    /**
     * This is an auxiliar method used to increase the size of an array.
     * @param x The initial array.
     * @param dim the new dimension of the array.
     * @return An array of size <i>dim</i> where the all the elements are equal x[0] if x.length &le; dim
     * the same array x, if x.lenght == dim, or null if x is null.
     */
    private double[] expandToDimension(double[] x, int dim) {
        if (x == null) {
            return null;
        }
        if (x.length == dim) {
            return x;
        }
        if (x.length != 1) {
            System.err.println("ERROR - x must have length one or length dimension");
        }

        return getArrayOf(x[0], dim);
    }

    /**
     * This method returns an array with dim elements, all of them with the same value.
     * @param x value to copy into the array.
     * @param dim the dimension of the returned array.
     * @return an array with <i>dim</i> elements where all the values are equal to x.
     */
    private double[] getArrayOf(double x, int dim) {
        double[] res = new double[dim];
        for (int i = 0; i < dim; ++i) {
            res[i] = x;
        }
        return res;
    }

    //Getters and setters.
    
    public double[] getStartSigma() {
        return this.startsigma;
    }

    public double[] getxMean() {
        return xMean;
    }

    public int getMu() {
        return mu;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getMueff() {
        return mueff;
    }

    public double getSigma() {
        return sigma;
    }

    public double[] getPs() {
        return ps;
    }

    public double getCs() {
        return this.cs;
    }

    public double[] getDiag() {
        return this.diagD;
    }

    /*
     * Recalculate diagonal flag:
     */
    public boolean getFlgDiag() {

        boolean flgdiag = false;

        flgdiag = (this.diagonalCovarianceMatrix == 1
                || diagonalCovarianceMatrix >= this.getGenerations() + 1);
        if (this.diagonalCovarianceMatrix == -1) {
            flgdiag = (this.getGenerations() <= 1 * 150 * N * this.getLambda());
        }

        return flgdiag;
    }

    public double[][] getB() {
        return this.B;
    }

    public double getChiN() {
        return this.chiN;
    }

    public double getCc() {
        return this.cc;
    }

    public double[] getPc() {
        return this.pc;
    }

    public double getCcov() {
        if (this.getFlgDiag())
            return this.ccovsep;
        return this.ccov;
    }

    public double[][] getC() {
        return C;
    }

    public double getMucov() {
        return mucov;
    }

    public double getDamps() {
        return damps;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public void setAxisRatio(double axisratio) {
        this.axisratio = axisratio;
    }

    @Override
    protected void select(Population toPopulation) {
        if (this.getGenerations() > 0) {
            super.select(toPopulation);
        } else {
            //The first generation do not apply updateDistribution:
            toPopulation.setIndividuals(this.getPopulation().getIndividualsCopy());
        }
    }

    @Override
    protected void evaluate(Problem problem, Population population) {
        if (this.getState() == EvolutionaryAlgorithm.INIT_EVALUATE_STATE) {
            //Do nothing
        } else {
            super.evaluate(problem, population);
        }
    }

    public double[] diag(double[][] M) {
        double[] diag = new double[M.length];

        for (int i = 0; i < M.length; i++) {
            diag[i] = M[i][i];
        }

        return diag;
    }

    @Override
    public void setPopulationSize(int new_pop_size) {
        super.setPopulationSize(new_pop_size);
        this.setLambda(new_pop_size);
    }
    
    
}
