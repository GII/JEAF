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
 * MigrationLogTool.java
 *
 * Created on April 2, 2008, 10:41 AM
 *
 */
package es.udc.gii.common.eaf.log.parallel;

import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.migration.MigrationOperator;
import es.udc.gii.common.eaf.algorithm.parallel.topology.migration.MigrationTopology;
import es.udc.gii.common.eaf.algorithm.population.Population;
import es.udc.gii.common.eaf.exception.WrongAlgorithmException;
import java.util.Observable;

/**
 * Logs the migrations in a parallel evolutionary algorithm.<p/>
 *
 * The output reads as follows:
 *
 * <pre>
 * This node sends to:
 *   ... ranks of the nodes to which this island sends individuals ...
 * This node receives from:
 *   ... ranks of the nodes from which this island receives individuals ...
 *
 * [topology rank = ... rank of this island ...]
 *  GENERATION ... generations ...
 *
 * Current population: ... current island population ...
 * Sending MigrationObject: ... migration object with the individuals sent to
 * other islands (and the ranks of those islands) ...
 *
 * Received MigrationObject: ... received migration object with the individuals
 * received from other islands (and the ranks of those islands) ...
 *
 * Accepted individuals: ... accepted individuals from other islands ...
 *
 * Culled individuals: ... culled individuals from the current population ...
 * </pre>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 * @see MigrationOperator
 * @see MigrationTopology
 */
public class MigrationLogTool extends ParallelLogTool {

    private boolean firstLog = true;

    /** Creates a new instance of MigrationLogTool */
    public MigrationLogTool() {
    }

    @Override
    public String getLogID() {
        return "MigrationLogTool";
    }

    @Override
    public void update(Observable o, Object arg) {

        if (!(o instanceof ParallelEvolutionaryAlgorithm)) {
            throw new WrongAlgorithmException(
                    ParallelEvolutionaryAlgorithm.class, o.getClass());
        }

        ParallelEvolutionaryAlgorithm pea = (ParallelEvolutionaryAlgorithm) o;

        if (pea.getCurrentObservable() instanceof MigrationOperator) {
            super.update(o, arg);
            MigrationOperator mop = (MigrationOperator) pea.getCurrentObservable();

            if (firstLog) {
                firstLog = false;
                getLog().println("This node sends to:");
                int[] recvs = mop.getMigrationTopology().getReceivers();
                for (int i = 0; i < recvs.length; i++) {
                    getLog().print(recvs[i] + " ");
                }
                getLog().println();
                getLog().println("This node receives from:");
                int[] sends = mop.getMigrationTopology().getSenders();
                for (int i = 0; i < sends.length; i++) {
                    getLog().print(sends[i] + " ");
                }

                getLog().println("\n");
            }

            String head = "[topology rank = "
                    + mop.getMigrationTopology().getTopologyRank() + "]\n GENERATION "
                    + pea.getGenerations() + "\n";

            Population p = new Population();

            switch (mop.getState()) {
                case MigrationOperator.SENDING:
                    p.setIndividuals(mop.getCurrentPopulation());
                    getLog().println(head + "Current population: \n" + p);
                    getLog().println("Sending MigrationObject: "
                            + "\n" + mop.getCurrentMigrant() + "\n");
                    break;
                case MigrationOperator.RECEIVED:
                    for (int i = 0; i < mop.getCurrentMigrants().size(); i++) {
                        getLog().println(head + "Received MigrationObject: "
                                + "\n" + mop.getCurrentMigrants().get(i) + "\n");
                    }
                    break;
                case MigrationOperator.ACCEPTED:
                    p.setIndividuals(mop.getCurrentAccepted());
                    getLog().println(head + "Accepted individuals: "
                            + "\n" + p + "\n");
                    break;
                case MigrationOperator.CULLED:
                    p.setIndividuals(mop.getCurrentCulled());
                    getLog().println(head + "Culled individuals: "
                            + "\n" + p + "\n");
                    break;
            }
        }
    }
}
