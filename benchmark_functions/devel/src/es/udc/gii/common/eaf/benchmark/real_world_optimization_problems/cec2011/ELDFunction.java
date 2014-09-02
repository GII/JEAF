/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_world_optimization_problems.cec2011;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class ELDFunction extends BenchmarkObjectiveFunction {

    private double[] lower_bounds;
    private double[] upper_bounds;
    private int No_of_Units;
    private double Power_Demand;
    private double[][] Data_1;
    private double[][] Data_2;
    private double[][] B1;
    private double[] B2;
    private double B3;
    private double power_balance_penalty_factor;
    private double capacity_limit_penalty_factor;
    private double ramp_limit_penalty_factor;
    private double poz_penalty_factor;

    @Override
    public double[][] getOptimum(int dim) {
        return null;
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        values = denormalize(values);

        RealMatrix Data_1_M = new Array2DRowRealMatrix(Data_1);
        RealMatrix Data_2_M = null;

        if (this.Data_2 != null) {
            Data_2_M = new Array2DRowRealMatrix(Data_2);
        }

        RealMatrix B1_M = new Array2DRowRealMatrix(B1);
        RealMatrix B2_M = new Array2DRowRealMatrix(B2);

        //Initializations:
        RealMatrix Pmin = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix Pmax = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix a = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix b = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix c = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix e = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix f = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix Initial_Generations = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix Up_Ramp = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix Down_Ramp = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix Up_Ramp_Limit = new Array2DRowRealMatrix(1, No_of_Units);
        RealMatrix Down_Ramp_Limit = new Array2DRowRealMatrix(1, No_of_Units);


        Pmin.setRow(0, this.lower_bounds);
        Pmax.setRow(0, this.upper_bounds);

        a.setRow(0, Data_1_M.getColumn(2));
        b.setRow(0, Data_1_M.getColumn(3));
        c.setRow(0, Data_1_M.getColumn(4));
        e.setRow(0, (Data_1_M.getColumnDimension() > 5 ? Data_1_M.getColumn(5) : new double[No_of_Units]));
        f.setRow(0, (Data_1_M.getColumnDimension() > 6 ? Data_1_M.getColumn(6) : new double[No_of_Units]));

        //Calculations:
        RealMatrix x = (new Array2DRowRealMatrix(values)).transpose();

        double Power_Loss = (x.multiply(B1_M)).multiply(x.transpose()).getEntry(0, 0)
                + (B2_M.multiply(x)).getEntry(0, 0) + B3;
        Power_Loss = Math.round(Power_Loss * 10000) / 10000.0;
        //Power Balance Penalty Calculation:
        double Power_Balance_Penalty = Math.abs(Power_Demand + Power_Loss - StatUtils.sum(values));
        //Capacity Limits Penalty Calculation:
        double sum_min, sum_max;
        sum_min = StatUtils.sum(
                ((x.subtract(Pmin)).getRowVector(0).mapAbs()).subtract((x.subtract(Pmin)).getRowVector(0)).toArray());
        sum_max = StatUtils.sum(
                ((Pmax.subtract(x)).getRowVector(0).mapAbs()).subtract((Pmax.subtract(x)).getRowVector(0)).toArray());
        double Capacity_Limit_Penalty = sum_min + sum_max;

        double Ramp_Limit_Penalty = 0.0;
        double POZ_Penalty = 0.0;
        if (this.Data_2 != null) {
            
            RealMatrix Prohibited_Operating_Zones_POZ = new Array2DRowRealMatrix(Data_2[0].length - 3, No_of_Units);
            int No_of_POZ_Limits = Prohibited_Operating_Zones_POZ.getRowDimension();
            RealMatrix POZ_Lower_Limits = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);
            RealMatrix POZ_Upper_Limits = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);

            Initial_Generations.setRow(0, Data_2_M.getColumn(0));
            Up_Ramp.setRow(0, Data_2_M.getColumn(1));
            Down_Ramp.setRow(0, Data_2_M.getColumn(2));


            Up_Ramp_Limit.setRow(0, this.getMinVector(Pmax.getRowVector(0),
                    (Initial_Generations.add(Up_Ramp)).getRowVector(0)).toArray());
            Down_Ramp_Limit.setRow(0, this.getMaxVector(Pmin.getRowVector(0),
                    (Initial_Generations.subtract(Down_Ramp)).getRowVector(0)).toArray());


            int low_limits, up_limits;
            low_limits = up_limits = 0;
            for (int i = 0; i < Prohibited_Operating_Zones_POZ.getRowDimension(); i++) {
                Prohibited_Operating_Zones_POZ.setRow(i, Data_2_M.getColumn(i + 3));
                if (i % 2 == 0) {
                    POZ_Lower_Limits.setRow(low_limits++, Prohibited_Operating_Zones_POZ.getRow(i));
                } else {
                    POZ_Upper_Limits.setRow(up_limits++, Prohibited_Operating_Zones_POZ.getRow(i));
                }
            }
            
            sum_min = StatUtils.sum(
                    ((x.subtract(Down_Ramp_Limit)).getRowVector(0).mapAbs()).subtract((x.subtract(Down_Ramp_Limit)).getRowVector(0)).toArray());
            sum_max = StatUtils.sum(
                    ((Up_Ramp_Limit.subtract(x)).getRowVector(0).mapAbs()).subtract((Up_Ramp_Limit.subtract(x)).getRowVector(0)).toArray());
            Ramp_Limit_Penalty = sum_min + sum_max;


            //Prohibited Operating Zones Penalty Calculation:
            //primero hay que crear una matriz temporal donde se copian No_of_Poz_Limits/2x1 copias
            //de x:
            RealMatrix temp_x = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);
            for (int k = 0; k < No_of_POZ_Limits / 2; k++) {
                temp_x.setRow(k, x.getRow(0));
            }
            RealMatrix bool_matrix = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);
            for (int k = 0; k < No_of_POZ_Limits / 2; k++) {

                for (int l = 0; l < No_of_Units; l++) {

                    bool_matrix.setEntry(k, l,
                            (POZ_Lower_Limits.getEntry(k, l) < temp_x.getEntry(k, l)
                            && temp_x.getEntry(k, l) < POZ_Upper_Limits.getEntry(k, l) ? 1 : 0)
                            * Math.min(temp_x.getEntry(k, l) - POZ_Lower_Limits.getEntry(k, l),
                            POZ_Upper_Limits.getEntry(k, l) - temp_x.getEntry(k, l)));

                }

                if (k > 0) {
                    bool_matrix.getRowMatrix(0).add(bool_matrix.getRowMatrix(k));
                }

            }

            POZ_Penalty = StatUtils.sum(bool_matrix.getRow(0));
        }
        double Total_Penalty = this.power_balance_penalty_factor * Power_Balance_Penalty
                + this.capacity_limit_penalty_factor * Capacity_Limit_Penalty
                + this.ramp_limit_penalty_factor * Ramp_Limit_Penalty
                + this.poz_penalty_factor * POZ_Penalty;

        RealVector v_c;
        RealVector v1 = a.getRowVector(0).ebeMultiply(x.getRowVector(0).mapPow(2.0));
        RealVector v2 = b.getRowVector(0).ebeMultiply(x.getRowVector(0));
        v_c = (v1.add(v2)).add(c.getRowVector(0));
        RealVector v3 = f.getRowVector(0).ebeMultiply((Pmin.getRowVector(0).subtract(x.getRowVector(0)))).mapSin();
        RealVector v4 = (e.getRowVector(0).ebeMultiply(v3)).mapAbs();
        v_c = v_c.add(v4);

        double Cost = StatUtils.sum(v_c.toArray());
        double Total_Cost = Cost + Total_Penalty;

        fitness = Total_Cost;

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

    public void setB1(double[][] B1) {
        this.B1 = B1;
    }

    public void setB2(double[] B2) {
        this.B2 = B2;
    }

    public void setB3(double B3) {
        this.B3 = B3;
    }

    public void setData_1(double[][] Data_1) {
        this.Data_1 = Data_1;
    }

    public void setData_2(double[][] Data_2) {
        this.Data_2 = Data_2;
    }

    public void setNo_of_Units(int No_of_Units) {
        this.No_of_Units = No_of_Units;
    }

    public void setPower_Demand(double Power_Demand) {
        this.Power_Demand = Power_Demand;
    }

    public void setCapacity_limit_penalty_factor(double capacity_limit_penalty_factor) {
        this.capacity_limit_penalty_factor = capacity_limit_penalty_factor;
    }

    public void setLower_bounds(double[] lower_bounds) {
        this.lower_bounds = lower_bounds;
    }

    public void setPower_balance_penalty_factor(double power_balance_penalty_factor) {
        this.power_balance_penalty_factor = power_balance_penalty_factor;
    }

    public void setPoz_penalty_factor(double poz_penalty_factor) {
        this.poz_penalty_factor = poz_penalty_factor;
    }

    public void setRamp_limit_penalty_factor(double ramp_limit_penalty_factor) {
        this.ramp_limit_penalty_factor = ramp_limit_penalty_factor;
    }

    public void setUpper_bounds(double[] upper_bounds) {
        this.upper_bounds = upper_bounds;
    }
}
