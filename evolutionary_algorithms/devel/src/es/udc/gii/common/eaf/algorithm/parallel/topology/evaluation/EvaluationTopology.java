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
 * EvaluationTopology.java
 *
 * Created on March 18, 2008, 5:46 PM
 *
 */

package es.udc.gii.common.eaf.algorithm.parallel.topology.evaluation;

import es.udc.gii.common.eaf.algorithm.parallel.evaluation.DistributedEvaluation;
import es.udc.gii.common.eaf.algorithm.parallel.topology.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mpi.MPI;
import mpi.Request;
import mpi.Status;
import org.apache.commons.configuration.Configuration;

/**
 * A evaluation topology is used by a {@link DistributedEvaluation} and
 * hides the details of the underlying distributed evironment.<p>
 * 
 * This class provides methods for sending and receiving individuals 
 * (using {@link EvaluationObject}): see ({@link EvaluationTopology#send} and 
 * {@link EvaluationTopology#receive}).
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EvaluationTopology extends Topology implements Serializable {
    
    /* Stores the pending send requests */
    private Map<Request, EvaluationObject[]> pendingSendRequest = null;
    
    /* Message tag for sending and receiving. */
    protected static final int MSG_TAG = 0;
    
    /** Rank of the default master process. */
    protected static final int MASTER = 0;
    
    /** <code>true</code> if this process is the master in this topology. */
    protected boolean master = false;
    
    /** Special return value for {@link EvaluationTopology#getMaster}. */
    public static final int NONE = -999;
    
    /** The rank of the master of this topology. */
    protected int myMaster = MASTER;
    
    /**
     * Creates a new instance of EvaluationTopology.
     */
    public EvaluationTopology() {
    }
    
    /**
     * Creates a new instance of EvaluationTopology.
     */
    public EvaluationTopology(int races) {
        super(races);
    }
    
    /**
     * Performs the initialization.
     */
    @Override
    protected void doInitialize() {
        
        /* Total nodes in the environment. */
        int nodes = MPI.COMM_WORLD.Size();
        
        /* Number of slaves. getRaces() returns the number of masters.*/
        int slaves = nodes - getRaces();
        
        /* Check that we have enough slaves. */
        if(slaves % getRaces() != 0) {
            throw new RuntimeException("EvaluationTopology (doInitialize): To " +
                "few slaves. Slaves = " + slaves + ", Masters = " + getRaces() +
                ". Each master must have the same number of slaves.");
        }
        
        /* Number of slave per master. */
        int slavesPerMaster = slaves / getRaces();
        
        /* Rank of the first slave. */
        int firstSlave = getRaces();
        
        /* The first ranks are considered masters for creating the communicator. */
        /* For each master */
        for(int master = 0; master < getRaces(); master++) {
            
            /* For knowing if the communicator can be stored. */
            boolean storeComm = false;
            
            /* Ranks of the new group: master + slaves. */
            int[] rankList = new int[slavesPerMaster+1];
            
            /* Add the master to the group. */
            rankList[0] = master;
            
            /* Add the slaves. */
            for(int slave = firstSlave; slave < firstSlave + slavesPerMaster; slave++) {
                rankList[slave - firstSlave + 1] = slave;
                
                /* If this process is the slave we are processing */
                if(slave == MPI.COMM_WORLD.Rank()) {
                    storeComm = true; /* Mark the communicator as to be stored. */
                }
            }
            
            /* If this process is the current master */
            if(master == MPI.COMM_WORLD.Rank()) {
                storeComm = true; /* Mark the communicator as to be stored. */
            }
            
            /* If the communicator can be stored */
            if(storeComm) {
                /* Store the communicator. */
                this.communicator = MPI.COMM_WORLD.Create(MPI.COMM_WORLD.group.Incl(rankList));
            } else {
                /* Don't store the communicator. However, MPI requires that ALL
                 * processes in COMM_WORLD call Create, even if they don't belong
                 * to the new communicator, so do this. */
                MPI.COMM_WORLD.Create(MPI.COMM_WORLD.group.Incl(rankList));
            }
            
            /* The rank of the new first slave. */
            firstSlave += slavesPerMaster;
        }
        
        /* We have to choose a master for this topology. A few lines up it has been
         * said that the first ranks are considered masters. This was only for
         * creating the communicators. Now it is simplier to choose a fixed rank
         * within the communicator as a master. Otherwise, only the master would know
         * that he is the master. Slaves would know they are slaves, but wouldn't
         * know who's the master unless some commucation is made. So, the master
         * is the process with rank MASTER within the new communicator. Most times
         * the masters chosen this way will have the same rank as the masters
         * used for creating the communicator, but it must not be always so. */
        if(communicator.Rank() == MASTER) {
            this.master = true;
        } else {
            this.master = false;
        }
        
        /* Initialize the rest of the topology. */
        
        if(this.master) {
            
            /* The master sends to its slaves. */
            this.receivers = new int[communicator.Size()-1];
            for(int i = 0; i < this.receivers.length; i++) {
                this.receivers[i] = i+1;
            }
            
            /* The master receives from its slaves. */
            this.senders = new int[communicator.Size()-1];
            for(int i = 0; i < this.senders.length; i++) {
                this.senders[i] = i+1;
            }
            
            /* The rank of the master. */
            this.topologyRank = this.communicator.Rank();
            
            /* The rank of my master. */
            myMaster = NONE;
        } else {
            
            /* A slave sends only to the master. */
            this.receivers = new int[1];
            this.receivers[0] = MASTER;
            
            /* A slave only receives from the master. */
            this.senders = new int[1];
            this.senders[0] = MASTER;
            
            /* The rank of the slave. */
            this.topologyRank = this.communicator.Rank();
            
            /* The rank of my master. */
            myMaster = MASTER;
        }
        
        this.pendingSendRequest = new HashMap<Request, EvaluationObject[]>();
    }
    
    @Override
    protected void doConfigure() {
        /* No further configuration needed. */
    }
    
    @Override
    protected void doConfigure(Configuration conf) {
        /* No further configuration needed. */
    }
    
    /* Look for finished isends and update the pending requests list. */
    private void testSendRequests() {
        if((pendingSendRequest != null) && !pendingSendRequest.isEmpty()) {
            Request[] rS = new Request[pendingSendRequest.size()];
            rS = pendingSendRequest.keySet().toArray(rS);
            
            Status[] status = Request.Testsome(rS);
            for(int i = 0; i < status.length; i++) {
                pendingSendRequest.remove(rS[status[i].index]);
            }
        }
    }
    
    /* Adds a new request and the send buffer to the pending send requests list. */
    private void addSendRequest(Request request, EvaluationObject[] evalObj) {
        this.pendingSendRequest.put(request, evalObj);
    }
    
    /**
     * Performs the send operation.
     */
    protected void doSend(EvaluationObject evalObj) {
        
        testSendRequests();
        
        EvaluationObject[] buf = new EvaluationObject[1];
        buf[0] = evalObj;
        
        /* Standard mode nonblocking send. */
        Request sendRequest = communicator.Isend(buf, 0, buf.length, MPI.OBJECT,
            evalObj.getDest(), MSG_TAG);
        
        if(sendRequest.Test() == null) { /* Request pending */
            addSendRequest(sendRequest, buf);
        }
    }
    
    /**
     * Sends a {@link EvaluationObject} to a slave or to the master. The receiver
     * is known by the method {@link EvaluationObject#getDest} of <code>evalObj</code>.
     *
     * This method is implemented with the {@link EvaluationTopology#doSend} method.
     * It is a nonblocking send.
     */
    public void send(EvaluationObject evalObj) {
        if(!isInitialized()) {
            initialize();
        }
        doSend(evalObj);
    }
    
    /**
     * Performs the receive operation.
     */
    protected EvaluationObject doReceive() {
        EvaluationObject[] buf = new EvaluationObject[1];
        communicator.Recv(buf, 0, buf.length, MPI.OBJECT, MPI.ANY_SOURCE, MSG_TAG);
        return buf[0];
    }
    
    /**
     * Receives a {@link EvaluationObject} from a slave or from a master. For slaves
     * it returns <code>null</code> when the evaluation process is finished.
     * For all processes it returns <code>null</code> when an object is received
     * that's not an instance of {@link EvaluationObject}.<p>
     *
     * This method is implemented with the {@link EvaluationTopology#doReceive} method.
     * It is a blocking receive, so the caller waits until a message is arrived.
     */
    public EvaluationObject receive() {
        if(!isInitialized()) {
            initialize();
        }
        return doReceive();
    }
    
    /**
     * Checks if some evaluation object has been received.
     * @return <code>true</code> if an evaluation object has been received, 
     * <code>false</code> otherwise.
     */
    public boolean evaluationObjectReceived() {
        return this.getCommunicator().Iprobe(MPI.ANY_SOURCE, MPI.ANY_TAG) != null;
    }
    
    /**
     * Retuns the rank of the master of this topology. If there isn't any master
     * it returns {@link EvaluationTopology#NONE}.
     */
    public int getMaster() {
        if(!isInitialized()) {
            initialize();
        }
        return myMaster;
    }
    
    /**
     * Returns <code>true</code> if the calling process is the master process
     * of this topology. Returns <code>false</code> otherwise.
     */
    public boolean isMaster() {
        if(!isInitialized()) {
            initialize();
        }
        return master;
    }
    
    public void finish() {
        /* Make sure all send requests have finished before exit. */
        if((pendingSendRequest != null) && !pendingSendRequest.isEmpty()) {
            Request[] rS = new Request[pendingSendRequest.size()];
            rS = pendingSendRequest.keySet().toArray(rS);
            Request.Waitall(rS);
            pendingSendRequest.clear();
        }
    }
}
