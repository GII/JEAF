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
 * BinaryAcceptancePolicy.java
 *
 * Created on March 20, 2008, 12:44 PM
 *
 */

package es.udc.gii.common.eaf.algorithm.parallel.migration.acceptance;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.topology.migration.MigrationObject;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.config.EAFConfiguration;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * With this class complex acceptance policies can be created as binary trees.
 * The left and right components must not be null. A runtime exception will be
 * thrown if setting either of them to null or when calling the {@link #accept}
 * method with some of them set to null.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class BinaryAcceptancePolicy implements MigAcceptancePolicy {
    
    /**
     * Left component of this node.
     */
    private MigAcceptancePolicy leftComponent = null;
    
    /**
     * Right component of this node.
     */
    private MigAcceptancePolicy rightComponent = null;
    
    /**
     * Creates a new instance of BinaryAcceptancePolicy
     */
    public BinaryAcceptancePolicy() {
    }
    
    /**
     * Creates a new instance of BinaryAcceptancePolicy
     */
    public BinaryAcceptancePolicy(MigAcceptancePolicy leftComponent, MigAcceptancePolicy rightComponent) {
        setLeftComponent(leftComponent);
        setRightComponent(rightComponent);
    }
    
    /**
     * Accepts the individuals that come in a {@link MigrationObject} based on
     * some criterion.<p>
     *
     * This method calls first the same method of its left component, then the
     * same method of its right component and finally it relies on the protected
     * {@link #binaryOperation} method for merging the two results accordingly.
     *
     * @return A list of the accepted individuals. If no individual is accepted,
     * then an empty list is returned.
     */
    @Override
    public List<Individual> accept(MigrationObject migrant,
        EvolutionaryAlgorithm algorithm, List<Individual> currentPopulation) {
        
        if((getLeftComponent() == null) || (getRightComponent() == null)) {
            throw new RuntimeException("BinaryAcceptancePolicy (accept): Either" +
                "the left of the right component is null.");
        }
        
        List<Individual> acceptedFromLeft =
            getLeftComponent().accept(migrant, algorithm, currentPopulation);
        
        List<Individual> acceptedFromRight =
            getRightComponent().accept(migrant, algorithm, currentPopulation);
        
        return binaryOperation(acceptedFromLeft, acceptedFromRight);
    }
    
    /**
     * The concrete binary operation that is performed with the results of the
     * left and right components.
     */
    protected abstract List<Individual> binaryOperation(List<Individual> acceptedFromLeft,
        List<Individual> acceptedFromRight);
    
    /**
     * Configures this object.<p>
     *
     * Configuration example:
     *
     * <pre>
     * &lt;AcceptancePolicy&gt;
     *     &lt;Class&gt;subclass of BinaryAcceptancePolicy&lt;/Class&gt;
     *
     *     &lt;AcceptancePolicy&gt;
     *         &lt;Class&gt;...GenerationBasedAcceptance&lt;/Class&gt;
     *         &lt;AcceptIndividualsFrom&gt;ANY_GENERATION&lt;/AcceptIndividualsFrom&gt;
     *     &lt;/AcceptancePolicy&gt;
     *
     *     &lt;AcceptancePolicy&gt;
     *         &lt;Class&gt;subclass of BinaryAcceptancePolicy&lt;/Class&gt;
     *
     *         &lt;AcceptancePolicy&gt;
     *             &lt;Class&gt;...GenerationBasedAcceptance&lt;/Class&gt;
     *         &lt;AcceptIndividualsFrom&gt;SOME_GENERATION&lt;/AcceptIndividualsFrom&gt;
     *         &lt;/AcceptancePolicy&gt;
     *
     *         &lt;AcceptancePolicy&gt;
     *             &lt;Class&gt;some MigAcceptancePolicy&lt;/Class&gt;
     *             ...
     *         &lt;/AcceptancePolicy&gt;
     *     &lt;/AcceptancePolicy&gt;
     * &lt;/AcceptancePolicy&gt;
     * </pre><p>
     *
     * This constructs the following tree:
     *
     * <pre>
     *               (B1)
     *              /    \
     *            (G1)  (B2)
     *                 /    \
     *               (G2)   (M)
     * </pre><p>
     *
     * B1 is the constructed BinaryAcceptancePolicy. Its left component is a
     * {@link GenerationBasedAcceptance} (G1) with parameter ANY_GENERATION. Its right
     * component is also a BinaryAcceptancePolicy with another
     * GenerationBasedAcceptance (G2) with parameter SOME_GENERATION as left
     * component and some other implementation of {@link MigAcceptancePolicy} as
     * right component.
     */
    @Override
    public void configure(Configuration conf) {
        
        EAFConfiguration eafconf = (EAFConfiguration)conf;
        
        MigAcceptancePolicy left =
            (MigAcceptancePolicy)eafconf.getObject("AcceptancePolicy(0).Class");
        
        MigAcceptancePolicy right =
            (MigAcceptancePolicy)eafconf.getObject("AcceptancePolicy(1).Class");
        
        if(left != null) {
            left.configure(eafconf.subset("AcceptancePolicy(0)"));
        }
        
        if(right != null) {
            right.configure(eafconf.subset("AcceptancePolicy(1)"));
        }
        
        setLeftComponent(left);
        setRightComponent(right);
    }
    
    /* ---- Getters and setters follow ---- */
    
    public MigAcceptancePolicy getLeftComponent() {
        return leftComponent;
    }
    
    /**
     * Set the left component of this node. If <code>null</code> is passed,
     * a runtime exception is thrown.
     **/
    public void setLeftComponent(MigAcceptancePolicy leftComponent) {
        if(leftComponent == null) {
            throw new RuntimeException("BinaryAcceptancePolicy " +
                "(setLeftComponent): The left component must not be null.");
        }
        this.leftComponent = leftComponent;
    }
    
    public MigAcceptancePolicy getRightComponent() {
        return rightComponent;
    }
    
    /**
     * Set the right component of this node. If <code>null</code> is passed,
     * a runtime exception is thrown.
     **/
    public void setRightComponent(MigAcceptancePolicy rightComponent) {
        if(rightComponent == null) {
            throw new RuntimeException("BinaryAcceptancePolicy " +
                "(setRightComponent): The right component must not be null.");
        }
        this.rightComponent = rightComponent;
    }
}
