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
 * ParallelLogTool.java
 *
 * Created on April 1, 2008, 2:47 PM
 *
 */
package es.udc.gii.common.eaf.log.parallel;

import es.udc.gii.common.eaf.log.LogTool;
import mpi.MPI;

/**
 * The base class for all parallel log tools.<p/>
 *
 * For using this class, a parallel environment must be configured and
 * initialized.<p/>
 *
 * This class ensures that each node has a unique named log file. The file name
 * is that supplied by the user followed by an underscore and the rank number
 * of the node. For example, if the user supplied the file name xxxx.txt, the
 * log file of the first 3 nodes would be xxxx_0.txt, xxxx_1.txt and xxxx_2.txt
 * respectively.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class ParallelLogTool extends LogTool {

    protected long t0 = System.currentTimeMillis();

    /** Creates a new instance of ParallelLogTool */
    public ParallelLogTool() {
        if (!MPI.Initialized()) {
            throw new RuntimeException("Parallel environment not yet initialized!");
        }
        this.name += "_ND";
    }

    @Override
    public String getNodeID() {
        return String.valueOf(MPI.COMM_WORLD.Rank());
    }
}
