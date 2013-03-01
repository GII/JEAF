/*
* Copyright (C) 2010 Grupo Integrado de Ingeniería
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/ 


/*
 * MOPCrossOver.java
 *
 * Created on 19 de diciembre de 2006, 11:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a SBX crossover (Simulated Binary Crossover). It is based on the search features
 * of the single-point crossover used in the binary-coded genetic algorithm. In this operator, the common
 * interval schemata of the parents are preserved in the offspring. This operator tends to generate
 * offspring near the parents. So, the extent of the children is proportional to the extent of the parents. <p>
 *
 * This is the xml code to configure this operator<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.SBXCrossOver</Class>
 *      <Probability>value</Probability>+
 *      <CrossOverIndex>value</CrossOverIndex>
 * </Operator>
 * }
 * </pre>
 *
 * Where the tag <i>Class<i> is mandatory and indicates the this class is goint to be used. The other
 * two tags are optional. The tag <i>Probability</i> represents the probability of two individuals of
 * being crossover and the tag <i>CrossOverIndex</i> represents the crossover index of the two individuals.
 * If some of these parameters do not appear in the configuration, their default value is used.
 *
 * Default values: <p>
 * <ul>
 * <li>Probability is set to 60.0</li>
 * <li>CrossOverIndex is set to 1</li>
 * </ul>
 * 
 * More information about this operator can be found in:<br>
 * K. Deb and R. B. Agrawal. Simulated binary crossover for continuous search space. <i>Complex Systems, 9</i>115-148, 1995.<p>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class SBXCrossOver extends CrossOverOperator {

    private static double EPS = 1.0E-14;
    //indice de cruce entre
    private int crossOverIndex = 1;
    //numero de cruces
    private int numberCrossOvers = 0;

    /** Creates a new instance of MOPCrossOver */
    public SBXCrossOver() {
    }

    public SBXCrossOver(int probability) {
        super(probability);
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        if (conf.containsKey("CrossOverIndex")) {
            this.crossOverIndex = conf.getInt("CrossOverIndex");
        } else {
            ConfWarning w = new ConfWarning("CrossOverIndex", crossOverIndex);
            w.warn();
        }
    }

    @Override
    protected List<Individual> crossOver(EvolutionaryAlgorithm ea, List<Individual> individuals) {

        int i, j, numVariables;
        List<Individual> crossOverIndividuals;
        double gen1, gen2, y1, y2, yl, yu, c1, c2, alpha, beta, betaq, rand, gensFirstIndividual, gensSecondInvidual;
        //Double gen1, gen2, y1, y2;
        List<Individual> children;
        double[] gens1, gens2;

        //List<Double> firstIndividualGens, secondIndividualGens;
        double[] firstIndividualGens, secondIndividualGens;


        //numero de variables del problema
        int numberVariables = individuals.get(0).getChromosomeAt(0).length;


        //individuos que se consideran en el cruce

        firstIndividualGens = individuals.get(0).getChromosomeAt(0);
        secondIndividualGens = individuals.get(1).getChromosomeAt(0);

        //firstIndividualGens = individuals.get(0).getChromosome().decode();
        //secondIndividualGens = individuals.get(1).getChromosome().decode();

        //if(Math.random()*100 <= super.getProbability()) {
        //aumentamos el numero de cruces que se han realizado
        increaseCrossOvers();

        for (j = 0; j < numberVariables; j++) {

            if (EAFRandom.nextDouble() <= 0.5) {

                //gen1 = firstIndividualGens.get(j);
                //gen2 = secondIndividualGens.get(j);

                gen1 = firstIndividualGens[j];
                gen2 = secondIndividualGens[j];

                if (Math.abs(gen1 - gen2) > EPS) {
                    if (gen1 < gen2) {
                        y1 = gen1;
                        y2 = gen2;
                    } else {
                        y1 = gen2;
                        y2 = gen1;
                    }

                    //el valor minimo y maximo de cada variable
                    //codificado en cada gen, es igual para ambos individuos

                    yl = -1.0;
                    yu = 1.0;

                    rand = EAFRandom.nextDouble();
                    beta = 1.0 + (2.0 * (y1 - yl) / (y2 - y1));
                    alpha = 2.0 - Math.pow(beta, -(crossOverIndex + 1.0));
                    if (rand <= (1.0 / alpha)) {
                        betaq = Math.pow((rand * alpha), (1.0 / (crossOverIndex + 1.0)));
                    } else {
                        betaq = Math.pow((1.0 / (2.0 - rand * alpha)), (1.0 / (crossOverIndex + 1.0)));
                    }

                    c1 = 0.5 * ((y1 + y2) - betaq * (y2 - y1));
                    beta = 1.0 + (2.0 * (yu - y2) / (y2 - y1));
                    alpha = 2.0 - Math.pow(beta, -(crossOverIndex + 1.0));
                    if (rand <= (1.0 / alpha)) {
                        betaq = Math.pow((rand * alpha), (1.0 / (crossOverIndex + 1.0)));
                    } else {
                        betaq = Math.pow((1.0 / (2.0 - rand * alpha)), (1.0 / (crossOverIndex + 1.0)));
                    }

                    c2 = 0.5 * ((y1 + y2) + betaq * (y2 - y1));
                    if (c1 < yl) {
                        c1 = yl;
                    }
                    if (c2 < yl) {
                        c1 = yl;
                    }
                    if (c1 > yu) {
                        c1 = yu;
                    }
                    if (c2 > yu) {
                        c2 = yu;
                    }

                    if (EAFRandom.nextDouble() <= 0.5) {
                        firstIndividualGens[j] = c2;
                        secondIndividualGens[j] = c1;
                    } else {
                        firstIndividualGens[j] = c1;
                        secondIndividualGens[j] = c2;
                    }

                    firstIndividualGens[j] = checkBounds(ea, firstIndividualGens[j]);
                    secondIndividualGens[j] = checkBounds(ea, secondIndividualGens[j]);
                }
            }


        }
        //}

        individuals.get(0).setChromosomeAt(0, firstIndividualGens);
        individuals.get(1).setChromosomeAt(0, secondIndividualGens);


        return individuals;

    }

    private void increaseCrossOvers() {
        this.numberCrossOvers++;
        // System.out.println("El nmero de cruces realizados es: " + this.numberCrossOvers);
    }

    @Override
    public String toString() {
        return "Multi-objective CrossOver Operator";
    }
}
