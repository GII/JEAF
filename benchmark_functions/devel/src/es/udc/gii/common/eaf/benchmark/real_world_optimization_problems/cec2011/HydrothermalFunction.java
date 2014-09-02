/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import javax.swing.SpinnerDateModel;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.optimization.direct.PowellOptimizer;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class HydrothermalFunction extends BenchmarkObjectiveFunction {

    private double[] lower_bounds = {5.0, 6.0, 10.0, 13.0};
    private double[] upper_bounds = {15.0, 15.0, 30.0, 25.0};
    private int No_of_Units = 4;
    private int No_of_Load_Hours = 24;
    private double[] Power_Demand = {1370, 1390, 1360, 1290, 1290, 1410, 1650, 2000, 2240,
        2320, 2230, 2310, 2230, 2200, 2130, 2070, 2130, 2140, 2240, 2280, 2240, 2120, 1850, 1590};
    private double[][] C_Coefficients = {{-0.0042, -0.42, 0.030, 0.90, 10.0, -50},
        {-0.0040, -0.30, 0.015, 1.14, 9.5, -70},
        {-0.0016, -0.30, 0.014, 0.55, 5.5, -40},
        {-0.0030, -0.31, 0.027, 1.44, 14.0, -90}};
    private double[][] Inflow_Rate = {{10, 9, 8, 7, 6, 7, 8, 9, 10, 11, 12, 10, 11, 12, 11, 10, 9, 8, 7, 6, 7, 8, 9, 10},
        {8, 8, 9, 9, 8, 7, 6, 7, 8, 9, 9, 8, 8, 9, 9, 8, 7, 6, 7, 8, 9, 9, 8, 8},
        {8.1, 8.2, 4, 2, 3, 4, 3, 2, 1, 1, 1, 2, 4, 3, 3, 2, 2, 2, 1, 1, 2, 2, 1, 0},
        {2.8, 2.4, 1.6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    private double Ptmin = 500.0;
    private double Ptmax = 2500.0;
    private double[] PHmin = {0.0, 0.0, 0.0, 0.0};
    private double[] PHmax = {500.0, 500.0, 500.0, 500.0};
    private double[] Delay_Time = {2, 3, 4, 0};
    private double[] No_of_Upstreams = {0, 0, 2, 1};
    private double[] Vmin = {80, 60, 100, 70};
    private double[] Vmax = {150, 120, 240, 160};
    private double[] V_Initial = {100, 80, 170, 120};
    private double[] V_Final = {120, 70, 170, 140};
    private double[][] Prohibited_Operating_Zones_POZ = {{8, 7, 22, 16},
        {9, 8, 27, 18}};
    private double pbp_factor = 1.0e+4;
    private double clph_factor = 1.0e+4;
    private double clpt_factor = 1.0e+4;
    private double drlp_factor = 1.0e+4;
    private double svlp_factor = 1.0e+5;
    private double pozp_factor = 1.0e+5;
    private double relp_factor = 1.0e+6;

    @Override
    public double[][] getOptimum(int dim) {
        return null;
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        values = denormalize(values);

        RealMatrix Input_Discharges = new Array2DRowRealMatrix(No_of_Units, No_of_Load_Hours);
        for (int i = 0; i < values.length; i++) {
            Input_Discharges.setEntry(i % No_of_Units, i / No_of_Units, values[i]);
        }

        RealMatrix Spillages = new Array2DRowRealMatrix(No_of_Units, No_of_Load_Hours);

        //Initializations:
        RealVector c1, c2, c3, c4, c5, c6;
        RealMatrix C_Coefficients_Matrix = new Array2DRowRealMatrix(C_Coefficients);

        c1 = new ArrayRealVector(C_Coefficients_Matrix.getColumn(0));
        c2 = new ArrayRealVector(C_Coefficients_Matrix.getColumn(1));
        c3 = new ArrayRealVector(C_Coefficients_Matrix.getColumn(2));
        c4 = new ArrayRealVector(C_Coefficients_Matrix.getColumn(3));
        c5 = new ArrayRealVector(C_Coefficients_Matrix.getColumn(4));
        c6 = new ArrayRealVector(C_Coefficients_Matrix.getColumn(5));

        int No_of_POZ_Limits = Prohibited_Operating_Zones_POZ.length;
        RealMatrix POZ_Lower_Limits, POZ_Upper_Limits;
        POZ_Lower_Limits = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, Prohibited_Operating_Zones_POZ[0].length);
        POZ_Upper_Limits = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, Prohibited_Operating_Zones_POZ[0].length);

        for (int i = 0; i < No_of_POZ_Limits; i++) {

            if (i < No_of_POZ_Limits / 2) {
                POZ_Lower_Limits.setRow(i, Prohibited_Operating_Zones_POZ[i]);
            } else {
                POZ_Upper_Limits.setRow(i - No_of_POZ_Limits / 2, Prohibited_Operating_Zones_POZ[i]);
            }
        }

        RealMatrix Storage_Volume = new Array2DRowRealMatrix(No_of_Units, No_of_Load_Hours + 1);
        Storage_Volume.setColumn(0, this.V_Initial);

        int Max_Delay = (int) StatUtils.max(Delay_Time);

        RealMatrix Initial_Discharges, Initial_Spillages;
        Initial_Discharges = new Array2DRowRealMatrix(No_of_Units, Max_Delay);
        Initial_Spillages = new Array2DRowRealMatrix(No_of_Units, Max_Delay);
        RealMatrix All_Discharges, All_Spillages;
        All_Discharges = new Array2DRowRealMatrix(No_of_Units,
                Initial_Discharges.getColumnDimension() + Input_Discharges.getColumnDimension());
        All_Spillages = new Array2DRowRealMatrix(No_of_Units,
                Initial_Spillages.getColumnDimension() + Spillages.getColumnDimension());

        All_Discharges.setSubMatrix(Input_Discharges.getData(), 0, Initial_Discharges.getColumnDimension());

        RealMatrix Upstream_Carry = new Array2DRowRealMatrix(No_of_Units, No_of_Load_Hours);

        double Upstream_Volume = 0.0;
        for (int i = 0; i < No_of_Units; i++) {

            for (int j = 0; j < No_of_Load_Hours; j++) {
                Upstream_Volume = 0.0;
                for (int k = (int) (i - No_of_Upstreams[i]); k < i; k++) {

                    Upstream_Volume += All_Discharges.getEntry(k, j + Max_Delay - (int) Delay_Time[k])
                            + All_Spillages.getEntry(k, j + Max_Delay - (int) Delay_Time[k]);

                }
                Upstream_Carry.setEntry(i, j, Upstream_Volume);
                Storage_Volume.setEntry(i, j + 1, Storage_Volume.getEntry(i, j) + Upstream_Volume
                        - Input_Discharges.getEntry(i, j) - Spillages.getEntry(i, j) + Inflow_Rate[i][j]);

            }
        }

        RealVector Discharge_Rate_Limits_Penalty = new ArrayRealVector(No_of_Load_Hours);
        RealVector Storage_Volume_Limits_Penalty = new ArrayRealVector(No_of_Load_Hours);
        RealVector Capacity_Limits_Penalty_H = new ArrayRealVector(No_of_Load_Hours);
        RealVector Capacity_Limits_Penalty_T = new ArrayRealVector(No_of_Load_Hours);
        RealVector Power_Balance_Penalty = new ArrayRealVector(No_of_Load_Hours);
        RealVector Current_Cost = new ArrayRealVector(No_of_Load_Hours);
        RealVector Power_Loss = new ArrayRealVector(No_of_Load_Hours);
        RealVector POZ_Penalty = new ArrayRealVector(No_of_Load_Hours);

        RealVector Thermal_Generations = new ArrayRealVector(No_of_Load_Hours);
        RealMatrix Hydro_Generations = new Array2DRowRealMatrix(No_of_Load_Hours, No_of_Units);
        RealVector q, v, Qmin, Qmax, V_Min_M, V_Max_M, ph, ph_Max_M, ph_Min_M;
        RealMatrix temp_q, bool_matrix;

        q = new ArrayRealVector(No_of_Units);
        temp_q = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);

        v = new ArrayRealVector(No_of_Units);
        Qmin = new ArrayRealVector(this.lower_bounds);
        Qmax = new ArrayRealVector(this.upper_bounds);
        V_Min_M = new ArrayRealVector(this.Vmin);
        V_Max_M = new ArrayRealVector(this.Vmax);
        ph_Max_M = new ArrayRealVector(this.PHmax);
        ph_Min_M = new ArrayRealVector(this.PHmin);
        ph = new ArrayRealVector(No_of_Units);
        double P_Thermal;
        double sum_min, sum_max;
        for (int i = 0; i < No_of_Load_Hours; i++) {
            q = Input_Discharges.getColumnVector(i);
            v = Storage_Volume.getColumnVector(i + 1);
            //Water Discharge Rate Limits Penalty Calculation 


            sum_min = StatUtils.sum(((q.subtract(Qmin)).mapAbs()).subtract((q.subtract(Qmin))).toArray());
            sum_max = StatUtils.sum(((Qmax.subtract(q)).mapAbs()).subtract((Qmax.subtract(q))).toArray());
            Discharge_Rate_Limits_Penalty.setEntry(i, sum_min + sum_max);
            //Reservoir Storage Volume Limits Penalty Calculation 
            sum_min = StatUtils.sum(((v.subtract(V_Min_M)).mapAbs()).subtract((v.subtract(V_Min_M))).toArray());
            sum_max = StatUtils.sum(((V_Max_M.subtract(v)).mapAbs()).subtract((V_Max_M.subtract(v))).toArray());
            Storage_Volume_Limits_Penalty.setEntry(i, sum_min + sum_max);

            //Prohibited Operating Zones Penalty Calculation
            for (int j = 0; j < temp_q.getRowDimension(); j++) {
                temp_q.setRow(j, q.getData());
            }

            bool_matrix = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);
            for (int k = 0; k < No_of_POZ_Limits / 2; k++) {

                for (int l = 0; l < No_of_Units; l++) {

                    bool_matrix.setEntry(k, l,
                            (POZ_Lower_Limits.getEntry(k, l) < temp_q.getEntry(k, l)
                            && temp_q.getEntry(k, l) < POZ_Upper_Limits.getEntry(k, l) ? 1 : 0)
                            * Math.min(temp_q.getEntry(k, l) - POZ_Lower_Limits.getEntry(k, l),
                            POZ_Upper_Limits.getEntry(k, l) - temp_q.getEntry(k, l)));

                }


                bool_matrix.getRowMatrix(0).add(bool_matrix.getRowMatrix(k));


            }

            POZ_Penalty.setEntry(i, StatUtils.sum(bool_matrix.getRow(0)));


            //Hydro Plants' Generation Calculation:
            ph = (((((c1.ebeMultiply(v.mapPow(2.0))).add(
                    c2.ebeMultiply(q.mapPow(2.0)))).add(
                    c3.ebeMultiply(v.ebeMultiply(q)))).add(
                    c4.ebeMultiply(v))).add(
                    c5.ebeMultiply(q))).add(c6);

            try {
                ph.mapToSelf(new UnivariateRealFunction() {

                    @Override
                    public double value(double d) {

                        return (d > 0 ? d : 0.0);

                    }
                });
            } catch (FunctionEvaluationException ex) {
                System.err.println("#ERROR during the evaluation phase (Class: "
                        + this.getClass().getSimpleName() + ").");
                System.exit(-1);
            }
            Hydro_Generations.setRow(i, ph.getData());

            //Capacity Limits Penalty Calculation of Hydro Plants
            sum_min = StatUtils.sum(((ph.subtract(ph_Min_M)).mapAbs()).subtract((ph.subtract(ph_Min_M))).toArray());
            sum_max = StatUtils.sum(((ph_Max_M.subtract(ph)).mapAbs()).subtract((ph_Max_M.subtract(ph))).toArray());
            Capacity_Limits_Penalty_H.setEntry(i, sum_min + sum_max);

            //Thermal Plant's Generation Calculation
            P_Thermal = Power_Demand[i] + Power_Loss.getEntry(i) - StatUtils.sum(ph.getData());
            Thermal_Generations.setEntry(i, P_Thermal);
            //Capacity Limits Penalty Calculation of Thermal Plants
            Capacity_Limits_Penalty_T.setEntry(i, Math.abs(P_Thermal - Ptmin) - (P_Thermal - Ptmin)
                    + Math.abs(Ptmax - P_Thermal) - (Ptmax - P_Thermal));
            //Power Balance Penalty Calculation 
            Power_Balance_Penalty.setEntry(i, Math.abs(Power_Demand[i] + Power_Loss.getEntry(i)
                    - StatUtils.sum(ph.getData()) - P_Thermal));
            //Cost Calculation % % CHANGE HERE FOR DIFFERENT CASES
            Current_Cost.setEntry(i, 5000 + 19.2 * P_Thermal + 0.002 * (P_Thermal * P_Thermal));
        }

        RealVector All_Penalty;

        All_Penalty = new ArrayRealVector(this.No_of_Load_Hours);

        All_Penalty = (Power_Balance_Penalty.mapMultiply(this.pbp_factor)).add(
                Capacity_Limits_Penalty_H.mapMultiply(this.clph_factor)).add(
                Capacity_Limits_Penalty_T.mapMultiply(this.clpt_factor)).add(
                Discharge_Rate_Limits_Penalty.mapMultiply(this.drlp_factor)).add(
                Storage_Volume_Limits_Penalty.mapMultiply(this.svlp_factor)).add(
                POZ_Penalty.mapMultiply(this.pozp_factor));

        double Reservoir_End_Limits_Penalty;
        double sum_v_ini, sum_v_end;

        sum_v_ini = sum_v_end = 0.0;

        for (int i = 0; i < No_of_Units; i++) {
            sum_v_ini += Math.abs(Storage_Volume.getEntry(i, 0) - V_Initial[i]);
            sum_v_end += Math.abs(Storage_Volume.getEntry(i, Storage_Volume.getColumnDimension() - 1) - V_Final[i]);
        }

        Reservoir_End_Limits_Penalty = sum_v_ini + sum_v_end;

        double Total_Penalty, Total_Cost;

        Total_Penalty = StatUtils.sum(All_Penalty.getData()) + this.relp_factor * Reservoir_End_Limits_Penalty;
        Total_Cost = StatUtils.sum(Current_Cost.getData());

        fitness = Total_Cost + Total_Penalty;

        return fitness;
    }

    private RealVector getMinVector(RealVector v1, RealVector v2) {
        RealVector vMin = new ArrayRealVector(v1.getDimension());

        for (int i = 0; i < vMin.getDimension(); i++) {

            vMin.setEntry(i, Math.min(v1.getEntry(i), v2.getEntry(i)));

        }

        return vMin;
    }

    private RealVector getMaxVector(RealVector v1, RealVector v2) {
        RealVector vMax = new ArrayRealVector(v1.getDimension());

        for (int i = 0; i < vMax.getDimension(); i++) {

            vMax.setEntry(i, Math.max(v1.getEntry(i), v2.getEntry(i)));

        }

        return vMax;
    }

    public double[] normalize(double[] values) {

        //From [lower:upper] to -1:1
        double[] new_values = new double[values.length];
        double u_b, l_b;

        for (int i = 0; i < values.length; i++) {

            u_b = this.upper_bounds[i % this.upper_bounds.length];
            l_b = this.lower_bounds[i % this.lower_bounds.length];

            new_values[i] = (values[i] - l_b) / (u_b - l_b) * 2.0 - 1.0;

            new_values[i] = (Double.isNaN(new_values[i]) ? 0.0 : new_values[i]);

        }

        return new_values;
    }

    public double[] denormalize(double[] values) {

        //From -1:1 to [lower, upper]
        double[] new_values = new double[values.length];
        double u_b, l_b;

        for (int i = 0; i < values.length; i++) {

            u_b = this.upper_bounds[i % this.upper_bounds.length];
            l_b = this.lower_bounds[i % this.lower_bounds.length];

            new_values[i] = (u_b - l_b) * (values[i] + 1.0) / 2.0 + l_b;

        }

        return new_values;

    }

    @Override
    public void reset() {
        //Do nothing
    }

    public void setClph_factor(double clph_factor) {
        this.clph_factor = clph_factor;
    }

    public void setClpt_factor(double clpt_factor) {
        this.clpt_factor = clpt_factor;
    }

    public void setDrlp_factor(double drlp_factor) {
        this.drlp_factor = drlp_factor;
    }

    public void setPbp_factor(double pbp_factor) {
        this.pbp_factor = pbp_factor;
    }

    public void setRelp_factor(double relp_factor) {
        this.relp_factor = relp_factor;
    }

    public void setSvlp_factor(double svlp_factor) {
        this.svlp_factor = svlp_factor;
    }

    public void setPozp_factor(double pozp_factor) {
        this.pozp_factor = pozp_factor;
    }
}
