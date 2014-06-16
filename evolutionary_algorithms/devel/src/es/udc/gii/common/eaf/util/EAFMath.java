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
package es.udc.gii.common.eaf.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a
 * href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EAFMath {

    public static double distance(double[] pI, double[] pJ) {
        double[] auxArray = new double[pI.length];

        for (int i = 0; i < pI.length; i++) {
            auxArray[i] = pI[i] - pJ[i];
        }
        double sumSq = StatUtils.sumSq(auxArray);

        return (sumSq == 0.0 ? 0.0 : Math.sqrt(sumSq));
    }
    
    public static double perpendicularDistance(double[] pI, double[] pJ) {
    
        
        double pJmodule = (StatUtils.sumSq(pJ) != 0.0 ? 
                Math.sqrt(StatUtils.sumSq(pJ)) : 0.0);
        
        
        return (pJmodule != 0.0 ? innerProduct(pI, pJ)/pJmodule : 0.0);
        
    }

    public static double innerProduct(double[] pI, double[] pJ) {

        double innerProduct = 0.0;

        for (int i = 0; i < pI.length; i++) {

            innerProduct += pI[i] * pJ[i];

        }

        return innerProduct;

    }

    public static double distance(List<Double> pI, List<Double> pJ) {
        double[] auxArray = new double[pI.size()];

        for (int i = 0; i < pI.size(); i++) {
            auxArray[i] = pI.get(i) - pJ.get(i);
        }

        double sumSq = StatUtils.sumSq(auxArray);

        return (sumSq == 0.0 ? 0.0 : Math.sqrt(sumSq));
    }
    
       public static double perpendicularDistance(List<Double> pI, List<Double> pJ) {
    
        double[] pJarray = new double[pJ.size()];
        
        for (int i = 0; i < pJ.size(); i++) {
            pJarray[i] = pJ.get(i);
        }
           
        double pJmodule = (StatUtils.sumSq(pJarray) != 0.0 ? 
                Math.sqrt(StatUtils.sumSq(pJarray)) : 0.0);
        
       
        
        return (pJmodule != 0.0 ? innerProduct(pI, pJ)/pJmodule : 0.0);
        
    }

    public static double innerProduct(List<Double> pI, List<Double> pJ) {

        double innerProduct = 0.0;

        for (int i = 0; i < pI.size(); i++) {

            innerProduct += pI.get(i) * pJ.get(i);

        }

        return innerProduct;

    }

    public static double[] difference(double[] pI, double[] pJ) {

        double[] difference = new double[pI.length];

        for (int i = 0; i < pI.length; i++) {

            difference[i] = pI[i] - pJ[i];

        }

        return difference;

    }

    public static List<Double> difference(List<Double> pI, List<Double> pJ) {

        List<Double> difference = new ArrayList<Double>();

        for (int i = 0; i < pI.size(); i++) {

            difference.add(pI.get(i) - pJ.get(i));

        }

        return difference;

    }

    public static double[] product(double[] pI, double c) {

        double[] product = new double[pI.length];

        for (int i = 0; i < pI.length; i++) {

            product[i] = pI[i] * c;

        }

        return product;

    }

    public static List<Double> product(List<Double> pI, double c) {

        List<Double> product = new ArrayList<Double>();

        for (int i = 0; i < pI.size(); i++) {

            product.add(pI.get(i) * c);

        }

        return product;

    }

    public static double norm(double[] pI) {

        double norm = 0.0;


        for (int i = 0; i < pI.length; i++) {

            norm += pI[i] * pI[i];

        }

        return Math.sqrt(norm);

    }

    public static double norm(List<Double> pI) {

        double norm = 0.0;


        for (int i = 0; i < pI.size(); i++) {

            norm += pI.get(i) * pI.get(i);

        }

        return Math.sqrt(norm);

    }
}
