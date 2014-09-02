/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import java.util.Arrays;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.StatUtils;

/**
 * Problem F08: Transmission Network Expansion Planning (TNEP) Problem.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F08 extends BenchmarkObjectiveFunction {

    private double u_bound = 15.0;
    private double l_bound = 0.0;
    //Data needed for the problem:
    double[] Pgen = {0.5, 0, 1.65, 0.0, 0.0, 5.45};
    double[] Pload = {0.8, 2.4, 0.4, 1.6, 2.4, 0.0};
    double[][] Linedata = {
        {1.0, 1.0, 2.0, 0.4, 1.0, 1.0, Double.POSITIVE_INFINITY, 1.0},
        {2.0, 1.0, 4.0, 0.6, 1.0, 0.8, Double.POSITIVE_INFINITY, 0.0},
        {3.0, 1.0, 5.0, 0.2, 1.0, 1.0, Double.POSITIVE_INFINITY, 0.0},
        {4.0, 2.0, 3.0, 0.2, 1.0, 1.0, Double.POSITIVE_INFINITY, 0.0},
        {5.0, 2.0, 4.0, 0.4, 1.0, 1.0, Double.POSITIVE_INFINITY, 1.0},
        {6.0, 3.0, 5.0, 0.2, 1.0, 1.0, 20.0, 1.0},
        {7.0, 6.0, 2.0, 0.3, 1.0, 1.0, 30.0, 1.0},};
    double[][] Candidate = {
        {1.0, 1.0, 2.0, 0.4, 1.0, 1.0, 40.0, 1.0},
        {2.0, 1.0, 3.0, 0.38, 1.0, 1.0, 38.0, 1.0},
        {3.0, 1.0, 4.0, 0.6, 1.0, 0.8, 60.0, 0.0},
        {4.0, 1.0, 5.0, 0.2, 1.0, 1.0, 20.0, 0.0},
        {5.0, 1.0, 6.0, 0.68, 1.0, 0.7, 68.0, 0.0},
        {6.0, 2.0, 3.0, 0.2, 1.0, 1.0, 20.0, 0.0},
        {7.0, 2.0, 4.0, 0.4, 1.0, 1.0, 40.0, 1.0},
        {8.0, 2.0, 5.0, 0.31, 1.0, 1.0, 31.0, 1.0},
        {9.0, 6.0, 2.0, 0.3, 1.0, 1.0, 30.0, 1.0},
        {10.0, 3.0, 4.0, 0.69, 1.0, 0.82, 59.0, 1.0},
        {11.0, 3.0, 5.0, 0.2, 1.0, 1.0, 20.0, 1.0},
        {12.0, 6.0, 3.0, 0.48, 0.0, 1.0, 48.0, 0.0},
        {13.0, 4.0, 5.0, 0.63, 0.0, 0.75, 63.0, 0.0},
        {14.0, 4.0, 6.0, 0.3, 0.0, 1.0, 30.0, 0.0},
        {15.0, 5.0, 6.0, 0.61, 0.0, 0.78, 61.0, 0.0}
    };

    @Override
    public double[][] getOptimum(int dim) {
        return null;
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double[] sw;
        int n1;
        double[][] Linedata_aux;

        values = denormalize(values);

        sw = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            //En el caso de java es "floor" porque los indices de la matriz de candidatos van de
            //0 a 14
            sw[i] = Math.floor(values[i]);
        }

        n1 = Linedata.length;

        Linedata_aux = new double[Linedata.length + sw.length][Linedata[0].length];

        //Copia de las primeras filas de Linedata en Linedata_aux
        for (int i = 0; i < Linedata.length; i++) {
            Linedata_aux[i] = Linedata[i];
        }

        for (int i = Linedata.length; i < Linedata_aux.length; i++) {
            Linedata_aux[i] = Candidate[(int) sw[i - n1]];
        }

        int n_originalLine = n1;
        int n = Pgen.length;
        double[][] B = new double[n][n];
        int NLine = Linedata_aux.length;

        double[] XLine = new double[NLine];
        double[] pijmax = new double[NLine];
        for (int i = 0; i < NLine; i++) {
            XLine[i] = Linedata_aux[i][3];
            pijmax[i] = Linedata_aux[i][5];
        }

        double[] Tap = new double[n];
        Arrays.fill(Tap, 1.0);

        double[] bline = new double[NLine];
        int k, m;
        for (int i = 0; i < NLine; i++) {

            bline[i] = 1.0 / XLine[i];
            k = (int) Linedata_aux[i][1] - 1;
            m = (int) Linedata_aux[i][2] - 1;

            B[k][m] = B[k][m] - bline[i];
            B[m][k] = B[k][m];
            B[k][k] = B[k][k] + bline[i];
            B[m][m] = B[m][m] + bline[i];
        
        }
        
        B[0][0] = 10000000;
        //X es la inversa de B
        RealMatrix X = new LUDecompositionImpl(new Array2DRowRealMatrix(B)).getSolver().getInverse();
        double[] delP = new double[Pgen.length];
        for (int i = 0; i < delP.length; i++) {
            delP[i] = Pgen[i] - Pload[i];
        }
        
        RealMatrix delta = X.multiply(new Array2DRowRealMatrix(delP));
        double[] pij = new double[NLine];
        
        int i, j;
        for (int kk = 0; kk < NLine; kk++) {
            i = (int) Linedata_aux[kk][1] - 1;
            j = (int) Linedata_aux[kk][2] - 1;
            
            pij[kk] = (delta.getEntry(i, 0) - delta.getEntry(j, 0))/XLine[kk];
        }
        double PIPbase = 0.0;
        for (i = n_originalLine; i < Linedata_aux.length; i++) {
            fitness += Linedata_aux[i][6];
        }
        
        fitness += 30.0;
        
        //Se calcula la penalización:
        double pen = 0.0;
        
        for (i = 0; i < Linedata_aux.length; i++) {
            pen += 5000.0*Math.max(Math.abs(pij[i]) - Linedata_aux[i][5], 0.0);
        }
        
        int num_a;
        for (i = 0; i < Candidate.length; i++) {
            num_a = 0;
            for (j = 0; j < sw.length; j++) {
                if ((int)sw[j] == i) num_a++;
            }
            
            if (num_a > 3) pen += 1000.0;
        }

        fitness += pen;
        return fitness;

    }

    public double denormalize(double value) {

        return (u_bound - l_bound) * (value + 1.0) / 2.0 + l_bound;

    }

    public double normalize(double value) {
        return (value - l_bound) / (u_bound - l_bound) * 2.0 - 1.0;
    }

    public double[] normalize(double[] values) {

        double[] new_values = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            new_values[i] = normalize(values[i]);
        }

        return new_values;

    }

    public double[] denormalize(double[] values) {

        double[] new_values = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            new_values[i] = denormalize(values[i]);
        }

        return new_values;

    }

    @Override
    public void reset() {
        //Do nothing
    }
}
