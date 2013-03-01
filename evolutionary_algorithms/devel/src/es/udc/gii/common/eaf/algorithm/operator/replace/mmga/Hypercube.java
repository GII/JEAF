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
 * Hypercube.java
 *
 * Created on November 13, 2007, 2:34 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.operator.replace.mmga;

import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a hypercube with individuals inside. Each individual
 * is stored in a single cell.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class Hypercube {

    /**
     * Number of dimensions of the hypercube.
     */
    private int numberOfDimensions;
    /**
     * Number of divisions into which we divide each dimension.
     */
    private int numberOfDivisionsPerDimension;

    /**
     * This class represents a cell of a hypercube.
     */
    public class Cell {

        /** Individuals in this cell. */
        private List<Individual> individualsInCell;

        /**
         * Adds an individual to a cell. Only the hypercube should
         * add individuals to cells since it checks that an individual really
         * belongs to a cell. So this method has private visibility and
         * is only accesible by the classes Cell and Hypercube.
         */
        private void addIndividual(Individual individual) {
            individualsInCell.add(individual);
        }

        /** Returns a list of all the individuals of this cell. */
        public List<Individual> getIndividuals() {
            return individualsInCell;
        }

        /** Returns the number of individuals that populate this cell. */
        public int getIndividualsCount() {
            return individualsInCell.size();
        }

        /** Removes an individual from this cell. */
        public void removeIndividual(Individual individual) {
            /* Remove individual from cell. */
            individualsInCell.remove(individual);

            /* Remove from mapping. */
            individualCellMapping.remove(individual);

            /* Remove from hypercube. */
            individuals.remove(individual);

            dirty = true;
        }

        /** Returns an individual with index 'index'. */
        public Individual getIndividual(int index) {
            return individualsInCell.get(index);
        }

        /** Returns a random individual from this cell. */
        public Individual getRandomIndividual() {
            return individualsInCell.get(EAFRandom.nextInt(individualsInCell.size()));
        }

        /** Class cosnstructor. */
        public Cell() {
            individualsInCell = new ArrayList<Individual>();
        }
    }
    /** 
     * Size of a division in each dimension. 
     */
    private double[] divisionSize;
    /** 
     * Represents the origin of the hypercube. 
     */
    private double[] origin;
    /** 
     * Maps cell numbers to cells. It stores the cells that are populated. 
     */
    private Map<Long, Cell> crowdedCells;
    /**
     * Maps an individual to the cell where it belongs. 
     */
    private Map<Individual, Cell> individualCellMapping;
    /** 
     * Contains all the individuals in the hypercube. 
     */
    private List<Individual> individuals;
    /** 
     * If the hypercube is modified without updating it, dirty is set to true.
     * Updating the hypercube sets dirty to false. Any method which needs a 
     * clean hypercube (i.e. those which use the bounds and origin) performs
     * an update automatically if dirty is true before it executes.
     */
    private boolean dirty;

    /**
     * Class constructor.
     *
     * @param individuals List of individuals that will be inside the
     * hypercube.
     * 
     * @param numberOfDimensions The number of objectives of the problem.
     *
     * @param numberOfDivisionsPerDimension Number of parts in wich each
     * dimension will be divided for constructing cell. Each division on
     * a dimension represents the coordinate of a cell on that dimension.
     *
     */
    public Hypercube(List<Individual> individuals, int numberOfDimensions, int numberOfDivisionsPerDimension) {

        this.numberOfDimensions = numberOfDimensions;
        this.divisionSize = new double[numberOfDimensions];
        this.origin = new double[numberOfDimensions];
        this.numberOfDivisionsPerDimension = numberOfDivisionsPerDimension;

        this.individualCellMapping = new HashMap<Individual, Cell>(individuals.size());

        this.crowdedCells = new HashMap<Long, Cell>();

        this.individuals = new ArrayList<Individual>();

        dirty = true;
    }

    /**
     * Returns the objective value of an individual on a dimension.
     */
    private double getObjectiveValue(Individual individual, int dimension) {
        if (dimension < individual.getObjectives().size()) {
            return individual.getObjectives().get(dimension).doubleValue();
        } else { /* Single objective, dimension ignored. */
            return individual.getFitness();
        }
    }

    /**
     * Initializes the hypercube.
     */
    private void initializeHypercube() {
        crowdedCells.clear(); /* Clear the list of cells. */
        individualCellMapping.clear(); /* Clear the mapping Individual<->Cell. */
    }

    /**
     * Returns the cells that have at least one individual.
     */
    public List<Cell> getCrowdedCells() {

        /* If a write operation has been performed without updating the hypercube */
        if (isDirty()) {
            update(); /* Update the hypercube now, please. */
        }

        List<Cell> cells = new ArrayList<Cell>(crowdedCells.values().size());
        cells.addAll(crowdedCells.values());
        return cells;
    }

    /**
     * Returns one cell among those which are most populated.
     */
    public Cell getMostCrowdedCell() {
        List<Cell> cells = getCrowdedCells();

        if (cells.size() == 0) {
            return null;
        } else {
            Cell mostPopulated = cells.get(0);

            for (int i = 1; i < cells.size(); i++) {
                Cell newCell = cells.get(i);

                if (mostPopulated.getIndividualsCount()
                        < newCell.getIndividualsCount()) {

                    mostPopulated = newCell;
                }
            }

            return mostPopulated;
        }
    }

    /**
     * Returns the cell number of the cell to wich a individual belongs. If
     * The individual belongs to no cell (i.e. the individual is out of the
     * bounds of the current hypercube) this method returns null.
     */
    private Long getCellNumber(Individual individual) {
        long coordinate = -1;
        long cellNumber = 0;
        int dimensionCount = numberOfDimensions - 1;

        /* For each dimension (objective). */
        for (int dimension = 0; dimension < numberOfDimensions;
                dimension++, dimensionCount--) {

            /* Get the objective value in this dimension. */
            double objValue = getObjectiveValue(individual, dimension);

            /* Calculate the coordinate in this dimension. */
            coordinate =
                    (long) Math.floor((objValue - origin[dimension])
                    / divisionSize[dimension]);

            /* Check bounds. */
            if ((coordinate > numberOfDivisionsPerDimension)
                    || (coordinate < 0)) {
                return null; /* Out of bounds. */
            } else {
                /* Calculate offset. */
                cellNumber +=
                        (long) Math.floor(coordinate
                        * Math.pow(numberOfDivisionsPerDimension, dimensionCount));
            }
        }

        /* Return the cell number found. */
        return new Long(cellNumber);
    }

    /**
     * Finds the cell of an individual.
     *
     * @return Returns the cell to wich the individual belongs or <code>null</code>
     *         if the individual is out of the current hypercube's bounds.
     */
    public Cell findCell(Individual individual) {

        /* If a write operation has been performed without updating the hypercube */
        if (isDirty()) {
            update(); /* Update the hypercube, now! */
        }

        /* Try to get the cell from mapping. */
        Cell cell = individualCellMapping.get(individual);

        /* If there is no mapping yet. */
        if (cell == null) {

            /* Get the cell number of the cell to wich this individual
             * belongs. */
            Long cellNumber = getCellNumber(individual);

            if (cellNumber == null) { /* If the individual is out of range */
                return null; /* Notify it. */
            } else { /* The individual belongs to a cell with number 'cellNumber'. */

                /* Look if we already have such a cell. */
                cell = crowdedCells.get(cellNumber);

                if (cell == null) { /* If we do not */
                    cell = new Cell(); /* Create a new cell. */
                    crowdedCells.put(cellNumber, cell); /* Remember that cell. */
                    individualCellMapping.put(individual, cell); /* Remember mapping. */
                }
            }
        }

        /* Return the cell found or null. */
        return cell;
    }

    /**
     * Updates the hypercube. The bounds and origin are recalculated.
     */
    public void update() {
        /* Initialize the hypercube. */
        initializeHypercube();

        /* Stores the minimum objective value on each dimension. */
        double[] minObjectiveValueOnDimension =
                new double[numberOfDimensions];

        /* Stores the maximum objective value on each dimension. */
        double[] maxObjectiveValueOnDimension =
                new double[numberOfDimensions];

        /* Initialize the bounds of the hypercube. */
        for (int dimension = 0; dimension < numberOfDimensions; dimension++) {
            minObjectiveValueOnDimension[dimension] = Double.MAX_VALUE;
            maxObjectiveValueOnDimension[dimension] = Double.MIN_VALUE;
        }

        /* Find the bounds for each dimension. */
        for (int dimension = 0; dimension < numberOfDimensions; dimension++) {
            for (int i = 0; i < individuals.size(); i++) {
                Individual individual = individuals.get(i);
                double objectiveValue =
                        getObjectiveValue(individual, dimension);

                if (minObjectiveValueOnDimension[dimension] > objectiveValue) {
                    minObjectiveValueOnDimension[dimension] = objectiveValue;
                }

                if (maxObjectiveValueOnDimension[dimension] < objectiveValue) {
                    maxObjectiveValueOnDimension[dimension] = objectiveValue;
                }
            }

            /* Calculate the size of a division in the current dimension. */
            divisionSize[dimension] =
                    (maxObjectiveValueOnDimension[dimension]
                    - minObjectiveValueOnDimension[dimension])
                    / (numberOfDivisionsPerDimension - 1);

            /* Evaluate the origin for this dimension. */
            origin[dimension] =
                    minObjectiveValueOnDimension[dimension] -
                    (divisionSize[dimension] / 2);
        }

        /* Hypercube is up to date (bounds and origin). */
        dirty = false;

        /* Link individuals with cells, so we can easily know which individual
         * belongs to which cell, and vice versa. */
        for (int i = 0; i < individuals.size(); i++) {
            Individual individual = individuals.get(i);
            Cell cell = findCell(individual);

            cell.addIndividual(individual);

            individualCellMapping.put(individual, cell);
        }
    }

    /**
     * Set a new list of individuals. The hypercube is not updated.
     */
    public void setIndividuals(List<Individual> newIndividuals) {
        individuals = newIndividuals;
        dirty = true;
    }

    /**
     * Adds a new individual to the hypercube. The hypercube is not updated.
     */
    public void addIndividual(Individual individual) {
        individuals.add(individual);

        dirty = true;
    }

    /**
     * Adds individuals to the hypercube. The hypercube is not updated.
     */
    public void addAllIndividuals(List<Individual> toAdd) {
        individuals.addAll(toAdd);
        dirty = true;
    }

    /**
     * Removes an individual from hypercube. The hypercube is not updated.
     */
    public void removeIndividual(Individual individual) {
        individuals.remove(individual);

        dirty = true;
    }

    /**
     * Removes individuals from hypercube. The hypercube is not updated.
     */
    public void removeAllIndividuals(List<Individual> toRemove) {

        for (int i = 0; i < toRemove.size(); i++) {
            individuals.remove(toRemove.get(i));
        }

        dirty = true;
    }

    /**
     * Returns the number of individuals in the hypercube.
     */
    public int getSize() {
        return individuals.size();
    }

    /**
     * Returns <code>true</code> if the hypercube has been modified without
     * updating it.
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Returns all individuals from the hypercube.
     */
    public List<Individual> getIndividuals() {
        return individuals;
    }
}
