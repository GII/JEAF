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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pilar
 */
public class ReferencePoint {
    
    private List<Double> point;
    
    private int deficiencyCount;
    
    private List<MONSGA2Individual> projectedPoints;

    public ReferencePoint() {
    }

    public ReferencePoint(List<Double> point, int deficiencyCount) {
        this.point = point;
        this.deficiencyCount = deficiencyCount;
    }

    public List<Double> getPoint() {
        return point;
    }

    public void setPoint(List<Double> point) {
        this.point = point;
    }

    public int getDeficiencyCount() {
        return deficiencyCount;
    }

    public void setDeficiencyCount(int deficiencyCount) {
        this.deficiencyCount = deficiencyCount;
    }
    
    public void addProjectedPoint(MONSGA2Individual projectedPoint) {
    
        if (this.projectedPoints == null) {
            this.projectedPoints = new ArrayList<MONSGA2Individual>();
        }
        this.projectedPoints.add(projectedPoint);
    }
    
    public List<MONSGA2Individual> getProjectedPoints() {
        return this.projectedPoints;
    }
    
    public int getActualCount() {
        if (this.projectedPoints == null)
            return 0;
        return this.projectedPoints.size();
    }
    
    @Override
    public Object clone() {
    
        ReferencePoint clone = new ReferencePoint();
        
        clone.deficiencyCount = this.deficiencyCount;
        clone.point = new ArrayList<Double>();
        for (Double d : this.point) {
            clone.point.add(d);
        }
                
        
        return clone;
        
    }
    
}
