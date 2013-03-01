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
 * BestIndividualLogTool.java
 *
 * Created on April 7, 2008, 2:53 PM
 *
 */
package es.udc.gii.common.eaf.log.parallel;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import java.util.List;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 * A parallel log tool for logging the best individuals of a population.<p/>
 *
 * Configuration:
 *
 * <pre>
 * {@code <Number>...</Number>}
 * </pre>
 *
 * The {@code Number} parameter states how many individuals are logged. So if
 * it were 3, the 3 best individuals would be logged.<p/>
 *
 * The output reads as follows:
 *
 * <pre>
 * [ GENERATION ... ]
 * Best individual(s) (...)
 * ...
 * </pre>
 *
 * The "{@code ...}" are replaced by the number of generations so far, the
 * number of individuals logged and those best individuals repectively.<p/>
 *
 * In an island model this log tool logs each population of each island in an
 * independent file. For a distributed evaluation model, only a file is needed.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class BestIndividualLogTool extends ParallelLogTool {

    private int number = 0;

    /** Creates a new instance of BestIndividualLogTool */
    public BestIndividualLogTool() {
    }

    /** Creates a new instance of BestIndividualLogTool
     * @param number The number of individuals to show.
     */
    public BestIndividualLogTool(int number) {
        setNumber(number);
    }

    @Override
    public String getLogID() {
        return "BestIndividualLogTool";
    }

    @Override
    public void update(Observable o, Object arg) {

        ParallelEvolutionaryAlgorithm pea = (ParallelEvolutionaryAlgorithm) o;

        if (pea.getCurrentObservable() instanceof EvolutionaryAlgorithm) {
            super.update(o, arg);
            EvolutionaryAlgorithm ea = (EvolutionaryAlgorithm) pea.getCurrentObservable();

            if (ea.getState() == EvolutionaryAlgorithm.REPLACE_STATE) {
                String head = "[ GENERATION "
                        + String.valueOf(pea.getGenerations()) + " ]\n"
                        + "Best individual(s) (" + getNumber() + ")\n";

                Population pop = ea.getPopulation();

                BestIndividualSpecification bestSpec =
                        new BestIndividualSpecification();

                List<Individual> best = IndividualsProductTrader.get(bestSpec,
                        pop.getIndividuals(), getNumber(), pea.getComparator());

                super.getLog().println(head + best + "\n");
            }
        }

    }

    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);
            setNumber(conf.getInt("Number"));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
