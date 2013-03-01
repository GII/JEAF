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
 * DeterministicTournamentSelection.java
 *
 * Created on December 1, 2007, 5:00 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.operator.selection;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.exception.OperatorException;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements the deterministic tournament selection. The individuals
 * of the population are arranged in groups and each individual in that group
 * competes for reproduction. Only one individual wins in each group.<p>
 *
 * It is ensured that no individual competes with itself and it is ensured that
 * all individuals get involved in at least one tournament (each individual is
 * in at least one of the created groups).
 *
 * To configure this operator the xml code is:<p>
 *
 * <pre>
 * {@code
 * <Operator><p>
 *      <Class>es.udc.gii.common.eaf.algorithm.operator.selection.DeterministicTournamentSelection</Class>
 *      <PoolSize>value</PoolSize>
 * </Operator>
 * }
 * </pre>
 *
 * Where the tag <i>Class</i> is mandatory and the tag <i>PoolSize</i> is the number of individuals
 * that are choosen to compete. If this tag does not appear in the configuration, the parameter <i>poolSize</i>
 * is set to its default value.<p>
 *
 * Default values:
 * <ul>
 * <li> PoolSize default value is 2.</li>
 * </ul>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class DeterministicTournamentSelection extends SelectionOperator {

    /**
     * The pool size is the number of members that compete in each tournament.
     */
    private int poolSize = 2;

    /** Creates a new instance of DeterministicTournamentSelection */
    public DeterministicTournamentSelection() {
    }

    /** Creates a new instance of DeterministicTournamentSelection */
    public DeterministicTournamentSelection(int poolSize) {
        this.poolSize = poolSize;
    }
    
    @Override
    public List<Individual> operate(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) throws OperatorException {

        /* Check exceptions. */
        if (individuals == null) {
            throw new OperatorException("DeterministicTournamentSelection - " +
                    "Empty individuals");
        }

        /* Contruct the selected list of individuals by tournament. */
        List<Individual> selectedIndividuals = 
                new ArrayList<Individual>(individuals.size());

        /* For each individual */
        for (int i = 0; i < individuals.size(); i++) {

            /* Get it. */
            Individual currentInd = individuals.get(i);

            /* Construct a pool with the other individuals. We will choose
             * individuals from that pool so we ensure that the current individual
             * is chosen only once and therefore it does not compete with itself. */
            List<Individual> pool = new ArrayList<Individual>(individuals.size());
            pool.addAll(individuals);
            pool.remove(currentInd);

            /* Construct the tournament group. */
            List<Individual> tournament = new ArrayList<Individual>(poolSize);
            tournament.add(currentInd);

            /* Add peers randomly to the tournament until the pool size
             * (group size) is reached. */
            for (int j = 0; j < poolSize - 1; j++) {
                int ind = EAFRandom.nextInt(pool.size());
                Individual peer = pool.get(ind);
                tournament.add(peer);
                pool.remove(peer); /* Don't consider the same individual again. */
            }

            /* Perform tournament. */
            BestIndividualSpecification spec =
                new BestIndividualSpecification();
            Individual selected = spec.get(tournament, 1, algorithm.getComparator()).get(0);
            selectedIndividuals.add((Individual) selected.clone());
        }

        return selectedIndividuals;
    }

    @Override
    public void configure(Configuration conf) {        
        if(conf.containsKey("PoolSize")) {
            this.poolSize = conf.getInt("PoolSize");
        } else {
            this.poolSize = 2;
            (new ConfWarning("DeterministicTournamentSelection.PoolSize",
                    this.poolSize)).warn();
        }
    }

    @Override
    protected Individual select(EvolutionaryAlgorithm algorithm,
            List<Individual> individuals) {
        throw new UnsupportedOperationException("Not supported");
    }
}
