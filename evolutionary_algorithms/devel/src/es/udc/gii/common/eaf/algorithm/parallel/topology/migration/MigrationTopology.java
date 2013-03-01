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
 * MigrationTopology.java
 *
 * Created on March 18, 2008, 8:40 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel.topology.migration;

import es.udc.gii.common.eaf.algorithm.parallel.topology.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mpi.MPI;
import mpi.Request;
import mpi.Status;

/**
 * A migration topology is a topology that encapsulates the comunication between
 * islands in an island model (parallel evolutionary algorithm).<p>
 *
 * A migration operator uses an instance of this class to perform the necessary
 * communication among islands making use of the {@link MigrationTopology#send}
 * and {@link MigrationTopology#receive} methods.<p>
 *
 * This is an abstract class, so it can not be directly used. Instead, some
 * subclasses are provided and the user can code their own. Subclasses of this class
 * are responsible for populating the protected atributes from {@link Topology} and
 * may optionally override de protected methods {@link MigrationTopology#doSend} and
 * {@link MigrationTopology#doReceive} in order to change the behaviour of the
 * {@link MigrationTopology#send} and {@link MigrationTopology#receive} methods.
 * However, a default implementation of the protected methods is given.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class MigrationTopology extends Topology {

    /* Stores the pending communication requests (send). */
    private Map<Request, MigrationObject[]> pendingSendRequest = null;
    /* Stores the pending communication requests (receive). */
    private Map<Request, MigrationObject[]> pendingRecvRequest = null;
    /** Tag used for sending and receiving. */
    private final static int TAG = 0;

    /* Creates an instance of MigrationTopology. */
    public MigrationTopology() {
    }

    /* Creates an instance of MigrationTopology. */
    public MigrationTopology(int races) {
        super(races);
    }

    /* Looks for finished send communications and updates the pending list. */
    private void testSendRequests() {
        if ((pendingSendRequest != null) && !pendingSendRequest.isEmpty()) {
            Request[] rS = new Request[pendingSendRequest.size()];
            rS = pendingSendRequest.keySet().toArray(rS);

            Status[] status = Request.Testsome(rS);
            for (int i = 0; i < status.length; i++) {
                pendingSendRequest.remove(rS[status[i].index]);
            }
        }
    }

    /* Adds a new send request and the send buffer to the pending requests list. */
    private void addSendRequest(Request request, MigrationObject[] migrant) {
        if (pendingSendRequest == null) {
            pendingSendRequest = new HashMap<Request, MigrationObject[]>();
        }

        pendingSendRequest.put(request, migrant);
    }

    /* Returns a list with the delayed migrants and updates the pending list. */
    private List<MigrationObject> testRecvRequests() {

        List<MigrationObject> delayedMigrants = new ArrayList<MigrationObject>();

        if (pendingRecvRequest != null) {
            Request[] rR = new Request[pendingRecvRequest.size()];
            rR = pendingRecvRequest.keySet().toArray(rR);

            Status[] status = Request.Testsome(rR);

            for (int i = 0; i < status.length; i++) {
                Request r = rR[status[i].index];
                MigrationObject delayedMigrant =
                        ((MigrationObject[]) pendingRecvRequest.get(r))[0];
                if (delayedMigrant != null) {
                    delayedMigrants.add(delayedMigrant);
                }
                pendingRecvRequest.remove(r);
            }
        }

        return delayedMigrants;
    }

    /* Adds a new request and the receive buffer to the pending requests list. */
    private void addRecvRequest(Request request, MigrationObject[] migrant) {
        if (pendingRecvRequest == null) {
            pendingRecvRequest = new HashMap<Request, MigrationObject[]>();
        }
        pendingRecvRequest.put(request, migrant);
    }

    /** Each subclass can implement the send method accordingly. */
    protected void doSend(MigrationObject migrant) {

        MigrationObject[] sendBuf = new MigrationObject[1];
        sendBuf[0] = migrant;

        /* Complete previous sends. */
        testSendRequests();

        for (int i = 0; i < getReceivers().length; i++) {
            Request sendRequest = getCommunicator().Isend(sendBuf, 0,
                    sendBuf.length, MPI.OBJECT, getReceivers()[i], TAG);
            if (sendRequest.Test() == null) { /* Request pending */
                addSendRequest(sendRequest, sendBuf);
            }
        }
    }

    /**
     * Sends a {@link MigrationObject} to the receivers. It is a nonblocking
     * send.
     */
    public void send(MigrationObject migrant) {
        if (isInitialized()) {
            doSend(migrant);
        } else {
            initialize();
            doSend(migrant);
        }
    }

    /** Each subclass can implement the receive method accordingly. */
    protected List<MigrationObject> doReceive() {
        MigrationObject[] recvBuf = new MigrationObject[1];
        List<MigrationObject> migrants = new ArrayList<MigrationObject>();

        /* Test if some delayed migrant (from a previous receive) has arrived
         * and get them. */
        migrants.addAll(testRecvRequests());

        /* Try to receive a migrant from each sender. */
        for (int i = 0; i < getSenders().length; i++) {

            Request request = getCommunicator().Irecv(recvBuf, 0, recvBuf.length,
                    MPI.OBJECT, getSenders()[i], TAG);

            if (request.Test() != null) { /* If received */
                if (recvBuf[0] != null) {
                    migrants.add(recvBuf[0]);
                }
            } else {
                addRecvRequest(request, recvBuf); /* Request pending. */
            }

        }

        return migrants;
    }

    /**
     * Receives a list of {@link MigrationObject} from the senders. It is a
     * nonblocking receive. If no message has arrived, an empty list is returned.
     */
    public List<MigrationObject> receive() {
        if (isInitialized()) {
            return doReceive();
        } else {
            initialize();
            return doReceive();
        }
    }

    /**
     * Cleans up this topology by finalizing all pending communications. This
     * method must be called when the topology is not need anymore.
     */
    public void finish() {

        /* Make sure all send requests have finished before exit. */
        if ((pendingSendRequest != null) && !pendingSendRequest.isEmpty()) {
            Request[] rS = new Request[pendingSendRequest.size()];
            rS = pendingSendRequest.keySet().toArray(rS);
            Request.Waitall(rS);
            pendingSendRequest.clear();
        }

        /* Make sure all recv requests have finished before exit. The received
         * objects are ignored and get lost. */
        if ((pendingRecvRequest != null) && !pendingRecvRequest.isEmpty()) {
            Request[] rR = new Request[pendingRecvRequest.size()];
            rR = pendingRecvRequest.keySet().toArray(rR);
            Request.Waitall(rR);
            pendingRecvRequest.clear();
        }
    }
}
