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
 * Topology.java
 *
 * Created on February 27, 2008, 4:31 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel.topology;

import es.udc.gii.common.eaf.config.Configurable;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.util.ConfWarning;
import mpi.Intracomm;
import mpi.MPI;
import org.apache.commons.configuration.Configuration;

/**
 * This class encapsulates the topology of the processing nodes in a parallel
 * environment and is responsible for the "low-level" communication between the
 * nodes. It represents an interconnection pattern between nodes.<p>
 *
 * Each process has an integer associated with it, beeing that number the rank
 * of this process within the topology.<p>
 *
 * A topology makes it easy to communicate with other nodes by giving methods to
 * send and receive data.<p>
 *
 * Before using a topology the method {@link Topology#initialize} should be called. This method
 * might perform communications with the other processes within the parallel environment
 * depending on the parameters of the topology and will configure the topology
 * by itself. If the user doesn't call this method, a call to any method who
 * uses the state of the class will call it.<p>
 *
 * Note: 'nodes' and 'processes' are used as synonyms.
 * Note for implementors: Subclasses must populate the protected atributes. 
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class Topology implements Configurable {

    /** The number of races in the parallel environment. */
    private int races = 0;
    /** For this implementation MPI is used. A communicator is used for each
     * topology separately so that messages between to nodes which are in different
     * topologies don't interfere with each other. */
    protected Intracomm communicator = null;
    /** The rank of this process within this topology. */
    protected int topologyRank = MPI.PROC_NULL;
    /** The ranks of the processes who receive from the process of this object. */
    protected int[] receivers = null;
    /** The ranks of the processes who send to the process of this object. */
    protected int[] senders = null;
    private boolean initialized = false;

    /** Creates a new instance of Topology. */
    public Topology() {
    }

    /** Creates a new instance of Topology. */
    public Topology(int races) {
        this.races = races;
    }

    /** Returns {@code true} if the process is connected to this topology and
     * can hence make use of it. Returns {@code false} otherwise.*/
    public boolean isConnected() {
        if (!isInitialized()) {
            initialize();
        }
        return topologyRank != MPI.PROC_NULL;
    }

    /** Returns {@code true} if this topology is initialized and can hence be used.
     * Returns {@code false} otherwise.*/
    public boolean isInitialized() {
        return initialized;
    }

    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /** A subclass might need additional initialization. */
    protected abstract void doInitialize();

    /** Initializes this topology.*/
    public void initialize() {
        if (!isInitialized()) {
            if (!MPI.Initialized()) {
                throw new RuntimeException("Topology (initialize): " +
                        "Parallel environment not initialized!");
            }

            int availableNodes = MPI.COMM_WORLD.Size();

            if (this.races > availableNodes) {
                throw new ConfigurationException("Topology (initialize): " +
                        "Not enough nodes (requested " + this.races + " races, only " +
                        availableNodes + " available).");
            }

            doInitialize();
            setInitialized(true);
        }
    }

    /** Synchronizes all processes within this topology. All the processes within
     * the topology have to call this method in order to work. */
    public void synchronize() {
        if (!isInitialized()) {
            initialize();
        }
        communicator.Barrier();
    }

    /** A subclass might need additional configuration. */
    protected abstract void doConfigure();

    /** A subclass might need additional configuration. */
    protected abstract void doConfigure(Configuration conf);

    /** Configures the topology. */
    @Override
    public void configure(Configuration conf) {
        if (!isInitialized()) {

            if (conf.containsKey("Races")) {
                this.races = conf.getInt("Races");
            } else if (!MPI.Initialized()) {
                throw new ConfigurationException("Topology: " +
                        "Parallel environment not initialized!");
            } else {
                this.races = MPI.COMM_WORLD.Size();
                ConfWarning w = new ConfWarning(this.getClass().getSimpleName() +
                        ".Races", this.races);
                w.warn();
            }

            doConfigure(conf);

            initialize();
        }
    }

    /* ---- Getters and setters follow ---- */
    public Intracomm getCommunicator() {
        if (!isInitialized()) {
            initialize();
        }
        return communicator;
    }

    public int getTopologyRank() {
        if (!isInitialized()) {
            initialize();
        }
        return topologyRank;
    }

    public int getRaces() {
        return races;
    }

    public int getSize() {
        if (!isInitialized()) {
            initialize();
        }
        return getCommunicator().Size();
    }

    public void setRaces(int races) {
        this.races = races;
    }

    public int[] getReceivers() {
        if (!isInitialized()) {
            initialize();
        }
        return receivers;
    }

    public int[] getSenders() {
        if (!isInitialized()) {
            initialize();
        }
        return senders;
    }
}
