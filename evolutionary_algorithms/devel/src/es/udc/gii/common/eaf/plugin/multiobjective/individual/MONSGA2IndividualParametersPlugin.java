/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.plugin.multiobjective.individual;

import es.udc.gii.common.eaf.algorithm.fitness.comparator.MinimizingASFComparator;
import es.udc.gii.common.eaf.algorithm.fitness.comparator.MinimizingObjectiveComparator;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.MONSGA2Individual;
import es.udc.gii.common.eaf.algorithm.population.multiobjective.ReferencePoint;
import es.udc.gii.common.eaf.plugin.multiobjective.asf.ASFStrategy;
import es.udc.gii.common.eaf.plugin.multiobjective.asf.MOASFStrategy;
import es.udc.gii.common.eaf.util.ConfWarning;
import es.udc.gii.common.eaf.util.EAFMath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.util.MathUtils;

/**
 *
 * @author pilar
 */
public class MONSGA2IndividualParametersPlugin implements MultiobjectiveIndividualParametersPlugin<MONSGA2Individual> {

    private ASFStrategy asf;
    private int p = 1;

    @Override
    public void configure(Configuration conf) {
        try {
            if (conf.containsKey("ASFStrategy.Class")) {
                this.asf = ((ASFStrategy) Class.forName(conf.getString("ASFStrategy.Class")).newInstance());
                this.asf.configure(conf);
            } else {
                this.asf = new MOASFStrategy();
                (new ConfWarning("ASFStrategy.Class", asf.getClass().getCanonicalName())).warn();
            }

            if (conf.containsKey("P")) {
                this.p = conf.getInt("P");
            } else {
                (new ConfWarning("P", asf.getClass().getCanonicalName())).warn();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void calculateParameters(List<MONSGA2Individual> individuals) {
        //LOs translated objectives también se calculan aquí porque hacen falta 
        //para ASF:
        MinimizingObjectiveComparator comparator = new MinimizingObjectiveComparator();
        int M = individuals.get(0).getObjectives().size();
        double[] idealPoint = new double[M];
        double[] intercept = new double[M];
        
//        for (Individual ind : individuals) {
//            System.out.println(ind.getObjectives().get(0) + "\t" + 
//                    ind.getObjectives().get(1));
//        }
//        
//        System.out.println();

        for (int i = 0; i < M; i++) {

            comparator.setObjectiveIndex(i);
            Collections.sort(individuals, comparator);
            idealPoint[i] = individuals.get(0).getObjectives().get(i);
            

        }
        
//        System.out.println(idealPoint[0] + "\t" + idealPoint[1]);
//        System.out.println();
        
        for (MONSGA2Individual ind : individuals) {
            ind.translateObjectives(idealPoint);
//            System.out.println(ind.getTranslatedObjectives().get(0) + "\t" + 
//                    ind.getTranslatedObjectives().get(1));
        }
//        System.out.println();

        List<List<Double>> extremePoints = new ArrayList<List<Double>>();
        MinimizingASFComparator asfComparator = new MinimizingASFComparator();

        this.asf.calculate(individuals);
        

        Collections.sort(individuals, asfComparator);
        MONSGA2Individual asfInd = individuals.get(0);
        List<Double> mExtreme;
        List<Double> w;
        
        //3.- Calculate the extreme point of each objective:
        for (int i = 0; i < M; i++) {
            mExtreme = new ArrayList<Double>(Collections.nCopies(M, 0.0));
            w = new ArrayList<Double>(Collections.nCopies(M, 10e-6));
            w.set(i, 1.0);
            mExtreme = computeASF(individuals, w).getTranslatedObjectives();
//            mExtreme.set(i, asfInd.getTranslatedObjectives().get(i));
            extremePoints.add(mExtreme);
//            System.out.println(extremePoints.get(i).get(0) + "\t" +
//                    extremePoints.get(i).get(1));
        }
//        System.out.println();


        for (MONSGA2Individual ind : individuals) {
            ind.projectObjectives(extremePoints);
//            System.out.println(ind.getProjectedObjectives().get(0) + "\t" + 
//                    ind.getProjectedObjectives().get(1));
        }
//        System.out.println();

        //Calculate the number of reference points:
        int H = (int) MathUtils.binomialCoefficient(M + p - 1, p);

        //Create the list of reference points:
        List<ReferencePoint> referencePoints = new ArrayList<ReferencePoint>();
        //At least the extremes of the hyperplane:
        for (int i = 0; i < extremePoints.size(); i++) {
            referencePoints.add(new ReferencePoint(extremePoints.get(i), 0));
        }
        for (int i = 0; i < H - extremePoints.size(); i++) {
            //Interpolate between the extreme points according to the number of divisions
            referencePoints.add(new ReferencePoint(interpolateReferencePoint(
                    referencePoints.get(0).getPoint(),
                    referencePoints.get(1).getPoint(), (double) (H - i - M) / (H - 1), 0, 1), 0));

        }

        //Assign to each element the closest reference point:
        for (MONSGA2Individual ind : individuals) {
            ind.setReferencePoint(getClosestReference(ind, referencePoints));
            ind.setDistanceToReferencePoint(EAFMath.perpendicularDistance(
                    ind.getTranslatedObjectives(), ind.getReferencePoint().getPoint()));
            if (ind.getReferencePoint() != null)
                ind.getReferencePoint().addProjectedPoint(ind);
        }
        
        for (ReferencePoint r : referencePoints) {
            r.setDeficiencyCount(individuals.size()/H - r.getActualCount());
//            System.out.println(r.getPoint().get(0) + "\t" +
//                    r.getPoint().get(1) + "\t" + 
//                    r.getDeficiencyCount());
        }



    }

    private ReferencePoint getClosestReference(MONSGA2Individual individual,
            List<ReferencePoint> referencePoint) {

        ReferencePoint closest = null;
        double distance = Double.MAX_VALUE;
        double auxDistance;

        for (ReferencePoint r : referencePoint) {

            auxDistance = EAFMath.perpendicularDistance(individual.getTranslatedObjectives(), r.getPoint());
            if (auxDistance < distance) {
                closest = r;
                distance = auxDistance;
            }

        }


        return closest;

    }

    private List<Double> interpolateReferencePoint(List<Double> e1, List<Double> e2, double pos, int o1, int o2) {

        List<Double> interpolation = new ArrayList<Double>();

        double x, y;

        interpolation.addAll(e1);

        x = (e2.get(o1) + e1.get(o1)) * pos;
        y = (e2.get(o2) + e1.get(o2)) * (1.0 - pos);

        interpolation.set(o1, x);
        interpolation.set(o2, y);


        return interpolation;

    }

    private MONSGA2Individual computeASF(List<MONSGA2Individual> individuals, List<Double> w) {
        
        MONSGA2Individual asfIndividual = null;
        double ASFMin = Double.MAX_VALUE;
        double indMax = -Double.MAX_VALUE;
        double auxValue;
        int i;
        
        for (MONSGA2Individual ind : individuals) {
        
            indMax = -Double.MAX_VALUE;
            auxValue = 0;
            i = 0;
            for (Double d : ind.getTranslatedObjectives()) {
                auxValue = d/w.get(i++);
                if (auxValue > indMax) {
                    indMax = auxValue;
                }
            }
            
            if (ASFMin > indMax) {
                ASFMin = indMax;
                asfIndividual = (MONSGA2Individual) ind.clone();
            }
            
        }
        
        return asfIndividual;
        
    }
}
