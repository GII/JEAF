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
 * FullConnectedMigrationTopology.java
 *
 * Created on March 12, 2008, 8:06 PM
 *
 */

package es.udc.gii.common.eaf.algorithm.parallel.topology.migration;

import es.udc.gii.common.eaf.util.ConfWarning;
import mpi.Intracomm;
import mpi.MPI;
import org.apache.commons.configuration.Configuration;

/**
 * A full connected migration topology is a migration topology where each node
 * is connected to each other node.<p/>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class FullConnectedMigrationTopology extends MigrationTopology {
    
    /**
     * Creates a new instance of FullConnectedMigrationTopology.
     */
    public FullConnectedMigrationTopology() {
        super(MPI.COMM_WORLD.Size());
        (new ConfWarning("FullConnectedMigrationTopology.Races", MPI.COMM_WORLD.Size())).warn();
    }
    
    /**
     * Creates a new instance of FullConnectedMigrationTopology.
     */
    public FullConnectedMigrationTopology(int races) {
        super(races);
    }
    
    /** Custom initialization. */
    @Override
    protected void doInitialize() {
        
        /* Split the global communicator in two. */
        int color = MPI.COMM_WORLD.Rank() < getRaces() ? 0 : 1;
        communicator = (Intracomm) MPI.COMM_WORLD.Split(color, MPI.COMM_WORLD.Rank());
        
        /* The first communicator will be used for the migration topology. */
        if(color == 0) {
            
            /* Store my rank within the topology. */
            this.topologyRank = communicator.Rank();
            
            /* Store my receivers. */
            this.receivers = new int[this.communicator.Size()-1];
            
            int j = 0;
            for(int i = 0; i < this.communicator.Size(); i++) {
                if(i != this.topologyRank) {
                    this.receivers[j] = i;
                    j++;
                }
            }
            
            /* Store my senders. */
            this.senders = this.receivers;            
            
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
        /* No further configuration needed. */
    }

    @Override
    protected void doConfigure(Configuration conf) {
        /* No further configuration needed. */
    }
    
}
