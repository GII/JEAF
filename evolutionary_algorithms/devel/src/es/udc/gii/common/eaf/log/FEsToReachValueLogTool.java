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
package es.udc.gii.common.eaf.log;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 * This log tool implements a log tool that record the number of function evaluations needed until
 * reach a fitness value specify in the parameter <i>value</i>.<p>
 *
 * Apart from the superclass configuration parameters, it is necessary to configure the value of the
 * parameter <i>value</i>. So, the xml code necessary to use this log tool is the following:<p>
 *
 * <pre>
 * {@code
 * <LogTool>
 *      <Class>es.udc.gii.common.eaf.log.FEsToReachValueLogTool</Class>
 *      <Folder>value</Folder>
 *      <Name>value</Name>
 *      <Value>value</Value>
 * </LogTool>
 * }
 * </pre>
 *
 * Where the tag <i>Class</i> is mandatory, and indicates the specific class to be used. The tags
 * <i>Folder</i>, <i>Name</i> and <i>Value</i> are optional, and indicates the folder where the log will be recorded,
 * the name of the file and the desired value, respectively. If these three tags do not appear, their defautl value is used.<p>
 *
 * Default values:
 * <ul>
 * <li>Folder default value is "working_directory/OF".
 * <li>Name default value is ALG_POP_TS.txt (monoprocessor environment) or ALG_POP_TS_ND.txt (distributed environment).
 * <li>Value default value is 1.0e-6
 * </ul>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class FEsToReachValueLogTool extends LogTool {

    private double value = 1.0e-6;
    private Individual to_compare;
    private boolean print = true;

    /** Creates a new instance of BestMeanLogTool */
    public FEsToReachValueLogTool() {
    }

    @Override
    public void configure(Configuration conf) {

        super.configure(conf);
        if (conf.containsKey("Value")) {
            this.value = conf.getDouble("Value");
            this.to_compare = new Individual();
            this.to_compare.setFitness(value);
        } else {
            ConfWarning w = new ConfWarning("Value", this.value);
            w.warn();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm) o;
        BestIndividualSpecification bestSpec =
                new BestIndividualSpecification();
        Individual best;

        super.update(o, arg);

        if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE && arg == null) {
            best = (Individual) IndividualsProductTrader.get(bestSpec,
                    algorithm.getPopulation().getIndividuals(), 1, algorithm.getComparator()).get(0);

            if (print && algorithm.getComparator().compare(best, this.to_compare) <= -1) {

                int index_of_best = algorithm.getPopulation().getIndividuals().indexOf(best);

                super.getLog().println(algorithm.getFEs()
                        - algorithm.getPopulation().getSize() + index_of_best);
                print = false;
            }
        }

        if (algorithm.getState() == EvolutionaryAlgorithm.FINAL_STATE) {
            print = true;
        }
    }

    @Override
    public String getLogID() {
        return "festovalue";
    }
}
