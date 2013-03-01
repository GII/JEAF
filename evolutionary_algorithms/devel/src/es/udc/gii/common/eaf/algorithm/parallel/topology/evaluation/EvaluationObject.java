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
 * EvaluationObject.java
 *
 * Created on March 24, 2008, 5:47 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel.topology.evaluation;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an envelope for exchanging individuals between
 * master nodes and slave nodes where they have to be evaluated.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EvaluationObject implements Externalizable {

    /** Special value for the source and dest rank when they are not known. */
    public final static int NOT_SPECIFIED = -1;
    /** Rank of the source node. */
    private int source = NOT_SPECIFIED;
    /** Rank of the destiny node. */
    private int dest = NOT_SPECIFIED;
    /** Individuals to send or individuals received. */
    private List<Individual> individuals = null;

    /** Creates a new instance of EvaluationObject */
    public EvaluationObject() {
    }
    
    /** Creates a new instance of EvaluationObject */
    public EvaluationObject(List<Individual> individuals, int source, int dest) {
        setIndividuals(individuals);
        setSource(source);
        setDest(dest);
    }

    /**
     * Clones this object. Take care: the list of individiduals is not cloned.
     * Thus this object and its clone share the same list!
     */
    @Override
    public Object clone() {
        return new EvaluationObject(getIndividuals(), getSource(), getDest());
    }

    @Override
    public String toString() {
        String sourceStr = getSource() == NOT_SPECIFIED ? "NOT_SPECIFIED" : String.valueOf(getSource());
        String destStr = getDest() == NOT_SPECIFIED ? "NOT_SPECIFIED" : String.valueOf(getDest());

        return "Source = " + sourceStr + " Dest = " + destStr + " Individuals = " + getIndividuals();
    }

    /* ---- Getters anda setters follow ---- */
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        if (!(individuals instanceof Serializable) && (individuals != null)) {
            this.individuals = new ArrayList<Individual>();
            this.individuals.addAll(individuals);            
        } else {
            this.individuals = individuals;
        }
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EvaluationObject) {
            EvaluationObject peer = (EvaluationObject) obj;
            return (this.source == peer.source) &&
                    (this.dest == peer.dest) &&
                    (this.individuals.equals(peer.individuals));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.source;
        hash = 29 * hash + this.dest;
        hash = 29 * hash + (this.individuals != null ? this.individuals.hashCode() : 0);
        return hash;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        /* Write source */
        out.writeInt(this.source);

        /* Write dest */
        out.writeInt(this.dest);

        /* Write individuals */
        if (this.individuals == null) {
            /* Number of individuals (-1 means individual list == null)*/
            out.writeInt(-1);            
        } else {
            out.writeInt(this.individuals.size());
            for (Individual i : individuals) {
                out.writeObject(i);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        /* Read the source */
        this.source = in.readInt();

        /* Read dest */
        this.dest = in.readInt();

        /* Read individuals */
        int size = in.readInt();        
        if (size == -1) {
            this.individuals = null;
        } else {
            this.individuals = new ArrayList<Individual>(size);
            for (int i = 0; i < size; i++) {
                this.individuals.add((Individual) in.readObject());
            }
        }
    }
}
