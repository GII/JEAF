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
package es.udc.gii.common.eaf.algorithm.csea.evaluate;

import es.udc.gii.common.eaf.algorithm.csea.population.EvaluatedPopulation;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import java.util.List;

/**
 *
 * @author pilar
 */
public abstract class EvaluatingEntity {
    
    private Individual target;
    
    private Individual current;
    
    private TargetSelectionStrategy targetSelectionStrategy;
    
    public EvaluatingEntity(TargetSelectionStrategy tss) {
        this.targetSelectionStrategy = tss;
    }
    
    public void evaluate() throws InterruptedException {
        
        //Primero se evalua el individuo según la aplicación concreta:
        this.evaluateCurrentIndividual();
        //Una vez evaluado se envia al buffer representado en la evaluatedPopulation:
        EvaluatedPopulation.getInstance().addEvaluatedIndividual(this.current);
    
    }
    
    /**
     * Este método es dependiente de la aplicación concreta. Por eso es abstracto
     * se debe implementar para cada caso concreto.
     */
    protected abstract void evaluateCurrentIndividual();
    
    public boolean modifyIndividual(ApplicationRule rule) {
        return rule.modifyIndividual(this);
    }
    
    public Individual getTargetBySelectionStrategy(List<Individual> targetPopulation) throws InterruptedException {

        Individual target = this.targetSelectionStrategy.selectTarget(targetPopulation, current);
        
        return target;
    }

    public Individual getTarget() {
        return target;
    }

    public void setTarget(Individual target) {
        this.target = target;
    }

    public Individual getCurrent() {
        return current;
    }

    public void setCurrent(Individual current) {
        this.current = current;
    }
    
    
    
}
