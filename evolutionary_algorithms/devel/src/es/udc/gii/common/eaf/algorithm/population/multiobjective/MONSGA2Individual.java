/*
 * Copyright (C) 2013 pilar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.common.eaf.algorithm.population.multiobjective;

import es.udc.gii.common.eaf.util.EAFMath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math.util.DoubleArray;

/**
 *
 * @author pilar
 */
public class MONSGA2Individual extends MultiobjectiveIndividual {

    private List<Double> translatedObjectives;
    private List<Double> projectedObjectives;
    private ReferencePoint referencePoint;
    private double ASF;
    
    private double distanceToReferencePoint;

    

    public MONSGA2Individual() {
        super();
        translatedObjectives = new ArrayList<Double>();
        this.projectedObjectives = new ArrayList<Double>();
    }

    public MONSGA2Individual(DoubleArray[] chromosomes) {
        super(chromosomes);
        translatedObjectives = new ArrayList<Double>();
        this.projectedObjectives = new ArrayList<Double>();
    }

    public void translateObjectives(double[] idealPoint) {

        int i = 0;

        translatedObjectives = new ArrayList<Double>();
        for (Double d : this.getObjectives()) {
            translatedObjectives.add(d - idealPoint[i]);
            i++;
        }

    }
    
    public void translateObjective(int objective, double value) {
    
        if (translatedObjectives.isEmpty())
            translatedObjectives = new ArrayList<Double>(Collections.nCopies(
                    this.getObjectives().size(), 0.0));
        translatedObjectives.set(objective, 
                this.getObjectives().get(objective).doubleValue() - value);
    
    }

    public List<Double> getTranslatedObjectives() {
        return translatedObjectives;
    }

    public void setTranslatedObjectives(List<Double> translatedObjectives) {
        this.translatedObjectives = translatedObjectives;
    }

    public List<Double> getProjectedObjectives() {
        return projectedObjectives;
    }

    public void setProjectedObjectives(List<Double> projectedObjectives) {
        this.projectedObjectives = projectedObjectives;
    }

    public ReferencePoint getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(ReferencePoint referencePoint) {
        this.referencePoint = referencePoint;
    }

    public double getASF() {
        return ASF;
    }

    public void setASF(double ASF) {
        this.ASF = ASF;
    }
    
    public double getDistanceToReferencePoint() {
        return distanceToReferencePoint;
    }

    public void setDistanceToReferencePoint(double distanceToReferencePoint) {
        this.distanceToReferencePoint = distanceToReferencePoint;
    }
    

    public void projectObjectives(List<List<Double>> extremePoints) {

        this.projectedObjectives.clear();

        List<Double> r = new ArrayList<Double>();
        List<Double> hp;

        r.addAll(this.translatedObjectives);
        hp = EAFMath.difference(extremePoints.get(1), extremePoints.get(0));

        double x1, y1, x2, y2, x3, y3, x4, y4;

        x1 = this.getTranslatedObjectives().get(0);
        y1 = this.getTranslatedObjectives().get(1);

        x2 = 0.0;
        y2 = 0.0;

        x3 = extremePoints.get(0).get(0);
        y3 = extremePoints.get(0).get(1);

        x4 = extremePoints.get(1).get(0);
        y4 = extremePoints.get(1).get(1);

        double x, y, div;

        div = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        
        if (div != 0.0) {
            x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / div;
            y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / div;
        } else {
            x = 0.0;
            y = 0.0;
        }

        this.projectedObjectives.add(x);
        this.projectedObjectives.add(y);
//
//
//        System.out.println(
//                this.getObjectives().get(0)
//                + "\t" + this.getObjectives().get(1)
//                + "\t" + this.translatedObjectives.get(0)
//                + "\t" + this.translatedObjectives.get(1)
//                + "\t" + this.projectedObjectives.get(0)
//                + "\t" + this.projectedObjectives.get(1));

    }

    /**
     * Clones this instance.
     *
     * @return A new instance of this class containing the same state as this
     * instance.
     */
    @Override
    public Object clone() {
        /* Clone the state. */
        MONSGA2Individual clone = (MONSGA2Individual) super.clone();

        //Each parameter of the MOSGA2Individual should be cloned:
        clone.ASF = this.ASF;
        clone.distanceToReferencePoint = this.distanceToReferencePoint;
        if (this.referencePoint != null) {
            clone.referencePoint = (ReferencePoint) this.referencePoint.clone();
        } else {
            clone.referencePoint = null;
        }
        List<Double> cloneTranslatedObjectives = new ArrayList<Double>();
        for (Double d : this.translatedObjectives) {
            cloneTranslatedObjectives.add(d);
        }
        clone.setTranslatedObjectives(cloneTranslatedObjectives);

        List<Double> cloneProjectedObjectives = new ArrayList<Double>();
        for (Double d : this.projectedObjectives) {
            cloneProjectedObjectives.add(d);
        }
        clone.setProjectedObjectives(cloneProjectedObjectives);


        return clone;
    }
}
