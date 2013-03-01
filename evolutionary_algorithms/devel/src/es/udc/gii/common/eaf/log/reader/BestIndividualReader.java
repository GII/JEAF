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


package es.udc.gii.common.eaf.log.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class BestIndividualReader {
    
    public static List<Float> getFloatChromosome(String path) 
        throws FileNotFoundException {
        
        String bi = null, tmp = "";
        List<Float> result = new LinkedList<Float>();
        
        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(new FileInputStream(path)));
        
        //Leo hasta el final del fichero, ya que el que me interesa es el último
        //individuo
        try {
            while (tmp != null) {
                bi = tmp;
                tmp = buffer.readLine();
            }
        }
        catch (IOException ioe) { }

        //Formato de la cadena:
        //numero - (numero, numero..., numero) - numero        
        bi = bi.replace('(', '#');
        bi = bi.replace(')', '#');
        
        String chromosome = bi.split("#")[1];
        
        String genes [] = chromosome.split(",");
        
        for (int i = 0; i < genes.length; i++) {
            
            result.add(new Float(genes[i].trim()));
        }
        
        return result;
    }
    
}
