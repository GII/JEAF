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
public class DEDFunction extends BenchmarkObjectiveFunction {

    protected double[] upper_bounds;
    
    protected double[] lower_bounds;
    
    protected int No_of_Load_Hours;
    
    protected int No_of_Units;
    
    //In MW
    protected double[] Power_Demand;
    
    protected double[][] Data1;
    
    protected double[][] Data2;
    //Loss-Coefficients:
    protected double[][] B1;
    
    protected double power_balance_penalty_factor;
    protected double capacity_limits_penalty_factor;
    protected double ramp_limits_penalty_factor;
    protected double poz_penalty_factor;

    
    public DEDFunction() {}
    
    public DEDFunction(int No_of_Units, int No_of_Load_Hours, double[] lower_bounds, 
            double[] upper_bounds, double[] Power_Demand, double[][] Data1, double[][] Data2,
            double[][] B1) {
        
        this.No_of_Units = No_of_Units;
        this.No_of_Load_Hours = No_of_Load_Hours;
        this.lower_bounds = lower_bounds;
        this.upper_bounds = upper_bounds;
        this.Power_Demand = Power_Demand;
        this.Data1 = Data1;
        this.Data2 = Data2;
        this.B1 = B1;
    }
    
    @Override
    public double[][] getOptimum(int dim) {
        return null;
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        values = denormalize(values);

        
        RealMatrix Input_Generations = new Array2DRowRealMatrix(No_of_Load_Hours, No_of_Units);
        for (int i = 0; i < values.length; i++) {

            Input_Generations.setEntry(i / No_of_Units, i % No_of_Units, values[i]);

        }

        //INITIALIZATIONS:
        //Pmin y Pmax son Lower_Bounds y Upper_Bounds:
        RealMatrix a, b, c, e, f, Previous_Generations, Up_Ramp, Down_Ramp, Pmin, Pmax;
        a = new Array2DRowRealMatrix(1, No_of_Units);
        b = new Array2DRowRealMatrix(1, No_of_Units);
        c = new Array2DRowRealMatrix(1, No_of_Units);
        e = new Array2DRowRealMatrix(1, No_of_Units);
        f = new Array2DRowRealMatrix(1, No_of_Units);
        Previous_Generations = new Array2DRowRealMatrix(1, No_of_Units);
        Up_Ramp = new Array2DRowRealMatrix(1, No_of_Units);
        Down_Ramp = new Array2DRowRealMatrix(1, No_of_Units);
        Pmin = new Array2DRowRealMatrix(1, No_of_Units);
        Pmin.setRow(0, this.lower_bounds);
        Pmax = new Array2DRowRealMatrix(1, No_of_Units);
        Pmax.setRow(0, this.upper_bounds);

        RealMatrix Prohibited_Operating_Zones_POZ = new Array2DRowRealMatrix(4, No_of_Units);

        for (int i = 0; i < Data1.length; i++) {

            a.setEntry(0, i, Data1[i][2]);
            b.setEntry(0, i, Data1[i][3]);
            c.setEntry(0, i, Data1[i][4]);
            e.setEntry(0, i, Data1[i][5]);
            f.setEntry(0, i, Data1[i][6]);

            Previous_Generations.addToEntry(0, i, Data2[i][0]);
            Up_Ramp.addToEntry(0, i, Data2[i][1]);
            Down_Ramp.setEntry(0, i, Data2[i][2]);


            for (int j = 3; j < Data2[i].length; j++) {
                Prohibited_Operating_Zones_POZ.setEntry(j - 3, i, Data2[i][j]);
            }

        }

        int No_of_POZ_Limits = Prohibited_Operating_Zones_POZ.getRowDimension();
        RealMatrix POZ_Lower_Limits, POZ_Upper_Limits;
        POZ_Lower_Limits = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);
        POZ_Upper_Limits = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);

        for (int i = 0; i < No_of_POZ_Limits; i++) {

            if (i < No_of_POZ_Limits / 2) {
                POZ_Lower_Limits.setRow(i, Prohibited_Operating_Zones_POZ.getRow(i));
            } else {
                POZ_Upper_Limits.setRow(i - No_of_POZ_Limits / 2, Prohibited_Operating_Zones_POZ.getRow(i));
            }
        }

        RealMatrix Power_Balance_Penalty = new Array2DRowRealMatrix(No_of_Load_Hours, 1);
        RealMatrix Capacity_Limits_Penalty = new Array2DRowRealMatrix(No_of_Load_Hours, 1);
        RealMatrix Up_Ramp_Limit = new Array2DRowRealMatrix(No_of_Load_Hours, No_of_Units);
        RealMatrix Down_Ramp_Limit = new Array2DRowRealMatrix(No_of_Load_Hours, No_of_Units);
        RealMatrix Ramp_Limits_Penalty = new Array2DRowRealMatrix(No_of_Load_Hours, 1);
        RealMatrix POZ_Penalty = new Array2DRowRealMatrix(No_of_Load_Hours, 1);
        RealMatrix All_Penalty = new Array2DRowRealMatrix(No_of_Load_Hours, 1);
        RealMatrix Current_Cost = new Array2DRowRealMatrix(No_of_Load_Hours, 1);
        RealMatrix Power_Loss = new Array2DRowRealMatrix(No_of_Load_Hours, 1);

        RealMatrix x, temp_x, x_t, bool_matrix;

        x = new Array2DRowRealMatrix(1, No_of_Units);

        RealMatrix B1_M = new Array2DRowRealMatrix(B1);
        RealMatrix B2 = new Array2DRowRealMatrix(1, No_of_Units);

        double B3 = 0.0;
        double sum_min, sum_max;

        for (int j = 0; j < No_of_Load_Hours; j++) {
            sum_min = sum_max = 0.0;

            x.setRow(0, Input_Generations.getRow(j));
            x_t = x.transpose();

            Power_Loss.setEntry(j, 0, ((x.multiply(B1_M)).multiply(x_t)).getEntry(0, 0)
                    + (B2.multiply(x_t)).getEntry(0, 0) + B3);
            //Redondear a cuatro decimales como en Matlab:
            Power_Loss.setEntry(j, 0, Math.round(Power_Loss.getEntry(j, 0) * 10000) / 10000.0);
            //Power Balance Penalty Calculation:
            Power_Balance_Penalty.setEntry(j, 0, Math.abs(Power_Demand[j] + Power_Loss.getEntry(j, 0)
                    - StatUtils.sum(x.getRow(0))));
            //Capacity Limits Penalty Calculation:
            sum_min = StatUtils.sum((((x.subtract(Pmin)).getRowVector(0).mapAbs()).subtract((x.subtract(Pmin)).getRowVector(0))).toArray());

            sum_max = StatUtils.sum((((Pmax.subtract(x)).getRowVector(0).mapAbs()).subtract((Pmax.subtract(x)).getRowVector(0))).toArray());
            Capacity_Limits_Penalty.setEntry(j, 0, sum_min + sum_max);

            //Ramp Rate Limits Penalty Calculation:
            if (j > 0) {

                sum_min = sum_max = 0.0;


                Up_Ramp_Limit.setRow(j, this.getMinVector(Pmax.getRowVector(0),
                        (Previous_Generations.add(Up_Ramp)).getRowVector(0)).toArray());
                Down_Ramp_Limit.setRow(j, this.getMaxVector(Pmin.getRowVector(0),
                        (Previous_Generations.subtract(Down_Ramp)).getRowVector(0)).toArray());

                sum_min = StatUtils.sum((((x.subtract(Down_Ramp_Limit.getRowMatrix(j))).getRowVector(0).mapAbs()).subtract((x.subtract(Down_Ramp_Limit.getRowMatrix(j))).getRowVector(0))).toArray());

                sum_max = StatUtils.sum((((Up_Ramp_Limit.getRowMatrix(j).subtract(x)).getRowVector(0).mapAbs()).subtract((Up_Ramp_Limit.getRowMatrix(j).subtract(x)).getRowVector(0))).toArray());

                Ramp_Limits_Penalty.setEntry(j, 0, sum_min + sum_max);

            }

            Previous_Generations.setRow(0, x.getRow(0));

            //Prohibited Operating Zones Penalty Calculation:
            //primero hay que crear una matriz temporal donde se copian No_of_Poz_Limits/2x1 copias
            //de x:
            temp_x = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);
            for (int k = 0; k < No_of_POZ_Limits / 2; k++) {
                temp_x.setRow(k, x.getRow(0));
            }
            bool_matrix = new Array2DRowRealMatrix(No_of_POZ_Limits / 2, No_of_Units);
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

            POZ_Penalty.setEntry(j, 0, StatUtils.sum(bool_matrix.getRow(0)));

            RealVector v_c;
            RealVector v1 = a.getRowVector(0).ebeMultiply(x.getRowVector(0).mapPow(2.0));
            RealVector v2 = b.getRowVector(0).ebeMultiply(x.getRowVector(0));
            v_c = (v1.add(v2)).add(c.getRowVector(0));
            RealVector v3 = f.getRowVector(0).ebeMultiply((Pmin.getRowVector(0).subtract(x.getRowVector(0)))).mapSin();
            RealVector v4 = (e.getRowVector(0).ebeMultiply(v3)).mapAbs();
            v_c = v_c.add(v4);

            Current_Cost.setEntry(j, 0, StatUtils.sum(v_c.toArray()));

        }

        //All & Total Penalty Calculation:
        All_Penalty = (((Power_Balance_Penalty.scalarMultiply(this.power_balance_penalty_factor)).add(
                Capacity_Limits_Penalty.scalarMultiply(this.capacity_limits_penalty_factor))).add(
                Ramp_Limits_Penalty.scalarMultiply(this.ramp_limits_penalty_factor))).add(
                POZ_Penalty.scalarMultiply(this.poz_penalty_factor));
        double total_penalty = StatUtils.sum(All_Penalty.getColumn(0));
        double total_cost = StatUtils.sum(Current_Cost.getColumn(0));
        double total_value = total_cost + total_penalty;

        fitness = total_value;

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

    @Override
    public void reset() {
        //Do nothing
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

}
