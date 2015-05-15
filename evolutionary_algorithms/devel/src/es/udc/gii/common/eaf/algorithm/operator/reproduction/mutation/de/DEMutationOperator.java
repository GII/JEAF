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
 * DEMutationOperator.java
 *
 * Created on 29 de agosto de 2007, 18:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.MutationOperator;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.crossover.BinCrossOverScheme;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.crossover.CrossOverScheme;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.mutationStrategy.DEMutationStrategy;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.mutationStrategy.RandomDEMutationStrategy;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * 
 * For each target vector x<sub>i,G</sub>, i = 1,2,...,NP; a trial vector is generated. To generate this
 * trial vector the algorithm follow two phases. First of all, a mutated individual is generated by applying
 * a mutation strategy to each target vector. In order to increase the diversity of the population, a
 * crossOver operator is appliad to this mutated vector generating the trial vector. <p>
 *
 * To decide if the trial vector should become a member of the population in the next generation, it is
 * compared with the target vector. This behavior is implemented in a replace operator. As the DE Algorithm
 * is an evolutionary strategy, the EvolutionaryStrategyReplaceOperator or a subclass of it should be use. <p>
 *
 * The mutation stategy and the crossOver scheme are implemented in DEMutationStrategy and CrossOverScheme classes
 * respectively.
 * 
 * To config this operator the xml code is:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 * <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.DEMutationOperator</Class>
 * <MutationStrategy>
 *      <Class>value</Class>
 *      <F>value<F;>
 *      <diffVector>value<diffVector;>
 *      ...
 * </MutationStr>
 * <CrossOverScheme>
 *      <Class>value</Class>
 *      <CR>value<CR>
 *      ...
 * </CrossOverScheme>
 * </Operator>
 * }
 * </pre>
 *
 * where the tags MutationStrategy and CrossOverScheme represent the mutation strategy and the crossover scheme
 * repectively. In these two tags, the tag <i>Class</i> is mandatory and it should be a subclass of the
 * DEMutationStrategy class or the CrossOverScheme class. Each of them should be configured. If this parameters
 * do not appear in the configuration file, they are set to their default values.
 *
 * Default values:
 * <ul>
 * <li>Mutation stategy is set to random strategy with F set to 0.5 and diffVector to 1.</li>
 * <li>Crossover scheme is set to binary crossover with CR equals to 0.1</li>
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class DEMutationOperator extends MutationOperator {

    /**
     * In order to increase the divertity a CrossOver operator is introduced. There are
     * several CrossOver schemes. The default one is the Binaray Crossover scheme.
     */
    private CrossOverScheme crossOverScheme = new BinCrossOverScheme();

    /**
     * The strategy to choose the base vector.
     */
    private DEMutationStrategy mutationStrategy = new RandomDEMutationStrategy();

    /** Creates a new instance of DEMutationOperator */
    public DEMutationOperator() {
    }

    @Override
    public void configure(Configuration conf) {
        try {

            if (conf.containsKey("MutationStrategy.Class")) {
                this.mutationStrategy = (DEMutationStrategy) Class.forName(
                        conf.getString("MutationStrategy.Class")).newInstance();
                this.mutationStrategy.configure(conf.subset("MutationStrategy"));
            } else {
                ConfWarning w = new ConfWarning("MutationStrategy",
                        this.getClass().getSimpleName());
                w.warn();
            }
            if (conf.containsKey("CrossOverScheme.Class")) {
                this.crossOverScheme = (CrossOverScheme) Class.forName(
                        conf.getString("CrossOverScheme.Class")).newInstance();
                this.crossOverScheme.configure(conf.subset("CrossOverScheme"));
            } else {
                ConfWarning w = new ConfWarning("CrossOverScheme",
                        this.crossOverScheme.getClass().getSimpleName());
                w.warn();
            }
            
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new ConfigurationException(this.getClass(), ex);
        }

    }
    //Se aplica el operador de mutación y de cruce en la misma operación, el
    //individuo que se pasa como parámetro es el individuo "target"
    @Override
    protected List<Individual> mutation(EvolutionaryAlgorithm algorithm, Individual target) {

        List<Individual> individuals = new ArrayList<>();
        
        Individual base = (this.mutationStrategy.getMutatedIndividual(algorithm, target));
        //Se comprueban los límites de los genes:
        double[] chromosome = new double[base.getChromosomeAt(0).length];
        for (int i = 0; i < base.getChromosomeAt(0).length; i++) {
            chromosome[i] = checkBounds(algorithm, base.getChromosomeAt(0)[i]);
        }
        base.setChromosomeAt(0, chromosome);
        
        //El individuo "base" es el nuevo individuo mutado, hay que cruzarlo para
        //crear el individuo "trial" que se comparará con el "target" para decidir
        //quien pasa a la siguiente generación:

        Individual mutatedIndividual = 
                this.crossOverScheme.crossOver(algorithm, target, base);
        
        
        individuals.add(mutatedIndividual);
        return individuals;
        
    }

    public CrossOverScheme getCrossOverScheme() {
        return crossOverScheme;
    }

    public DEMutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    
}
