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
package es.udc.gii.common.eaf.plugin.multiobjective.remainingfront;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.NSGA2Algorithm;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.MinimizingASFComparator;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.MONSGA2Individual;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.ReferenceDistanceComparator;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.ReferencePoint;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.ReferencePointActualCountComparator;
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author pilar
 */
public class MONSGA2RemainingFrontSelection implements RemainingFrontSelectionPlugin<MONSGA2Individual> {

    @Override
    public List<MONSGA2Individual> selection(EvolutionaryAlgorithm alg, List<MONSGA2Individual> newPop,
            List<MONSGA2Individual> front, int size) {

        List<MONSGA2Individual> St = new ArrayList<MONSGA2Individual>();
        List<MONSGA2Individual> Ij = new ArrayList<MONSGA2Individual>();
        List<ReferencePoint> referencePoints;
        ReferencePointActualCountComparator refComparator = new ReferencePointActualCountComparator();
        ReferenceDistanceComparator minimumReferenceDistanceComparator = new ReferenceDistanceComparator();
        MinimizingASFComparator asfComparator = new MinimizingASFComparator();
        ReferencePoint refPoint;
        List<ReferencePoint> neighbouring;
        int r;
        int included = 0;
        int points;
        MONSGA2Individual newInd;

        //0.- Create S(t) = P(t+1) + F(l) (the last front):
        St.addAll(newPop);
        St.addAll(front);

        ((NSGA2Algorithm) alg).getParametersPlugin().calculateParameters(St);
        //1.- Get all the reference points of the front:
        referencePoints = new ArrayList<ReferencePoint>();
        for (MONSGA2Individual ind : St) {
            if (ind.getReferencePoint() != null && !referencePoints.contains(ind.getReferencePoint())) {
                referencePoints.add(ind.getReferencePoint());
            }
        }

        if (referencePoints.size() > 0) {
            while (included < size) {

                //2.- Sort the reference points by actualCount:
                Collections.sort(referencePoints, refComparator);

                //3.- Select Jmin = reference points having minimum deficiency count
                //Calculate the number of points with minimum deficienty count:
                points = 0;
                for (ReferencePoint ref : referencePoints) {
                    if (ref.getActualCount() == referencePoints.get(0).getActualCount()) {
                        points++;
                    } else {
                        break;
                    }
                }

                r = EAFRandom.nextInt(points);
                refPoint = referencePoints.get(r);

                //Create Ij = list of individuals from Fl which reference point is "r"
                Ij.clear();
                for (MONSGA2Individual ind : front) {
                    if (ind.getReferencePoint().equals(refPoint)) {
                        Ij.add(ind);
                    }
                }

                newInd = null;
                if (!Ij.isEmpty()) {

                    //El actual count no debería tener en cuanta los individuos de Fl
                    if (refPoint.getActualCount() - Ij.size() == 0) {
                        Collections.sort(Ij, minimumReferenceDistanceComparator);
                        r = 0;
                    } else if (refPoint.getActualCount() - Ij.size() > 0) {
                        r = EAFRandom.nextInt(Ij.size());
                    }
                    newInd = Ij.get(r);
                    newPop.add((MONSGA2Individual) newInd.clone());
                    included++;
                    // Remove the individual from the Fl:
                    front.remove(newInd);
//                    refPoint.getProjectedPoints().remove(newInd);
                } else {

                    referencePoints.remove(refPoint);
                }
            }
        }

        return newPop;
    }

    @Override
    public void configure(Configuration conf) {
        //Do nothing
    }
}
