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
 * MigrationObject.java
 *
 * Created on March 12, 2008, 7:25 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel.topology.migration;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A exchange object is a sort of envelope that contains the data to exchange
 * between to processes in a parallel evolutionary algorithm.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MigrationObject implements Cloneable, Externalizable {

    /** List of individuals to exchange. */
    private List<Individual> individuals = null;
    /** Current generation at which the sending process is. */
    private int generation = NOT_SPECIFIED;
    /** Special value of a generation. It states that the current generation is
     * unknown, not needed, etc. */
    public final static int NOT_SPECIFIED = -1;

    /**
     * Creates a new instance of MigrationObject
     */
    public MigrationObject() {
    }

    /**
     * Creates a new instance of MigrationObject
     */
    public MigrationObject(List<Individual> individuals, int generation) {
        setIndividuals(individuals);
        setGeneration(generation);
    }

    @Override
    public String toString() {
        String gen = generation == NOT_SPECIFIED ? "Not specified" : "" + generation;
        Population p = new Population();
        p.setIndividuals(individuals);
        return "Generation: " + gen + ", Individuals: \n" + p;
    }

    /**
     * Clones this object. Take care: the list of individiduals is not cloned.
     * Thus this object and its clone share the same list!
     */
    @Override
    public Object clone() {
        Object clone = null;

        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            /* This should not happen since this class has no other superclass than
             * Object itself.
             */
        }        

        ((MigrationObject) clone).setGeneration(getGeneration());
        ((MigrationObject) clone).setIndividuals(getIndividuals());

        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MigrationObject) {
            MigrationObject peer = (MigrationObject) obj;
            return (peer.generation == this.generation) && (peer.individuals.equals(this.individuals));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.individuals != null ? this.individuals.hashCode() : 0);
        hash = 13 * hash + this.generation;
        return hash;
    }

    /* ---- Getters and setters follow ---- */
    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {

        /* If the individuals list is not serializable we must create a list
         * which is serializable. */
        if (!(individuals instanceof Serializable)) {
            this.individuals = new ArrayList<Individual>();
            this.individuals.addAll(individuals);
        } else {
            this.individuals = individuals;
        }
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        /* Write generation */
        out.writeInt(this.generation);

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
        /* Read generation */
        this.generation = in.readInt();

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
