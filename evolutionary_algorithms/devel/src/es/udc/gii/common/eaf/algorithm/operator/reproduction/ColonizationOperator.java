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
 * ColonizationOperator.java
 *
 * Created on 18 de diciembre de 2006, 18:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.operator.reproduction;

    import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.MaIndividual;
import es.udc.gii.common.eaf.plugin.parameter.Parameter;
import es.udc.gii.common.eaf.plugin.parameter.Constant;
import es.udc.gii.common.eaf.plugin.parameter.LinearAnnealing;
import es.udc.gii.common.eaf.plugin.individual.IndividualChooser;
import es.udc.gii.common.eaf.plugin.individual.RandomIndividual;
import es.udc.gii.common.eaf.exception.OperatorException;
import es.udc.gii.common.eaf.plugin.parameter.RandomValue;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * It allows filling vacant sites that are freed by extinct individuals (that is,
 * those such that <i>S<sub>i</sub> = 0</i>. This operator is applied to each 
 * extinct individual in two ways.<p>
 * 
 * With a probability <i>&tau;</i>, a totally new solution <i><b>p</b><sub>n</sub>
 * &#8712; &#937;</i> will be generated. Otherwise exploitation on surviving
 * solutions takes place throughh colonization. For a given solution <i><b>p</b>
 * <sub>i</sub></i>, we choose one of the surviving solutions, say <i><b>p</b>
 * <sub>b</sub></i>. Now the extinct solution will be "attracted" toward <i>
 * <b>p</b><sub>b</sub></i>.
 * 
 * The solution implemented in this case is:<p>
 * 
 * <i><b>p</b><sub>i</sub>(t+1) = <b>p</b><sub>b</sub>(t) + &rho;*&lambda;*(
 * <b>p</b><sub>b</sub>(t) - <b>p</b><sub>i</sub>(t)), if &epsilon; > &tau;</i>
 * <p>
 * <i><b>p</b><sub>i</sub>(t+1) = <b>p</b><sub>n</sub>, if &epsilon; &le; &tau;</i>
 * <p>
 * 
 * where &epsilon; &#8712; [0,1] is a random number, &lambda; &#8712; [-1,1] (both
 * with uniform distribution) and &rho; and &tau; are given constants of our 
 * operator. So we can see that &rho; describes a maximum radius around surviving
 * solutions and &tau; acts as a temperature. <p>
 *
 * To config this operator the xml code is:<p>
 *
 * <pre>
 * {@code
 * <Operator>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.reproduction.ColonizationOperator</Class>
 *      <Rho>
 *          <Class>value</Class>
 *          ...
 *      </Rho>
 *      <Lambda>
 *          <Class>value</Class>
 *          ...
 *      </Lambda>
 *      <Thau>
 *          <Class>value</Class>
 *          ...
 *      </Thau>
 *      <Chooser>
 *          <Class>value</Class>
 *          ...
 *      </Chooser>
 * </Operator>
 * }
 * </pre>
 *
 * where the tags Rho, Lambda and Thau represent the parameters of the operator and Chooser is the
 * strategy to choose the survivor chromosome. In all these tags, the tag <i>Class</i> is mandatory
 * and it should be a subclass of the Parameter Class in the first three ones and a subclass of IndividualChooser
 * in the last one. Each of them should be configured. If this parameters
 * do not appear in the configuration file, they are set to their default values.<p>
 *
 * Default values:
 * <ul>
 * <li>Rho is set to a Constant Parameter.
 * <li>Thau is set to a linear annealing Parameter.
 * <li>Lambda is set to a random value in the range of [-1-0,1.0].
 * <li>Chooser is set to RandomIndividual chooser.
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class ColonizationOperator extends ReproductionOperator
{
    private Parameter rho;
    private Parameter tau;
    private IndividualChooser chooser;
    private Parameter lambdaGenerator;
    
    public ColonizationOperator()
    {
        rho = new Constant();
        tau = new LinearAnnealing();
        chooser = new RandomIndividual();
        lambdaGenerator = new RandomValue(-1, 1);
    }

    @Override
    public void configure(Configuration conf)
    {
        ConfWarning w;
        try
        {
            if (conf.containsKey("Rho.Class"))
            {
                this.rho = (Parameter)Class.forName(conf.getString("Rho.Class")).newInstance();
                this.rho.configure(conf.subset("Rho"));
            } else {
                w = new ConfWarning("Rho",
                        this.rho.toString());
                w.warn();
            }
            if (conf.containsKey("Lambda.Class"))
            {
                this.lambdaGenerator = (Parameter)Class.forName(conf.getString("Lambda.Class")).newInstance();
                this.lambdaGenerator.configure(conf.subset("Lambda"));
            } else {
                w = new ConfWarning("Lambda",
                        this.lambdaGenerator.toString());
                w.warn();
            }
            if (conf.containsKey("Tau.Class"))
            {
                this.tau = (Parameter)Class.forName(conf.getString("Tau.Class")).newInstance();
                this.tau.configure(conf.subset("Tau"));
            } else {
                w = new ConfWarning("Tau",
                        this.tau.toString());
                w.warn();
            }

            if (conf.containsKey("Chooser.Class"))
            {
                this.chooser = (IndividualChooser)Class.forName(conf.getString("Chooser.Class")).newInstance();
                this.chooser.configure(conf.subset("Chooser"));
            } else {
                w = new ConfWarning("Chooser",
                        this.chooser.toString());
                w.warn();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) throws OperatorException
    {
        List<Individual> survivors = new ArrayList<Individual>(individuals.size() / 2);
        List<Individual> extincts = new ArrayList<Individual>(individuals.size() / 2);
        double[] survivorChrom, extinctChrom;
        double tauValue = this.tau.get(algorithm);
        double rhoValue = this.rho.get(algorithm);
        double lambda;
        
        for (Individual individual: individuals)
        {
            if (((MaIndividual)individual).isSurvivor())
            {
                survivors.add(individual);
            }
            else
            {
                extincts.add(individual);
            }
        }
        
        for (Individual extinct: extincts)
        {
            if (EAFRandom.nextDouble() > tauValue)
            {
                extinctChrom = extinct.getChromosomeAt(0);
                survivorChrom = this.chooser.get(algorithm, survivors, extinct).getChromosomeAt(0);
                for (int j = 0; j < extinctChrom.length; j++)
                {
                    lambda = lambdaGenerator.get(algorithm);
                    extinctChrom[j] = checkBounds(algorithm, 
                            survivorChrom[j] + rhoValue * lambda * (extinctChrom[j] - survivorChrom[j]));
                }

                extinct.setChromosomeAt(0, extinctChrom);
            }
            else
            {
                extinct.generate();
            }
        }
        return individuals;
    }

    public IndividualChooser getChooser() {
        return chooser;
    }

    public void setChooser(IndividualChooser chooser) {
        this.chooser = chooser;
    }

    public Parameter getRho() {
        return rho;
    }

    public void setRho(Parameter rho) {
        this.rho = rho;
    }

    public Parameter getTau() {
        return tau;
    }

    public void setTau(Parameter tau) {
        this.tau = tau;
    }

    public Parameter getLambdaGenerator() {
        return lambdaGenerator;
    }

    public void setLambdaGenerator(Parameter lambdaGenerator) {
        this.lambdaGenerator = lambdaGenerator;
    }
}
