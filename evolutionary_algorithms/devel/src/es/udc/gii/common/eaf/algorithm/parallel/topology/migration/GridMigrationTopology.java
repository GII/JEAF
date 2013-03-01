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
 * GridMigrationTopology.java
 *
 * Created on March 12, 2008, 8:23 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel.topology.migration;

import es.udc.gii.common.eaf.algorithm.parallel.topology.Topology;
import es.udc.gii.common.eaf.config.EAFConfiguration;
import java.util.ArrayList;
import java.util.List;
import mpi.Cartcomm;
import mpi.Intracomm;
import mpi.MPI;
import mpi.ShiftParms;
import org.apache.commons.configuration.Configuration;

/**
 * A grid migration topology is a migration topology where the nodes are arranged
 * in a (perhaps multidimensional) grid.<p/>
 *
 * Each dimension of the grid might be periodic, i.e. the first element of the
 * dimension is connected to the last. Thus this topology can be used to construct
 * a ring, which is a periodic one-dimensional grid, a torus, hypercubes, etc.<p/>
 *
 * Configuration example:
 *
 * <pre>
 * &lt;Topology&gt;
 *     &lt;Class&gt;...topology.GridMigrationTopology&lt;/Class&gt;
 *     &lt;Races&gt;18&lt;/Races&gt;
 *     &lt;Dimension count="3" periodic="true"/&gt;
 *     &lt;Dimension count="2" periodic="false"/&gt;
 *     &lt;Dimension count="3"/&gt;
 * &lt;/Topology&gt;
 * </pre><p>
 *
 * This example configures a GridMigrationTopology with 3 dimensions.
 * Dimension 1 has 3 nodes and is periodic, dimension has 2 nodes and is not
 * periodic and dimension 3 has 3 nodes and is not periodic. There are hence
 * 3 x 2 x 3 = 18 nodes needed.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class GridMigrationTopology extends MigrationTopology {

    private int[] nodesPerDimension = null;
    private boolean[] dimIsPeriodic = null;

    /**
     * Creates a new instance of GridMigrationTopology.
     */
    public GridMigrationTopology() {
    }

    /**
     * Creates a new instance of GridMigrationTopology. For an explanation of
     * {@code nodesPerDimension} and {@code dimIsPeriodic} see
     * {@link GridMigrationTopology#setNodesPerDimension} and
     * {@link GridMigrationTopology#setDimIsPeriodic}.
     */
    public GridMigrationTopology(int races, int[] nodesPerDimension,
            boolean[] dimIsPeriodic) {
        super(races);
        setNodesPerDimension(nodesPerDimension);
        setDimIsPeriodic(dimIsPeriodic);
    }

    /**
     * Calculates how many nodes (races) are needed for the current configuration. */
    private int neededNodes() {

        int total = 1;

        for (int i = 0; i < nodesPerDimension.length; i++) {
            total *= nodesPerDimension[i];
        }

        return total;
    }

    @Override
    protected void doInitialize() {
        /* These atributes must be set hence they are basic configuration. */
        if (nodesPerDimension == null || dimIsPeriodic == null) {
            throw new RuntimeException("GridMigrationTopology (doInitialize): "
                    + "Grid parameters not set (either dimensions or periodicities).");
        }

        /* Check that configuration is coherent. */
        if (nodesPerDimension.length != dimIsPeriodic.length) {
            throw new RuntimeException("GridMigrationTopology (doInitialize): "
                    + "the number of dimensions and the length of the periods array "
                    + "must be the same.");
        }

        /* Check that configuration is coherent. */
        if (neededNodes() != getRaces()) {
            throw new RuntimeException("GridMigrationTopology (doInitialize): "
                    + "The number of nodes needed for creating the grid must be equal "
                    + "to the number of races. With the current grid configuration "
                    + neededNodes() + " nodes are needed and " + getRaces()
                    + " races are requested.");
        }

        /* From here on we asume that the configuration is ok. */

        /* Split the global communicator in two. */
        int color = MPI.COMM_WORLD.Rank() < getRaces() ? 0 : 1;
        this.communicator = (Intracomm) MPI.COMM_WORLD.Split(color, MPI.COMM_WORLD.Rank());

        /* The first communicator will be used for the migration topology. */
        if (color == 0) {

            /* For freeing the communicator. */
            Intracomm comm_aux = this.communicator;

            /* Create the grid. */
            this.communicator = this.communicator.Create_cart(nodesPerDimension,
                    dimIsPeriodic, true);

            /* This should not happen since we asumed a correct conf., but it's
             * better to lounch our own exception rather than let the user guess
             * what is going on when he gets a NullPointerException ;-). */
            if (this.communicator == null) {
                throw new RuntimeException("GridMigrationTopology "
                        + "(doInitialize): Communicator not created! Is the "
                        + "configuration correct?");
            }

            /* Free the old communicator. */
            comm_aux.Free();

            /* Create list of receivers and senders */
            List<Integer> recvrs = new ArrayList<Integer>();
            List<Integer> sendrs = new ArrayList<Integer>();

            /* Calculate receivers and senders for each dimension. */
            for (int i = 0; i < nodesPerDimension.length; i++) {
                ShiftParms sp = ((Cartcomm) this.communicator).Shift(i, 1);

                /* Values less than 0 for rank_dest and rank_source mean that
                 * this node is not connected to the next (rank_dest) or to the
                 * previous (rank_source) node.
                 */

                if (sp.rank_dest >= 0) {
                    recvrs.add(sp.rank_dest);
                }

                if (sp.rank_source >= 0) {
                    sendrs.add(sp.rank_source);
                }
            }

            /* Copy */
            this.receivers = new int[recvrs.size()];
            for (int i = 0; i < this.receivers.length; i++) {
                this.receivers[i] = recvrs.get(i);
            }

            /* Copy */
            this.senders = new int[sendrs.size()];
            for (int i = 0; i < this.senders.length; i++) {
                this.senders[i] = sendrs.get(i);
            }

            /* Store my rank within the topology. */
            this.topologyRank = this.communicator.Rank();

        } else { /* The second communicator is useless and is discarded. The
             * result of this is that the process is not connected to this
             * topology. */
            this.communicator.Free();
            this.communicator = null;
            this.receivers = null;
            this.senders = null;
            this.topologyRank = MPI.PROC_NULL;
        }
    }

    @Override
    protected void doConfigure() {
        doConfigure(EAFConfiguration.getInstance());
    }
    
    @Override
    protected void doConfigure(Configuration conf) {
        List listDimensions = conf.getList("Dimension[@count]");

        this.nodesPerDimension = new int[listDimensions.size()];
        this.dimIsPeriodic = new boolean[listDimensions.size()];

        for (int i = 0; i < listDimensions.size(); i++) {
            nodesPerDimension[i] = conf.getInt("Dimension(" + i + ")[@count]");
            dimIsPeriodic[i] = conf.getBoolean("Dimension(" + i + ")[@periodic]", false);
        }
    }

    public int[] getNodesPerDimension() {
        return nodesPerDimension;
    }

    /**
     * Sets how many nodes will each dimension have. The number of dimensions is
     * thus the length of the array {@code nodesPerDimension}.<p>
     *
     * This parameter should be set before the method {@link Topology#initialize}
     * is called. After Topology#initialize is called, setting the nodes per dimension
     * will have no effect. */
    public void setNodesPerDimension(int[] nodesPerDimension) {
        this.nodesPerDimension = nodesPerDimension;
    }

    public boolean[] getDimIsPeriodic() {
        return dimIsPeriodic;
    }

    /**
     * Sets if a dimension of the grid is periodic (i.e. the last node an the
     * first are connected) for each dimension. Thus the length of {@code dimIsPeriodic}
     * must be equal to the number of dimensions of the grid.<p>
     *
     * This parameter should be set before the method {@link Topology#initialize}
     * is called. After Topology#initialize is called, setting this parameter will have
     * no effect. */
    public void setDimIsPeriodic(boolean[] dimIsPeriodic) {
        this.dimIsPeriodic = dimIsPeriodic;
    }
}
