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


package es.udc.gii.common.eaf.problem;

import es.udc.gii.common.eaf.config.Configurable;
import es.udc.gii.common.eaf.exception.ConfigurationException;
import es.udc.gii.common.eaf.problem.constraint.Constraint;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * This class represents a problem to resolve.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class Problem implements Configurable {

    /**
     * List of the objective functions of the problem.
     */
    private List<ObjectiveFunction> objectiveFunctions;

    /**
     * List of the constraints of the problem.
     */
    private List<Constraint> constraints;

    /**
     *
     */
    private boolean checkBounds = false;

    /**
     * Creates an instance of the class Problem without objectives and constraints.
     */
    public Problem() {
    }

    /**
     * Returns the value of the variable #chekcBounds.
     * @return the value of the variable checkBounds.
     */
    public boolean isCheckBounds() {
        return checkBounds;
    }

    /**
     * Sets the value of the variable checkBounds.
     * @param checkBounds new value for the variable checkBounds.
     */
    public void setCheckBounds(boolean checkBounds) {
        this.checkBounds = checkBounds;
    }

    /**
     * Sets a new list of objective functions.
     * @param objectiveFunctions a new list of objective functions.
     */
    public void setObjectiveFunctions(
            List<ObjectiveFunction> objectiveFunctions) {

        this.objectiveFunctions = objectiveFunctions;

    }

    /**
     * Returns the current list of objective functions.
     * @return the current list of objective functions.
     */
    public List<ObjectiveFunction> getObjectiveFunctions() {

        return this.objectiveFunctions;

    }

    /**
     * Sets a new list of constraints.
     * @param constraints a new list of constraints.
     */
    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    /**
     * Returns the current list of constraints.
     * @return the current list of constraints.
     */
    public List<Constraint> getConstraints() {
        return this.constraints;
    }

    /**
     * Resets the value of all the objective functions-
     */
    public void resetObjectiveFunctions() {

        for (int i = 0; i < this.objectiveFunctions.size(); i++) {
            this.objectiveFunctions.get(i).reset();
        }

    }

    /**
     * Configures the current problem.
     * @param conf a Configuration object with the configuration of this problem.
     */
    @Override
    public void configure(Configuration conf) {
        this.checkBounds = conf.containsKey("CheckBounds");
        setObjectiveFunctions(createObjectiveFunctions(conf));
        setConstraints(createConstraints(conf));        
    }

    /**
     * Creates the list of objective functions of this problem from the Configuration object.
     * @param conf a Configuration object with the configuration of this problem.
     * @return the list of objective functions.
     */
    private List<ObjectiveFunction> createObjectiveFunctions(Configuration conf) {

        List<ObjectiveFunction> listObj = new ArrayList<ObjectiveFunction>();
        List objs = conf.getList("ObjectiveFunction.Class");

        for (int i = 0; i < objs.size(); i++) {
            try {
                ObjectiveFunction obj = (ObjectiveFunction) Class.forName(
                        (String) objs.get(i)).newInstance();
                obj.configure(conf.subset("ObjectiveFunction(" + i + ")"));
                listObj.add(obj);
            } catch (Exception ex) {
                throw new ConfigurationException(
                        "Wrong objective function configuration for " + 
                        (String) objs.get(i) + " ?" + " \n Thrown exception: \n" + ex);
            }
        }

        return listObj;
    }

    /**
     * Creates the list of constraint functions of this problem from the Configuration object.
     * @param conf a Configuration object with the configuration of this problem.
     * @return the list of constraint functions.
     */
    private List<Constraint> createConstraints(Configuration conf) {
        
        List<Constraint> listCnsts = new ArrayList<Constraint>();
        List cnsts = conf.getList("Constraint.Class");

        for (int i = 0; i < cnsts.size(); i++) {
            try {
                Constraint cnstr = (Constraint) Class.forName(
                        (String) cnsts.get(i)).newInstance();
                cnstr.configure(conf.subset("Constraint(" + i + ")"));
                listCnsts.add(cnstr);
            } catch (Exception ex) {
                throw new ConfigurationException(
                        "Wrong constraint configuration for " + (String) cnsts.get(i) + " ?" + " \n Thrown exception: \n" + ex);
            }
        }

        return listCnsts;
    }
}

