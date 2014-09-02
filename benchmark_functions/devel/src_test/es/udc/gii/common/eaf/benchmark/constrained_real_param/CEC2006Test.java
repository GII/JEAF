/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.constrained_real_param;

import es.udc.gii.common.eaf.benchmark.LoadFromFile;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 *
 * @author pilar
 */
public class CEC2006Test {

    public static final double epsilon = 1e-4d;
    private static final String best_known_solution_file = "/cec2006data/best_known_solution.txt";

    public static final double error(double expected, double result) {
        double return_val;
        double diff = Math.abs(expected - result);

        return_val = (expected != 0.0 ? diff / expected : diff);

        return return_val;
    }

    public static final double[] loadBestKnownSolution(int function_number) {

        double[] best_known_solution = null;
        String function = "x";
        String line;
        String[] line_split, solution;

        try {
            InputStream is = CEC2006Test.class.getResourceAsStream(best_known_solution_file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader brSrc = new BufferedReader(isr);


            function += (function_number < 10 ? "0" + function_number : function_number);

            while ((line = brSrc.readLine()) != null) {

                line_split = line.split("=");

                //Si es la funcin que nos corresponde:
                if ((line_split[0].trim()).equalsIgnoreCase(function)) {

                    solution = line_split[1].split("\t");
                    best_known_solution = new double[solution.length];

                    for (int i = 0; i < best_known_solution.length; i++) {
                        best_known_solution[i] = Double.parseDouble(solution[i]);
                    }
                    break;

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }


        return best_known_solution;
    }

    public static final double loadBestKnownResult(int function_number) {

        double best_known_result = Double.MAX_VALUE;
        String function = "f";
        String line;
        String[] line_split;
        StringTokenizer tokenizer;

        try {
            InputStream is = CEC2006Test.class.getResourceAsStream(best_known_solution_file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader brSrc = new BufferedReader(isr);


            function += (function_number < 10 ? "0" + function_number : function_number);

            while ((line = brSrc.readLine()) != null) {

                line_split = line.split("=");

                //Si es la funcin que nos corresponde:
                if ((line_split[0].trim()).equalsIgnoreCase(function)) {

                    best_known_result = Double.parseDouble(line_split[1].trim());
                    break;

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }


        return best_known_result;
    }
}
