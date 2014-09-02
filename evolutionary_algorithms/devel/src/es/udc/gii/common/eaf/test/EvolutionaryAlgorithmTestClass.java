/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.test;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.io.File;

/**
 *
 * @author pilar
 */
public class EvolutionaryAlgorithmTestClass {

    public static void main(String[] args) {

        String eaf_config;
        EAFFacade eaf_facade = new EAFFacade();
        int num_test;



        eaf_config = System.getProperty("user.dir") + File.separatorChar + args[0];
        num_test = (args.length >= 2 ? Integer.parseInt(args[1]) : 1);


        EAFRandom.init();
        EvolutionaryAlgorithm algorithm = eaf_facade.createAlgorithm(eaf_config);
        StopTest stop_test = eaf_facade.createStopTest(eaf_config);
        for (int i = 0; i < num_test; i++) {
            
            eaf_facade.resolve(stop_test, algorithm);
            

        }


    }
}
