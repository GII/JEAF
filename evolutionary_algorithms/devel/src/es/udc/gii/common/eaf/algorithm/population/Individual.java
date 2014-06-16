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
 * Individual.java
 *
 * Created on 24 de octubre de 2006, 17:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.algorithm.population;

import es.udc.gii.common.eaf.algorithm.fitness.comparator.FitnessComparator;
import es.udc.gii.common.eaf.config.Configurable;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.util.DoubleArray;
import org.apache.commons.math.util.ResizableDoubleArray;

/**
 * This class represents a basic individual of the population.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class Individual implements Cloneable, Configurable, Externalizable {

    /**
     * Fitness value of the individual.
     */
    private double fitness = Double.MAX_VALUE;    //15-04-2008: añadido un vector de valores objetivo y un numero de restr. violadas:

    /**
     * List of the objective values.
     */
    private List<Double> objectives = null;
    
    //13-12-2007: añadido un vector de valores de restricciones:
    /**
     * List of the constraint values.
     */
    private List<Double> constraints = null;

    /**
     * Number of violated constraints.
     */
    private int violatedConstraints = 0;    //pilar - 23 - 07 - 08: Array de cromosomas

    /**
     * Array of chromosomes of the individual.
     */
    private DoubleArray[] chromosomes = null;

    
    private boolean serializeEvalResults = true;
    private boolean serializeGenotype = true;

    /**
     * Fitness comparator of the individual.
     */
    private FitnessComparator<Individual> comparator = null;

    /** Creates a new instance of Individual */
    public Individual() {
    }

    /**
     * Creates a new instance of Individual with a DoubleArray of chromosomes.
     * @param chromosomes DoubleArray of the individual's chromosomes.
     */
    public Individual(DoubleArray[] chromosomes) {

        this.chromosomes = chromosomes;
    }

    /**
     * Configures the current individual.
     * @param conf configuration's parameters to configure the current individual.
     */
    @Override
    public void configure(Configuration conf) {
    }

    /**
     * Returns the fitness comparator of this individual.
     * @return the fitness comparator of this individual.
     */
    public FitnessComparator<Individual> getComparator() {
        return comparator;
    }

    /**
     * Sets the fitness comparator of this individual.
     * @param comparator the fitness comparator of this individual.
     */
    public void setComparator(FitnessComparator<Individual> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns the fitness value of the Individual.
     * 
     * @return fitness the fitness value of the Individual.
     */
    public double getFitness() {

        return this.fitness;

    }

    /**
     * Sets the fitness value of the Individual.
     * 
     * @param fitness the new fitness value of the Individual.
     */
    public void setFitness(double fitness) {

        this.fitness = fitness;

    }

    /**
     * Returns the chromosomes of the individual.
     * @return chromosomes an array of chromososmes of the individual.
     */
    public DoubleArray[] getChromosomes() {

        return this.chromosomes;

    }

    /**
     * Returns the chromosome at <i>index</i> of the individual.
     * 
     * @param index index of the chromosome to return.
     * @return the double array of the chromosome at index.
     */
    public double[] getChromosomeAt(int index) {

        return ((ResizableDoubleArray) this.chromosomes[index]).getElements();

    }

    /**
     * Sets an array of chromosomes to the current Individual.
     * @param chromosomes a new array of chromosomes.
     */
    public void setChromosomes(DoubleArray[] chromosomes) {

        this.chromosomes = chromosomes;

    }

    /**
     * Sets a double array chromosome to the current individual at position <i>index</i>.
     * @param index position to set the new chromosome.
     * @param chromosome a chromosme to be set at position <i>index</i>.
     */
    public void setChromosomeAt(int index, double[] chromosome) {

        if (this.chromosomes == null) {
            this.chromosomes = new DoubleArray[1];
        }
        
        this.chromosomes[index] = new ResizableDoubleArray(chromosome.length);
        for (int i = 0; i < chromosome.length; i++) {
            this.chromosomes[index].setElement(i, chromosome[i]);
        }


    }

    /**
     * Returns the number of violated constraints.
     * @return violated_contraint the number of violated constraints.
     */
    public int getViolatedConstraints() {
        return violatedConstraints;
    }

    /**
     * Sets the number of violated constraints of the Individual.
     * @param violatedConstraints number of violated constraints.
     */
    public void setViolatedConstraints(int violatedConstraints) {
        this.violatedConstraints = violatedConstraints;
    }

    /**
     * Restuns the value of the objective functions evaluate with the chromosomes 
     * of this Individual.
     * @return objectives a list with the values of the objective functions 
     * evaluate with this Individual.
     */
    public List<Double> getObjectives() {
        return objectives;
    }

    /**
     * Sets the values of the objectives functions evaluate by this individual.
     * @param objectives list of values of the objective functions.
     */
    public void setObjectives(List<Double> objectives) {
        this.objectives = objectives;
    }

    /**
     * Returns the values of the contraint functions evaluate with the chromosomes of
     * this Individual.
     * @return contraints list of values of the contraint functions.
     */
    public List<Double> getConstraints() {
        return constraints;
    }

    /**
     * Sets the values of the constraint functions evaluate by this individual.
     * @param constraints list of values of the objective functions.
     */
    public void setConstraints(List<Double> constraints) {
        this.constraints = constraints;
    }

    /**
     * This method controls the behaviour of the serialization of an instance of
     * this class. Setting <code>true</code> means that the genotype information
     * will be serialized (and hence will be transmitted, for example, via sockets).
     * Setting <code>false</code> means that the genotype information won't be
     * serialized.
     * 
     * @param serializeGenotype
     */
    public void setSerializeGenotype(boolean serializeGenotype) {
        this.serializeGenotype = serializeGenotype;
    }

    /**
     * This method controls the behaviour of the serialization of an instance of
     * this class. Setting <code>true</code> means that the evaluation results
     * (objectives, contraint values, ...)
     * will be serialized (and hence will be transmitted, for example, via sockets).
     * Setting <code>false</code> means that this information won't be
     * serialized.
     * 
     * @param serializePhenotype {@code true} or {@code false}.
     */
    public void setSerializeEvalResults(boolean serializePhenotype) {
        this.serializeEvalResults = serializePhenotype;
    }

    /**
     * Before and while serialization this method returns <code>true</code> if
     * the genotype information should be serialized and <code>false</code>
     * otherwise.<p>
     * 
     * While de-serialization this method returns <code>true</code> if
     * the genotype information should be de-serialized. After de-serialization
     * it returns <code>true</code> if genotype information has been
     * de-serialized. It returns <code>false</code> otherwise.   
     */
    public boolean isSerializeGenotype() {
        return serializeGenotype;
    }

    /**
     * Before and while serialization this method returns <code>true</code> if
     * the evaluation results should be serialized and <code>false</code>
     * otherwise.<p>
     * 
     * While de-serialization this method returns <code>true</code> if
     * the evaluation results should be de-serialized. After de-serialization
     * it returns <code>true</code> if the evaluation results heve been
     * de-serialized. It returns <code>false</code> otherwise.   
     */
    public boolean isSerializeEvalResults() {
        return serializeEvalResults;
    }

    /**
     * Generates the chromosomes of the Individual with values in [-1.0,1.0]
     */
    public void generate() {
        
        if (this.chromosomes == null) {
            this.chromosomes = new DoubleArray[1];
            this.chromosomes[0] = new ResizableDoubleArray(this.getDimension());
        }
        
        for (int i = 0; i < this.chromosomes.length; i++) {

            for (int j = 0; j < this.chromosomes[i].getNumElements(); j++) {


                this.chromosomes[i].setElement(j, EAFRandom.nextDouble() * 2.0 - 1.0);

            }

        }

    }

    /**
     * Returns the dimension of this individual as the sum of the dimensions of all its chromosomes.
     * @return the dimension of this individual.
     */
    public int getDimension() {

        int dimension = 0;

        for (DoubleArray d : this.chromosomes) {
            dimension += d.getNumElements();
        }

        return dimension;

    }

    /**
     * Sets the dimension of each chromosome of the individual.
     * @param dimensions array of dimensions.
     */
    public void setDimension(int[] dimensions) {

        for (int i = 0; i < this.chromosomes.length; i++) {

            this.setChromosomeAt(i, new double[dimensions[i]]);

        }

        this.generate();
    }

    /**
     * Resturns a String representation of the Individual.
     * @return a string representation of the Individual.
     */
    @Override
    public String toString() {
        if (this.chromosomes != null) {
            String s = "[";

            for (int i = 0; i < this.chromosomes.length; i++) {

                s += "(";

                for (int j = 0; j < this.chromosomes[i].getNumElements() - 1; j++) {

                    s += this.chromosomes[i].getElement(j) + ", ";

                }

                s += this.chromosomes[i].getElement(this.chromosomes[i].getNumElements() - 1);

            }


            s += "] - " + this.fitness;

            return s;
        } else {
            return "[unknown genotype] - " + this.getFitness();
        }
    }

    /**
     * Clones the current individual.
     * @return a new individual which is a copy of the current one.
     */
    @Override
    public Object clone() {

        Individual clone = null;
        DoubleArray[] clone_chromosomes = null;

        try {
            clone = (Individual) super.clone();
        } catch (CloneNotSupportedException e) {
            assert false;
        }

        //Clono los cromosomas:
        if (this.chromosomes != null) {
            clone_chromosomes = new DoubleArray[this.chromosomes.length];

            for (int i = 0; i < this.chromosomes.length; i++) {

                clone_chromosomes[i] = new ResizableDoubleArray(this.chromosomes[i].getNumElements());
                for (int j = 0; j < this.chromosomes[i].getNumElements(); j++) {

                    clone_chromosomes[i].setElement(j, this.chromosomes[i].getElement(j));

                }

            }
        }

        clone.setChromosomes(clone_chromosomes);

        clone.setFitness(this.fitness);
        clone.setSerializeGenotype(serializeGenotype);
        clone.setSerializeEvalResults(serializeEvalResults);
        clone.setViolatedConstraints(violatedConstraints);

        /* Clone objective values */
        List<Double> obj = null;
        if (objectives != null) {
            obj = new ArrayList<Double>(objectives.size());

            for (Double d : objectives) {
                obj.add(new Double(d.doubleValue()));
            }
        }

        clone.setObjectives(obj);

        /* Clone constraint values */
        List<Double> contr = null;
        if (constraints != null) {
            contr = new ArrayList<Double>(constraints.size());

            for (Double d : constraints) {
                contr.add(new Double(d.doubleValue()));
            }
        }

        clone.setConstraints(contr);

        /* Copy comparator. */
        clone.setComparator(getComparator());

        return clone;
    }

    /**
     * Tests if two individuals are or are not equals.
     * @param obj another Individual to compare.
     * @return true if they are equal, false in other case.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Individual other = (Individual) obj;
        if (this.fitness != other.fitness) {
            return false;
        }
        if (this.objectives != other.objectives && (this.objectives == null || !this.objectives.equals(other.objectives))) {
            return false;
        }
        if (this.constraints != other.constraints && (this.constraints == null || !this.constraints.equals(other.constraints))) {
            return false;
        }
        if (this.violatedConstraints != other.violatedConstraints) {
            return false;
        }
        if (this.chromosomes != other.chromosomes &&  (this.chromosomes == null || /* !Arrays.equals(this.chromosomes, other.chromosomes)) */
                !this.checkChromosomes(this.chromosomes, other.chromosomes)))  {
            return false;
        }

        if (this.serializeGenotype != other.serializeGenotype) {
            return false;
        }

        if (this.serializeEvalResults != other.serializeEvalResults) {
            return false;
        }

        return true;
    }

    private boolean checkChromosomes(DoubleArray[] c_1, DoubleArray[] c_2) {

        DoubleArray d_1, d_2;

        if (c_1.length != c_2.length) {
            return false;
        }

        for (int i = 0; i < c_1.length; i++) {

            d_1 = c_1[i];
            d_2 = c_2[i];

            if (d_1.getNumElements() != d_2.getNumElements()) {
                return false;
            }


            if (!Arrays.equals(d_1.getElements(), d_2.getElements())) {
                return false;
            }

        }

        return true;
    }

    /**
     * Resturns the hashCode of an Individual.
     * @return the hashCode of an Individual.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.fitness) ^ (Double.doubleToLongBits(this.fitness) >>> 32));
        hash = 97 * hash + (this.objectives != null ? this.objectives.hashCode() : 0);
        hash = 97 * hash + (this.constraints != null ? this.constraints.hashCode() : 0);
        hash = 97 * hash + this.violatedConstraints;
        hash = 97 * hash + (this.chromosomes != null ? this.chromosomes.hashCode() : 0);
        hash = 97 * hash + (this.serializeGenotype ? 1 : 0);
        hash = 97 * hash + (this.serializeEvalResults ? 1 : 0);
        return hash;
    }

    /**
     * This method is called whenever an instance of this class has to be serialized.<p>
     * 
     * It might write to the output <code>out</code> the evaluation results
     * or the genotype information or both, deppending on the value of 
     * Individual#getSerializeEvalResults and Individual#getSerializeGenotype,
     * which are always writen at the beginning of the output to know later what
     * type of information is contained in the data.<p>
     * 
     * Subclasses should override this method if they introduce new attibutes.
     * Remember to call <code>super.writeExternal()</code> in order to
     * be sure that the state of the parent class is serialized.    
     * 
     * @param out - DataOutput to write the serialized bytes to.
     * @throws java.io.IOException
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        /* Phenotype serialized? */
        out.writeBoolean(this.serializeEvalResults);

        /* Genotype serialized? */
        out.writeBoolean(this.serializeGenotype);

        if (this.serializeEvalResults) {
            /* Serialize number of violated constraints */
            out.writeInt(this.violatedConstraints);

            /* Serialize fitness */
            out.writeDouble(this.fitness);

            /* Serialize objective values */
            if (this.objectives == null) {
                out.writeInt(-1);
            } else {
                out.writeInt(this.objectives.size());
                for (Double d : this.objectives) {
                    out.writeDouble(d.doubleValue());
                }
            }

            /* Serialize constraint values */
            if (this.constraints == null) {
                out.writeInt(-1);
            } else {
                out.writeInt(this.constraints.size());
                for (Double d : this.constraints) {
                    out.writeDouble(d.doubleValue());
                }
            }
        }

        if (this.serializeGenotype) {
            /* Serialize the chromosomes */
            if (this.chromosomes == null) {
                out.writeInt(-1);
            } else {
                out.writeInt(this.chromosomes.length);
                for (int i = 0; i < this.chromosomes.length; i++) {
                    int genes = this.chromosomes[i].getNumElements();
                    out.writeInt(genes);

                    for (int j = 0; j < genes; j++) {
                        out.writeDouble(this.chromosomes[i].getElement(j));
                    }
                }
            }
        }

    }

    /**
     * This method is called whenever an instance of this class has to be
     * de-serialized.<p>
     * 
     * It sets the values of Individual#getSerializeEvalResults and
     * Individual#getSerializeGenotype accordingly to the information received
     * so that subclasses can rely on them to know what kind of information is
     * to be read.<p>
     * 
     * Subclasses should override this method if they introduce new attibutes.
     * Remember to call <code>super.readExternal()</code> in order to
     * be sure that the state of the parent class is de-serialized and the values
     * of Individual#getSerializeEvalResults and Individual#getSerializeGenotype
     * contain the right information.
     * 
     * @param in - DataInput from which the bytes are read.
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        /* Retrieve the type of information comming in */
        boolean evals = in.readBoolean();
        boolean genotype = in.readBoolean();

        this.serializeEvalResults = evals;
        this.serializeGenotype = genotype;

        /* Read the evaluation results, if present */
        if (evals) {
            /* Read number of violated constraints */
            this.violatedConstraints = in.readInt();

            /* Read the fitness value */
            this.fitness = in.readDouble();

            /* Read the number of objectives (-1 means no objective values) */
            int nObjs = in.readInt();

            if (nObjs == -1) {
                this.objectives = null;
            } else {
                /* Read all objective values */
                this.objectives = new ArrayList<Double>(nObjs);
                for (int i = 0; i < nObjs; i++) {
                    this.objectives.add(in.readDouble());
                }
            }

            /* Read the number of constraints (-1 means no constraint values) */
            int nConstr = in.readInt();

            if (nConstr == -1) {
                this.constraints = null;
            } else {
                /* Read all constraint values */
                this.constraints = new ArrayList<Double>(nConstr);
                for (int i = 0; i < nConstr; i++) {
                    this.constraints.add(in.readDouble());
                }
            }
        }

        /* Read the genotype information, if present */
        if (genotype) {
            int nChroms = in.readInt();

            if (nChroms == -1) {
                this.chromosomes = null;
            } else {
                /* Read all the chromosomes */
                this.chromosomes = new DoubleArray[nChroms];
                for (int i = 0; i < nChroms; i++) {
                    int nGenes = in.readInt();
                    this.chromosomes[i] = new ResizableDoubleArray(nGenes);
                    for (int j = 0; j < nGenes; j++) {
                        this.chromosomes[i].addElement(in.readDouble());
                    }
                }
            }
        }
    }

    /**
     * Copies the evaluation results of this individual to the passed individual. No deep
     * copy is made, so this object and the passed individual share this
     * information.<p>
     * 
     * Subclasses of <code>Individual</could> have to override this method and call
     * <code>super.copyEvalResults()</code> in order to handle the copy of the 
     * possible extra attibutes introduced in the subclass.
     * 
     * @param other - The individual into which the phenotype of this object will
     * be copied.
     */
    public void copyEvalResults(Individual other) {
        other.setViolatedConstraints(getViolatedConstraints());
        other.setFitness(getFitness());
        other.setObjectives(getObjectives());
        other.setConstraints(getConstraints());
    }

    /**
     * Copies the genotype of this individual to the passed individual. No deep
     * copy is made, so this object and the passed individual share the genotype
     * information.<p>
     * 
     * Subclasses of <code>Individual</could> have to override this method and call
     * <code>super.copyGenotype()</code> in order to handle the copy of the 
     * extra genotype information introduced in the subclass.
     * 
     * @param other - The individual into which the genotype of this object will
     * be copied.
     */
    public void copyGenotype(Individual other) {
        other.chromosomes = this.chromosomes;
        other.setChromosomes(getChromosomes());
    }
}
