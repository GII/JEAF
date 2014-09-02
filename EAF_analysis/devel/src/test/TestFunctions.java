/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.io.File;
import java.util.List;
import util.LoadProblemsFromFile;

/**
 *
 * @author pilar
 */
public class TestFunctions {

    public TestFunctions(String functions_file, int dim) {

        List<Problem> problems;
        double[][] optimums;
        double value;
        int aux_dim;

        EAFRandom.init();

        problems = LoadProblemsFromFile.readProblems(functions_file);

        for (Problem p : problems) {

            //Funciones objetivo:
            if (p.getObjectiveFunctions().size() > 0) {
                System.out.println(" *** Funciones objetivo *** ");
                for (ObjectiveFunction f : p.getObjectiveFunctions()) {

                    System.out.println(f.toString());
                    aux_dim = (f.getDimension() != -Integer.MAX_VALUE) ? f.getDimension() : dim;
                    optimums = ((BenchmarkObjectiveFunction) f).getOptimum(aux_dim);

                    for (double[] o : optimums) {
                        value = f.evaluate(o);
                        System.out.print("f(x*) = " + value + " ");
                        if (Math.abs(value) <= 1.0E-12) {
                            System.out.println("*");
                        } else {
                            System.out.println();
                        }
                    }

                }
            }

            //Restricciones:
            if (p.getConstraints().size() > 0) {
                System.out.println(" *** Restricciones *** ");
                for (Constraint c : p.getConstraints()) {
                    System.out.println(c.toString());
                }
            }

        }

//        for (BenchmarkObjectiveFunction f : functions) {
//
//            System.out.println(f.toString());
//            System.out.println();
//            
//            optimums = f.getOptimum(dim);
//            
//            for (double[] optimum : optimums) {
//                
//                value = f.evaluate(optimum);
//                System.out.print("f(x*) = " + value + " ");
//                if (Math.abs(value) <= 1.0E-12) {
//                    System.out.println("*");
//                } else {
//                    System.out.println();
//                }
//                
//            }
//            
//            System.out.println("--------------------------------------------");
//        }

    }

    public static void main(String[] args) {

        //arg[0] --> Ruta relativa al fichero donde se encuentran las funciones que se quieren testear
        //arg[1] --> Dimensionalidad de los problemas que se quieren testear:
        String functions_file;
        int dim;

        if (args.length < 2) {
            System.err.println("Número de parámetros insuficientes");
            System.err.println("java -jar TestFunctions.jar functions_file dim");
            System.err.println("\t functions_file : Ruta relativa al fichero " +
                    "de las funciones que se van a testear");
            System.err.println("\t dim : dimensionalidad del problema");
            System.exit(0);
        }

        functions_file = System.getProperty("user.dir") + File.separatorChar + args[0];
        dim = Integer.parseInt(args[1]);

        new TestFunctions(functions_file, dim);
    }
}
