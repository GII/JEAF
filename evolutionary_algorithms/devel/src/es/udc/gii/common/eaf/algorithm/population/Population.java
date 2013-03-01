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



package es.udc.gii.common.eaf.algorithm.population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the list of individuals that will be used to solve the problem.
 *
 * @see Individual
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0.
 */
public class Population {

    /**
     * A list of individuals.
     */
    private List<Individual> individuals;

    /**
     * The size of this population.
     */
    private int size;

    //Archivo, sólo se necesita en JADE pero lo pongo aquí por si se utilizase en un futuro en otros algoritmos.
    private List<Individual> archive;

    /**
     * Creates an instance of this class with an empty list of individuals.
     */
    public Population() {
    
        this.individuals = Collections.synchronizedList(new ArrayList<Individual>());
        
    }

    /**
     * Creates an instance of this class with individuals as the list of individuals.
     * @param individuals a list of individuals.
     */
    public Population(List<Individual> individuals) {
        
        this.individuals = Collections.synchronizedList(individuals);
        
    }

    /**
     * Generates a new list of individuals.
     * @see Individual#generate() 
     */
    public void generate() {
        
        for (int i = 0; i<this.individuals.size(); i++) {
            
            this.individuals.get(i).generate();
            
        }
        
    }

    /**
     * Increses or decreases the size of the population depending on the value of the parameter size.
     * If it is necessary create new individuals, these individuals are also generate.
     * @param size the new population size. If size is greater than current size, new individuals are added.
     * If size is lower than current size, the last individuals are removed.
     */
    public void modifyPopulationSize(int size) {
        
        this.size = size;
        Individual individual, newIndividual;
        
        
        if (this.size > this.individuals.size()) {
            
            individual = this.individuals.get(0);
            
            for (int i = this.individuals.size(); i<this.size; i++) {
                
                newIndividual = (Individual)individual.clone();
                newIndividual.generate();
                this.individuals.add(newIndividual);
                
            }
            
        } else {
            
            while (this.individuals.size() > this.size) {
                this.individuals.remove(this.individuals.size()-1);
            }
            
        }
        
    }
    
    /**
     * Returns the size of the population.
     * @return the size of the current population.
     */
    public int getSize() {
        
        return this.individuals.size();
        
    }

    /**
     * Sets the size of the population.
     * @param size the new population size.
     */
    public void setSize(int size) {
        this.size = size;
        
    }

    /**
     * Returns the list of individuals.
     * @return the list of individuals.
     */
    public List<Individual> getIndividuals() {
        
        return this.individuals;
        
    }

    /**
     * Sets the list of individuals as the current list of individuals.
     * @param individuals a new list of individuals.
     */
    public void setIndividuals(List<Individual> individuals) {
        
        
        this.individuals = individuals;
        
    }

    /**
     * Returns the individial at position index in the list of individuals.
     * @param index the index of the desired individual.
     * @return the individual at position index.
     */
    public Individual getIndividual(int index) {
        
        return this.individuals.get(index);
        
    }

    /**
     * Returns a copy of the individuals' list.
     * @return a copy of the individuals' list.
     */
    public List<Individual> getIndividualsCopy() {

        List<Individual> copy = new ArrayList<Individual>();

        for (Individual i : this.individuals) {
            copy.add((Individual) i.clone());
        }

        return copy;

    }

    /**
     * Adds an individual to the current list.
     * @param individual an individual to be added.
     */
    public void addIndividual(Individual individual) {
        
        this.individuals.add(individual);
        
    }

    /**
     * Adds a list of individuals to the current list.
     * @param individuals a list of individuals to be added.
     */
    public void addIndividuals(List<Individual> individuals) {
        
        this.individuals.addAll(individuals);
        
    }

    /**
     * Removes a list of individuals from the current list.
     * @param individuals a list of individuals to be removed.
     */
    public void removeIndividuals(List<Individual> individuals) {
        
        for(int i=0; i < individuals.size(); i++) {
            this.individuals.remove(individuals.get(i));
        }                
    }
    /**
     * Sets the dimensions of the chromosomes of all the individuals.
     * @param dimensions an array of dimensions.
     */
    public void setDimension(int[] dimensions) {
        
        for (Individual i : this.individuals) {
            i.setDimension(dimensions);
        }
        
    }

    public List<Individual> getArchive() {
        return archive;
    }

    public void setArchive(List<Individual> archive) {
        this.archive = archive;
    }

    
    
    @Override
    public String toString() {
        
        Individual[] arrayIndividuals = new Individual[this.individuals.size()];
        String s = "{\n";
        
        arrayIndividuals = this.individuals.toArray(arrayIndividuals);
        
        for (int i = 0; i<arrayIndividuals.length; i++) {
            
            s += arrayIndividuals[i].toString() + "\n";
            
        }
        
        s += "}\n";
        
        return s;
        
    }

    public Population getSubPopulation(int size) {
        Population sub_population = new Population();
        
        sub_population.setIndividuals(this.getIndividuals().subList(0, size));
        
        
        return sub_population;
        
    }
    
}

