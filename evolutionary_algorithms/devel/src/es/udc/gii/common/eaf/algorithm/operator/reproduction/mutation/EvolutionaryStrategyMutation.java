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
 * EvolutionaryStrategyMutation.java
 *
 * Created on 27 de septiembre de 2007, 12:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryStrategy;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a mutation operator to be used with the Evoluationary Strategies. This
 * operator reads the parameter &lamda; from the Evolutionar Strategy class {@see EvolutionaryStrategy}
 * and generates &lambda; children from one parent using a mutation operator.<p>
 *
* To configure this operator the tag at the xml config file would be like:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.EvolutionaryStrategyMutation</Class>
 *      <Operator>value</Operator>
 *      <!-- Parameters of the Operator used -->
 *      ...
 * </Operator>
 * }
 * </pre>
 *
 * Where <i>Class</i> and <i>Operator</i> are mandatory, the first one indicates that this class is going
 * to be used. The second one indicates the mutation operator that is going to be used to generate the
 * mutated individuals. After this second tag, is necessary to indicate the configuration parameters of the
 * mutation operator indicated in the tag <i>Operator</i>.<p>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EvolutionaryStrategyMutation extends MutationOperator {
    
    private MutationOperator operator;    
    
    /** Creates a new instance of EvolutionaryStrategyMutation */
    public EvolutionaryStrategyMutation() {
    }

    @Override
    public void configure(Configuration conf) {
        try {            
            this.operator =
                    (MutationOperator) Class.forName(conf.getString("Operator")).newInstance();
            this.operator.configure(conf);
            //this.lambda = conf.getInt("Lambda");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm,
            Individual individual) {
        
        List<Individual> mutated_individuals = new ArrayList<Individual>();
        int lambda = ((EvolutionaryStrategy)algorithm).getLambda();
        
        //Genero tantos hijos como lambda:
        for (int i = 0; i<lambda; i++) {
            
//            newIndividual = this.operator.mutation(
//                    algorithm,individual);
            mutated_individuals.addAll(
                    this.operator.mutation(algorithm, individual));
            
//            algorithm.evaluate(newIndividual);
//            mutatedIndividuals.add(newIndividual);
            
        }
        
        /** 
         * 06-06-08: No se añade el padre se devuelve el mejor de los hijos que se 
         * añadira a la población intermedia para evaluar:
         */
        //Añado el padre a la lista de individuos para comparar con el:
        //mutatedIndividuals.add(individual);
        
//        return bestSpecification.get(mutatedIndividuals,1,this.comparator).get(0);
        return mutated_individuals;
        
    }

    public MutationOperator getOperator() {
        return operator;
    }

    
    
}
