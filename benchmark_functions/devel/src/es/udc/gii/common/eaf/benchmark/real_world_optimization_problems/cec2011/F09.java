/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.stat.StatUtils;

/**
 * Problem F08: Transmission Network Expansion Planning (TNEP) Problem.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class F09 extends BenchmarkObjectiveFunction {

    private double[] l_bound;
    private double[] u_bound;
    private double basemva = 100.0;
    private double accuracy = 0.0001;
    private int maxiter = 10;
    private double[][] bus_spec = {
        {1, 1, 1.05, 0.0, 0.0, 0.0, 165.9, 0, 0, 0, 0.000},
        {2, 2, 1.043, 0.0, 21.7, 12.7, 49.1, 28.4, -40, 50, 0.000},
        {3, 0, 1.00, 0.0, 2.4, 1.2, 0.0, 0.0, 0, 0, 0.000},
        {4, 0, 1.00, 0.0, 7.6, 1.6, 0.0, 0.0, 0, 0, 0.0},
        {5, 2, 1.01, 0.0, 94.2, 19.0, 21.6, 28, -40, 40, 0.0},
        {6, 0, 1.00, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0},
        {7, 0, 1.00, 0.0, 22.8, 10.9, 0.0, 0.0, 0, 0, 0.0},
        {8, 2, 1.01, 0.0, 30.0, 30.0, 22.8, 39.1, -6, 24, 0.0},
        {9, 0, 1.00, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0},
        {10, 0, 1.00, 0.0, 5.8, 2.0, 0.0, 0.0, -6, 24, 0.0},
        {11, 2, 1.082, 0.0, 0.0, 0.0, 12.4, 31.6, 0, 0, 0.0},
        {12, 0, 1.00, 0.0, 11.2, 7.5, 0.0, 0.0, 0, 0, 0.0},
        {13, 2, 1.071, 0.0, 0.0, 0.0, 11.6, 45.7, 0, 0, 0.0},
        {14, 0, 1.0, 0.0, 6.2, 1.6, 0.0, 0.0, 0, 0, 0.0},
        {15, 0, 1.0, 0.0, 8.2, 2.5, 0.0, 0.0, 0, 0, 0.0},
        {16, 0, 1.0, 0.0, 3.5, 1.8, 0.0, 0.0, 0, 0, 0.0},
        {17, 0, 1.0, 0.0, 9.0, 5.8, 0.0, 0.0, 0, 0, 0.0},
        {18, 0, 1.0, 0.0, 3.2, 0.9, 0.0, 0.0, 0, 0, 0.0},
        {19, 0, 1.0, 0.0, 9.5, 3.4, 0.0, 0.0, 0, 0, 0.0},
        {20, 0, 1.0, 0.0, 2.2, 0.7, 0.0, 0.0, 0, 0, 0.0},
        {21, 0, 1.0, 0.0, 17.5, 11.2, 0.0, 0.0, 0, 0, 0.0},
        {22, 0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0},
        {23, 0, 1.0, 0.0, 3.2, 1.6, 0.0, 0.0, 0, 0, 0.0},
        {24, 0, 1.0, 0.0, 8.7, 6.7, 0.0, 0.0, 0, 0, 0.0},
        {25, 0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0},
        {26, 0, 1.0, 0.0, 3.5, 2.3, 0.0, 0.0, 0, 0, 0.0},
        {27, 0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0},
        {28, 0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0},
        {29, 0, 1.0, 0.0, 2.4, 0.9, 0.0, 0.0, 0, 0, 0.0},
        {30, 0, 1.0, 0.0, 10.6, 1.9, 0.0, 0.0, 0, 0, 0.0}
    };
    // 2 and 3 are the components of a complex number
    private double[][] linedata = {
        {1, 2, 0.0192, 0.0575, 0.0264, 1, 130.00},
        {1, 3, 0.0452, 0.1652, 0.0204, 1, 130.0000},
        {2, 4, 0.0570, 0.1737, 0.0184, 1, 65.0000},
        {3, 4, 0.0132, 0.0379, 0.0042, 1, 130},
        {2, 5, 0.0472, 0.1983, 0.0209, 1, 130},
        {2, 6, 0.0581, 0.1763, 0.0187, 1, 65},
        {4, 6, 0.0119, 0.0414, 0.0045, 1, 90},
        {7, 5, 0.0460, 0.1160, 0.0102, 1, 70},
        {6, 7, 0.0267, 0.0820, 0.0085, 1, 130},
        {6, 8, 0.0120, 0.0420, 0.0045, 1, 32},
        {6, 9, 0.0000, 0.2080, 0.0, 1, 65},
        {6, 10, 0.0000, 0.5560, 0.0, 1, 32},
        {11, 9, 0.0000, 0.2080, 0.0, 1, 65},
        {9, 10, 0.0000, 0.1100, 0.0, 1, 65},
        {4, 12, 0.0000, 0.2560, 0.0, 1, 65},
        {13, 12, 0.0000, 0.1400, 0.0, 1, 65},
        {12, 14, 0.1231, 0.2559, 0.0, 1, 32},
        {12, 15, 0.0662, 0.1304, 0.0, 1, 32},
        {12, 16, 0.0945, 0.1987, 0.0, 1, 32},
        {14, 15, 0.2210, 0.1997, 0.0, 1, 16},
        {16, 17, 0.0524, 0.1923, 0.0, 1, 16},
        {15, 18, 0.1073, 0.2185, 0.0, 1, 16},
        {18, 19, 0.0639, 0.1292, 0.0, 1, 16},
        {20, 19, 0.0340, 0.0680, 0.0, 1, 32},
        {10, 20, 0.0936, 0.2090, 0.0, 1, 32},
        {10, 17, 0.0324, 0.0845, 0.0, 1, 32},
        {10, 21, 0.0348, 0.0749, 0.0, 1, 32},
        {10, 22, 0.0727, 0.1499, 0.0, 1, 32},
        {22, 21, 0.0116, 0.0236, 0.0, 1, 32},
        {15, 23, 0.1000, 0.2020, 0.0, 1, 16},
        {22, 24, 0.1150, 0.1790, 0.0, 1, 16},
        {23, 24, 0.1320, 0.2700, 0.0, 1, 16},
        {25, 24, 0.1885, 0.3292, 0.0, 1, 16},
        {25, 26, 0.2544, 0.3800, 0.0, 1, 16},
        {27, 25, 0.1093, 0.2087, 0.0, 1, 16},
        {28, 27, 0.0000, 0.3960, 0.0, 1, 65},
        {27, 29, 0.2198, 0.4153, 0.0, 1, 16},
        {27, 30, 0.3202, 0.6027, 0.0, 1, 16},
        {29, 30, 0.2399, 0.4533, 0.0, 1, 16},
        {8, 28, 0.0636, 0.2000, 0.0214, 1, 32},
        {6, 28, 0.0169, 0.0599, 0.0065, 1, 32}
    };
    private double tc = 283.4 * 4.52 * 2;
    private double[] Rg = {32.7290, 32.1122, 30.3532, 33.6474, 64.1156, 66.8729};
    private double[] Rd = {7.5449, 10.7964, 10.9944, 11.0402, 11.7990, 15.3803, 42.6800, 41.4551,
        73.1939, 57.0430, 45.5920, 43.6553, 61.8002, 59.6409, 57.0279,
        51.0749, 67.1070, 60.6623, 198.6744, 178.9956, 199.9483};

    @Override
    public double[][] getOptimum(int dim) {
        return null;
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        RealMatrix bus_spec_M, linedata_M;

        bus_spec_M = new Array2DRowRealMatrix(bus_spec);
        linedata_M = new Array2DRowRealMatrix(linedata);

        double Kp = 100;

        int n = bus_spec_M.getRowDimension();

        RealVector Pg, Pd;

        Pg = new ArrayRealVector(bus_spec_M.getColumn(6));
        Pg = Pg.mapDivide(100.0);

        Pd = new ArrayRealVector(bus_spec_M.getColumn(4));
        Pd = Pd.mapDivide(100.0);

        RealVector na, nb;

        na = new ArrayRealVector(linedata_M.getColumn(0));
        nb = new ArrayRealVector(linedata_M.getColumn(1));

        RealVector g, d;


        List<Integer> g_list, d_list;
        g_list = new ArrayList<Integer>();
        d_list = new ArrayList<Integer>();

        for (int i = 0; i < Pg.getDimension(); i++) {
            if (Pg.getEntry(i) > 0.0) {
                g_list.add(i);
            }

            if (Pd.getEntry(i) > 0.0) {
                d_list.add(i);
            }
        }

        RealVector Pg_aux, Pd_aux;
        Pg_aux = new ArrayRealVector(g_list.size());
        Pd_aux = new ArrayRealVector(d_list.size());
        for (int i = 0; i < g_list.size(); i++) {
            Pg_aux.setEntry(i, Pg.getEntry(g_list.get(i)));
        }
        for (int i = 0; i < d_list.size(); i++) {
            Pd_aux.setEntry(i, Pd.getEntry(d_list.get(i)));
        }

        Pg = new ArrayRealVector(Pg_aux);
        Pd = new ArrayRealVector(Pd_aux);


        RealMatrix BT = new Array2DRowRealMatrix(g_list.size(), d_list.size());
        // 100 MW BILATERAL TRANSACTION   %%%%%%%%%%%%%

        // BT(1,4)=40;BT(1,5)=10;BT(1,6)=15;
        // BT(2,3)=5;BT(2,14)=5;
        // BT(3,21)=2.5;
        // BT(4,21)=2.5;BT(4,16)=15;
        // BT(5,12)=2.5;BT(6,8)=2.5;

        //   50 MW BILATERAL TRANSACTION   %%%%%%%%%%%%%
        BT.setEntry(0, 3, 5);
        BT.setEntry(0, 4, 10);
        BT.setEntry(0, 5, 5);
        BT.setEntry(1, 2, 5);
        BT.setEntry(2, 20, 2.5);
        BT.setEntry(3, 20, 2.5);
        BT.setEntry(3, 15, 15);
        BT.setEntry(4, 11, 2.5);
        BT.setEntry(5, 7, 2.5);

        BT = BT.scalarMultiply(1 / 100.0);

        //Pg2  es un vector columna con la suma de las columnas de BT
        //Pd2 es un vector fila con la suma de las filas de BT
        RealVector Pg2, Pd2;
        Pg2 = new ArrayRealVector(g_list.size());
        Pd2 = new ArrayRealVector(d_list.size());

        //Pg2 -> generations involved in BT
        for (int i = 0; i < BT.getRowDimension(); i++) {
            Pg2.setEntry(i, StatUtils.sum(BT.getRow(i)));
        }

        //Pd2 -> loads involved in BT
        for (int i = 0; i < BT.getColumnDimension(); i++) {
            Pd2.setEntry(i, StatUtils.sum(BT.getColumn(i)));
        }

        calculateBounds(BT, Pd, Pg);
        //Una vez que tenemos los límites se desnormaliza:
        values = denormalize(values);

        //GD es una matrix de g*d elementos, se componen las filas a partir del vector de valores,
        //es aquí donde hay que desnormalizar:
        RealMatrix GD = new Array2DRowRealMatrix(g_list.size(), d_list.size());

        int row, column;

        for (int i = 0; i < values.length; i++) {

            row = i / Pd.getDimension();
            column = i % Pd.getDimension();
            GD.setEntry(row, column, values[i]);

        }

        //Calculation of PTDF:
        RealMatrix line_data_M = new Array2DRowRealMatrix(this.linedata);
        RealMatrix YIBMatrix = this.EBEformYBus(line_data_M, n);
        //Elimino la primera fila y la primera columna:
        YIBMatrix = YIBMatrix.getSubMatrix(1, n - 1, 1, n - 1);

        RealMatrix Xt = new LUDecompositionImpl(YIBMatrix).getSolver().getInverse();
        RealMatrix X = new Array2DRowRealMatrix(n, n);

        for (int i = -1; i < n - 1; i++) {

            for (int j = -1; j < n - 1; j++) {

                if (i != -1 && j != -1) {
                    X.setEntry(i + 1, j + 1, Xt.getEntry(i, j));
                } else {
                    X.setEntry(i + 1, j + 1, 0.0);
                }

            }

        }

        // PTDF es una matriz tridimensional --> la implementó como una lista de listas de listas de doubles:
        RealVector xij = new ArrayRealVector(line_data_M.getColumn(3));

        List<List<List<Double>>> PTDF = new ArrayList<List<List<Double>>>();
        for (int i = 0; i < g_list.size(); i++) {

            PTDF.add(new ArrayList<List<Double>>());
            for (int j = 0; j < d_list.size(); j++) {

                PTDF.get(i).add(new ArrayList<Double>());

                for (int k = 0; k < na.getDimension(); k++) {

                    PTDF.get(i).get(j).add(
                            (X.getEntry((int) na.getEntry(k) - 1, (int) g_list.get(i))
                            - X.getEntry((int) nb.getEntry(k) - 1, (int) g_list.get(i))
                            - X.getEntry((int) na.getEntry(k) - 1, (int) d_list.get(j))
                            + X.getEntry((int) nb.getEntry(k) - 1, (int) d_list.get(j))) / xij.getEntry(k));

                }

            }

        }

        // calculation of objective fn.

        RealVector FC = new ArrayRealVector(xij.getDimension());
        FC = (xij.mapMultiply(100.0)).mapDivide(StatUtils.sum(xij.getData()));

        RealVector flows, cost_line;

        flows = new ArrayRealVector(na.getDimension());
        cost_line = new ArrayRealVector(na.getDimension());
        double aux_value;

        for (int k = 0; k < na.getDimension(); k++) {

            for (int i = 0; i < g_list.size(); i++) {

                for (int j = 0; j < d_list.size(); j++) {

                    aux_value = flows.getEntry(k);
                    aux_value += Math.abs(PTDF.get(i).get(j).get(k) * GD.getEntry(i, j))
                            + Math.abs(PTDF.get(i).get(j).get(k) * BT.getEntry(i, j));

                    flows.setEntry(k, aux_value);



                }

            }
            cost_line.setEntry(k, FC.getEntry(k) / flows.getEntry(k));

        }

        RealVector Pg_m = Pg.subtract(Pg2);
        RealVector Pd_m = Pd.subtract(Pd2);

        double rate_ebe1 = 0.0;

        RealMatrix cost_l = new Array2DRowRealMatrix(g_list.size(), d_list.size());
        RealVector cost_gen = new ArrayRealVector(g_list.size());

        for (int i = 0; i < g_list.size(); i++) {

            for (int j = 0; j < d_list.size(); j++) {

                for (int k = 0; k < na.getDimension(); k++) {

                    cost_l.setEntry(i, j, cost_l.getEntry(i, j)
                            + Math.abs(cost_line.getEntry(k) * PTDF.get(i).get(j).get(k)));

                }
                cost_gen.setEntry(i, cost_gen.getEntry(i) + GD.getEntry(i, j) * cost_l.getEntry(i, j));

            }

            rate_ebe1 += Math.pow(cost_gen.getEntry(i) / Pg_m.getEntry(i) - Rg[i], 2.0);
        }


        double rate_ebe2 = 0.0;
        RealVector cost_load = new ArrayRealVector(d_list.size());

        for (int j = 0; j < d_list.size(); j++) {

            for (int i = 0; i < g_list.size(); i++) {

                cost_load.setEntry(j, cost_load.getEntry(j)
                        + GD.getEntry(i, j) * cost_l.getEntry(i, j));

            }

            rate_ebe2 += Math.pow(cost_load.getEntry(j) / Pd_m.getEntry(j) - Rd[j], 2.0);
        }

        double rate_d = rate_ebe1 + rate_ebe2;


        // CONSTRAINT  VIOLATIONS  
        RealVector Pg_x, Pd_x;
        Pg_x = new ArrayRealVector(g_list.size());
        Pd_x = new ArrayRealVector(d_list.size());

        for (int i = 0; i < GD.getRowDimension(); i++) {

            Pg_x.setEntry(i, StatUtils.sum(GD.getRow(i)) + StatUtils.sum(BT.getRow(i)));

        }

        for (int i = 0; i < GD.getColumnDimension(); i++) {

            Pd_x.setEntry(i, StatUtils.sum(GD.getColumn(i)) + StatUtils.sum(BT.getColumn(i)));

        }

        double Gpen = 0.0;
        for (int i = 0; i < g_list.size(); i++) {

            Gpen += 100 * Math.abs(Pg_x.getEntry(i) - Pg.getEntry(i));
        }


        double LDpen = 0.0;

        for (int i = 0; i < d_list.size(); i++) {

            LDpen += 100 * Math.abs(Pd_x.getEntry(i) - Pd.getEntry(i));
        }


        double PENALTY = Gpen + LDpen;

        fitness = rate_d + 50 * Kp * PENALTY;

        return fitness;
    }
//Formation of Ybus
    private RealMatrix EBEformYBus(RealMatrix line_data_M, int n) {

        RealMatrix YBus = new Array2DRowRealMatrix(n, n);


        RealVector busa = new ArrayRealVector(line_data_M.getColumn(0));
        RealVector busb = new ArrayRealVector(line_data_M.getColumn(1));
        int L = busa.getDimension();
        //ZI contains the imaginary part of linedata[2,3], so index 3
        RealVector ZI = new ArrayRealVector(line_data_M.getColumn(3));
        //YI -> ZI inverse:
        double[] ones = new double[ZI.getDimension()];
        Arrays.fill(ones, 1.0);
        RealVector YI = (new ArrayRealVector(ones)).ebeDivide(ZI);

        int m;
        for (int i = 0; i < L; i++) {

            n = (int) busa.getEntry(i) - 1;
            m = (int) busb.getEntry(i) - 1;

            YBus.setEntry(n, n, YBus.getEntry(n, n) + YI.getEntry(i));
            YBus.setEntry(n, m, -YI.getEntry(i) + YBus.getEntry(n, m));
            YBus.setEntry(m, n, -YI.getEntry(i) + YBus.getEntry(m, n));
            YBus.setEntry(m, m, YBus.getEntry(m, m) + YI.getEntry(i));


        }

        return YBus;

    }

    private void calculateBounds(RealMatrix BT, RealVector Pd, RealVector Pg) {

        int dim = BT.getColumnDimension() * BT.getRowDimension();

        //GD_min or lower bounds = 0.0;
        this.l_bound = new double[dim];
        this.u_bound = new double[dim];
        int row, column;

        for (int i = 0; i < dim; i++) {

            row = i / Pd.getDimension();
            column = i % Pd.getDimension();
            this.u_bound[i] = Math.min(Pg.getEntry(row) - BT.getEntry(row, column),
                    Pd.getEntry(column) - BT.getEntry(row, column));

        }


    }

//
//    public double normalize(double value) {
//        return (value - l_bound) / (u_bound - l_bound) * 2.0 - 1.0;
//    }
//
//    public double[] normalize(double[] values) {
//
//        double[] new_values = new double[values.length];
//
//        for (int i = 0; i < values.length; i++) {
//            new_values[i] = normalize(values[i]);
//        }
//
//        return new_values;
//
//    }
//
    public double[] denormalize(double[] values) {

        double[] new_values = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            new_values[i] = (u_bound[i] - l_bound[i]) * ((values[i] + 1.0) / 2.0) + l_bound[i];
        }

        return new_values;

    }

    @Override
    public void reset() {
        //Do nothing
    }
}
