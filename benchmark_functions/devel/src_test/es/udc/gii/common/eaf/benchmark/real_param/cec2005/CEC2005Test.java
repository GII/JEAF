/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_param.cec2005;

import es.udc.gii.common.eaf.benchmark.LoadFromFile;

/**
 *
 * @author rafa
 */
public class CEC2005Test {

    public static final int N = 10;
    public static final double epsilon = 1e-6d;

    protected final double[][] getTestVectors(int funcNumber) {
        double[][] t = new double[N][50];
        LoadFromFile.loadMatrixFromResourceFile(
                CEC2005ObjectiveFunction.prefix + "test_f" + funcNumber +
                "_vectors.txt", N, 50, t);
        return t;
    }

    protected final double[] getResults(int funcNumber) {
        double[] r = new double[N];
        LoadFromFile.loadColumnVectorFromResourceFile(
                CEC2005ObjectiveFunction.prefix + "test_f" + funcNumber +
                "_results.txt", N, r);
        return r;
    }

    protected final double error(double expected, double result) {
        double diff = Math.abs(expected - result);
        return diff / expected;
    }

    protected final void normalize(double[] v, double lB, double uB) {
        for (int i = 0; i < v.length; i++) {
            v[i] = 2.0d * (v[i] - lB) / (uB - lB) - 1.0d;
        }
    }

    protected final double normalize(double v, double lB, double uB) {
        return 2.0d * (v - lB) / (uB - lB) - 1.0d;
    }
}
