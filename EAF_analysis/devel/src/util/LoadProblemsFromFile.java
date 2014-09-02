/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import analysis.MultipleTestAnalysis;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.problem.Problem;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author pilar
 */
public class LoadProblemsFromFile {

    public static List<Problem> readProblems(String file) {

        List<Problem> problems = null;
        Problem problem;
        List<ObjectiveFunction> pObjectives;
        List<Constraint> pConstraints;
        XMLConfiguration config;
        SubnodeConfiguration subNode;
        List<String> lObjective, lConstraint;
        int i = 0;
        try {

            config = new XMLConfiguration(file);
            problems = new ArrayList<Problem>();
            while (true) {

                try {

                    subNode = config.configurationAt("Problem(" + i + ")");

                    //Primero creamos los objetivos:
                    lObjective = (List<String>) subNode.getList("Objective.Class");
                    pObjectives = new ArrayList<ObjectiveFunction>();
                    for (int j = 0; j < lObjective.size(); j++) {

                        pObjectives.add((ObjectiveFunction) Class.forName(lObjective.get(j)).newInstance());
                        pObjectives.get(j).configure(subNode.subset("Objective(" + j + ")"));

                    }

                    //En segundo lugar creamos las retricciones:
                    lConstraint = (List<String>) subNode.getList("Constraint.Class");
                    pConstraints = new ArrayList<Constraint>();
                    for (int j = 0; j < lConstraint.size(); j++) {
                        pConstraints.add((Constraint) Class.forName(lConstraint.get(j)).newInstance());
                        pConstraints.get(j).configure(subNode.subset("Constraint(" + j + ")"));
                    }

                    //Se crea el problema:
                    problem = new Problem();
                    problem.configure(subNode);
                    problem.setObjectiveFunctions(pObjectives);
                    problem.setConstraints(pConstraints);
                    problems.add(problem);

                    i++;
                } catch (IllegalArgumentException ex) {
                    break;
                }

            }

        } catch (org.apache.commons.configuration.ConfigurationException ex) {
            Logger.getLogger(LoadProblemsFromFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfigurationException ex) {
            Logger.getLogger(MultipleTestAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MultipleTestAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MultipleTestAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MultipleTestAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }

        return problems;
    }
}
