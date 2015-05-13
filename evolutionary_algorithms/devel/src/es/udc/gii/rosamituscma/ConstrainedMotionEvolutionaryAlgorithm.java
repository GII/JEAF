package es.udc.gii.rosamituscma;

/*
 * Copyright (C) 2011 Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.EvolutionaryStrategy;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.Population;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class ConstrainedMotionEvolutionaryAlgorithm extends EvolutionaryStrategy {

    private List<Individual> buffer_population;
    

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
    }

    @Override
    protected void init() {
        super.init();
        this.buffer_population = new ArrayList<Individual>();
    }

    @Override
    protected synchronized void reproduce(Population population) {
//        super.reproduce(population);

        while (buffer_population.size() < this.getPopulation().getSize()) {
            try {
                wait(10);
            } catch (InterruptedException ex) {
                System.out.println("Exception at CMARosaMitusEvolutionaryAlgorithm reproduce phase: " + ex.getMessage());
                System.exit(0);
            }
        }

        //La población buffer es la que tiene la información de la calidad:
        List<Individual> sub_population = this.buffer_population.subList(0, this.getPopulation().getSize());
//        for (int i = 0; i < sub_population.size(); i++) {
//            population.getIndividuals().set(i, (Individual) sub_population.get(i).clone());
//        }
        if (this.getReproductionChain() != null) {
            super.reproduce(new Population(sub_population));
        }
        synchronized (this.buffer_population) {
            for (int i = 0; i < this.getPopulation().getSize(); i++) {
                this.buffer_population.remove(0);
            }
            System.gc();
        }

    }

    @Override
    protected synchronized void replace(Population toPopulation) {
        
        //La "toPopulation" es una sub-población de buffer_population:
        
        while (buffer_population.size() < this.getPopulation().getSize()) {
            try {
                wait(10);
            } catch (InterruptedException ex) {
                System.out.println("Exception at CMARosaMitusEvolutionaryAlgorithm reproduce phase: " + ex.getMessage());
                System.exit(0);
            }
        }

        //La población buffer es la que tiene la información de la calidad:
        Population sub_population = new Population(this.buffer_population.subList(0, this.getPopulation().getSize()));
        if (this.getReplaceChain() != null) {
            super.replace(sub_population);
        }
        synchronized (this.buffer_population) {
            for (int i = 0; i < this.getPopulation().getSize(); i++) {
                this.buffer_population.remove(0);
            }
            System.gc();
        }
    }

    public synchronized void addIndividual(double[] chromosome, double fitness_values) {

        Individual ind;

        while (this.state == EvolutionaryAlgorithm.INIT_STATE) {
            try {
                wait(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConstrainedMotionEvolutionaryAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        synchronized (this.buffer_population) {
            if (chromosome.length == this.getPopulation().getIndividual(0).getDimension()) {

                ind = new Individual();

                ind.setChromosomeAt(0, chromosome);
                ind.setFitness(fitness_values);

                buffer_population.add(ind);

                notifyAll();


            }
        }


    }

    public Population samplePopulation() {

        return new Population(this.getReproductionChain().execute(this, this.getPopulation()));
        //return new Population(this.getPopulation().getIndividualsCopy());

    }
    
    public List<Individual> getBufferPopulation() {
        return this.buffer_population;
    }
}
