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
 * FloatBLXCrossOver.java
 *
 * Created on 22 de mayo de 2007, 20:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.real_code;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.CrossOverOperator;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * An offspring is generated: H = (h<sub>1</sub>...h<sub>i</sub>..h<sub>n</sub>), 
 * where h<sub>i</sub> is a randomly (uniformly) chosen number of the interval 
 * [c<sub>min</sub>-I*&alpha;,c<sub>max</sub>+I*&alpah;], cmax = max(c<sup>1</sup><sub>i</sub> 
 * ,c<sup>2</sup><sub>i</sub>), c<sub>min</sub> = min(c<sup>1</sup><sub>i</sub>,
 * c<sup>2</sup><sub>i</sub>), I = c<sub>max</sub> - c<sub>min</sub>. 
 * The BLX-0.0 crossover is equal to the �at crossover.<p>
 * 
 * The xml code to config this operator is:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.crossover.real_code.BLXCrossOver</Class>
 *      <Alfa>value</Alfa>
 *      <Probability>value</Probability>
 * </Operator>
 * }
 * </pre>
 *
 * Where the tag Alfa represents the value of the parameter with the same name and probability is double
 * value in the range [0, 100] which represents the probability of crossover.
 * If these two tags do not appear in the configuration, the parameters are set to the default values. <p>
 *
 * Default values: <p>
 * <ul>
 * <li>Probability is set to 60.0</li>
 * <li>Alfa is set to 0.5</li>
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class BLXCrossOver extends CrossOverOperator {
    
    private double alfa = 0.5;
    
    /** Creates a new instance of BLXCrossOver */
    public BLXCrossOver() {
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        this.alfa = conf.getDouble("Alfa");
    }
    
    
    //An offSpring is generated: H = (h1,...,hi,...,hn); where hi is a randomly
    //(uniformly) chosen number of the interval [a = cmin- I*alfa,b = cmax + I*alfa],
    // cmax = max(ci1,ci2), cmin = min(ci1,ci2), I = cmax - cmin. The BLX-0.0
    //crossover is equal to the flat crossover:
    @Override
    protected List<Individual> crossOver(EvolutionaryAlgorithm ea, List<Individual> individuals) {
        
        double[] genes1, genes2;
        double gene1, gene2, newGene1, newGene2;
        double cmin, cmax, I, a, b, range;
        
        genes1 = individuals.get(0).getChromosomeAt(0);
        genes2 = individuals.get(1).getChromosomeAt(0);
        
        for (int i = 0; i<genes1.length; i++) {
            
            gene1 = genes1[i];
            gene2 = genes2[i];
            
            cmin = Math.min(gene1,gene2);
            cmax = Math.max(gene1,gene2);           
            I = cmax-cmin;
            
            a = cmin - I*this.alfa;
            b = cmax + I*this.alfa;
            range = b-a;           
            
            newGene1 = range*EAFRandom.nextDouble() + a;
            newGene1 = (newGene1 > 1 ? 1 : newGene1);
            newGene1 = (newGene1 < -1 ? -1 : newGene1);
            newGene2 = range*EAFRandom.nextDouble() + a;
            newGene2 = (newGene2 > 1 ? 1 : newGene2);
            newGene2 = (newGene2 < -1 ? -1 : newGene2);

            genes1[i] =  checkBounds(ea, newGene1);
            genes2[i] = checkBounds(ea, newGene2);
            
        }
        
        individuals.get(0).setChromosomeAt(0, genes1);
        individuals.get(1).setChromosomeAt(0, genes2);
        
        return individuals;
        
    }

    
}
