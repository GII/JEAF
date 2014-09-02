/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package run;

import analysis.EAFAnalysis;
import analysis.MultipleTestAnalysis;
import analysis.OneTestAnalysis;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.io.File;


/**
 *
 * @author pilar
 */
public class RunMultipleTestAnalysis {

    public void run(String[] args) {
        
        //arg[0] --> ruta relativa al fichero de configuraci√≥n del algoritmo
        //arg[1] --> numero de pruebas para cada funci√≥n objetivo
        //arg[2] --> ruta relativa al fichero xml con las funciones objetivo que se van a testear
        
        String eaf_config, functions_list;
        int num_test;
        EAFAnalysis multiple_analysis, analysis;
        EAFFacade eaf_facade = new EAFFacade();
        EvolutionaryAlgorithm algorithm;
        StopTest stop_test;
        
        if (args.length < 3) {
            
            System.err.println("N√∫mero de par√°metros insuficientes");
            System.err.println("java -jar RunMultipleTestAnalysis.jar eaf_config " +
                    "num_test functions_list");
            System.err.println("\t eaf_config : ruta relativa al fichero de " +
                    "configuraci√≥n del algoritmo evolutivo");
            System.err.println("\t num_test : n√∫mero de pruebas para cada " +
                    "funci√≥n objetivo");
            System.err.println("\t functions_list : ruta relativa al fichero xml " +
                    "con las funciones objetivo a testear");
            System.exit(0);
        }
        
        eaf_config = System.getProperty("user.dir") + File.separatorChar + args[0];
        num_test = Integer.parseInt(args[1]);
        functions_list = System.getProperty("user.dir") + File.separatorChar + args[2];
        
        analysis = new OneTestAnalysis(num_test);
        multiple_analysis = new MultipleTestAnalysis(functions_list, analysis);
        
        //Creamos el algoritmo evolutivo y el test de parada:
        EAFRandom.init();
        algorithm = eaf_facade.createAlgorithm(eaf_config);
        stop_test = eaf_facade.createStopTest(eaf_config);
        
        //Lanzamos la prueba:
        multiple_analysis.run(algorithm, stop_test);
        
    }
    
    public static void main(String[] args) {
        
        new RunMultipleTestAnalysis().run(args);
    }
    
}
