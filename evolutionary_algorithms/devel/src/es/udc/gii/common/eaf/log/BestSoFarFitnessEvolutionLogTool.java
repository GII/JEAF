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
import es.udc.gii.common.eaf.algorithm.mga.MGAAlgorithm;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 * This log tool logs the fitness of the best individual each n evaluations
 * of the objective function.<p/>
 *
 * Note: Due to different factors, as for instance population size, this log tool
 * does not ensure an output *exactly* each n evaluations. It only ensures that there
 * will be an output (a log entry) when this log tool is called the first time 
 * after at least n function evaluations have been performed.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class BestSoFarFitnessEvolutionLogTool extends LogTool {

    private int feStep;
    private int currentFes;

    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);
            this.currentFes = feStep;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Creates a new instance of BestSoFarFitnessEvolutionLogTool */
    public BestSoFarFitnessEvolutionLogTool() {
    }

    @Override
    public void update(Observable o, Object arg) {
        EvolutionaryAlgorithm alg = (EvolutionaryAlgorithm) o;

        super.update(o, arg);

        if ((alg.getState() == EvolutionaryAlgorithm.REPLACE_STATE) ||
                ((alg instanceof MGAAlgorithm) && alg.getState() == EvolutionaryAlgorithm.FINAL_STATE)) {
            if (alg.getFEs() >= this.currentFes) {

                BestIndividualSpecification bestSpec =
                        new BestIndividualSpecification();

                double bestFitness =
                        IndividualsProductTrader.get(bestSpec,
                        alg.getPopulation().getIndividuals(), 1,
                        alg.getComparator()).get(0).getFitness();

                super.getLog().println(alg.getFEs() + " " + bestFitness);

                this.currentFes += feStep;
            }
        }
    }

    @Override
    public String getLogID() {
        return "bestsofarfitnessevolution";
    }
}
