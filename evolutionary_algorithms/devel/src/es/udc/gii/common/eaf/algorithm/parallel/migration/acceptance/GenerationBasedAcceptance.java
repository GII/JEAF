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
 * GenerationBasedAcceptance.java
 *
 * Created on March 20, 2008, 11:06 AM
 *
 */

package es.udc.gii.common.eaf.algorithm.parallel.migration.acceptance;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.topology.migration.MigrationObject;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * Accepts the individuals that come in a {@link MigrationObject} based on
 * the generation of the sending node.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class GenerationBasedAcceptance implements MigAcceptancePolicy {
    
    /** States that only individuals from other processes in the same generation
     * are going to be considered for inmigration.
     */
    public final static int SAME_GENERATION = 0;
    
    /** States that individuals from other processes in the any generation
     * are going to be considered for inmigration.
     */
    public final static int ANY_GENERATION = 1;
    
    /** States that only individuals from other processes in an older generation
     * or in the same generation are going to be considered for inmigration.
     */
    public final static int OLDER_GENERATION = 2;
    
    /** States that only individuals from other processes in a newer generation
     * or in the same generation are going to be considered for inmigration.
     */
    public final static int NEWER_GENERATION = 3;
    
    /** Generation from which individuals are accepted. */
    private int acceptIndividualsFrom = ANY_GENERATION;
    
    /** Individuals that have not been yet accepted but that might be accepted in
     * the future. */
    private List<MigrationObject> pendingMigrants = null;
    
    /** Creates a new instance of GenerationBasedAcceptance */
    public GenerationBasedAcceptance() {
        pendingMigrants = new ArrayList<MigrationObject>();
        setAcceptIndividualsFrom(OLDER_GENERATION);
        (new ConfWarning("GenerationBasedAcceptance.AcceptIndividualsFrom", 
                "OLDER_GENERATION")).warn();
    }
    
    /** Creates a new instance of GenerationBasedAcceptance */
    public GenerationBasedAcceptance(int acceptIndividualsFrom) {
        setAcceptIndividualsFrom(acceptIndividualsFrom);
        pendingMigrants = new ArrayList<MigrationObject>();
    }
    
    /**
     * Accepts the individuals that come in a {@link MigrationObject} based on
     * the generation of the sending node.<p>
     *
     * @return A list of the accepted individuals. If no individual is accepted,
     * then an empty list is returned.
     */
    @Override
    public List<Individual> accept(MigrationObject migrant,
        EvolutionaryAlgorithm algorithm, List<Individual> currentPopulation) {
        
        List<Individual> result = new ArrayList<Individual>();
        List<MigrationObject> completed = new ArrayList<MigrationObject>();
        
        int myGeneration = algorithm.getGenerations();
        
        /* Look if some of the pending migrants can be accepted now. */
        for (MigrationObject elem : getPendingMigrants()) {
            if(elem != null) {
                switch(getAcceptIndividualsFrom()) {
                    case SAME_GENERATION: /* Accept only data from other process
                     * with the same generation. */
                        if(elem.getGeneration() == myGeneration) {
                            completed.add(elem);
                            result.addAll(elem.getIndividuals());
                        }
                        break;
                    case OLDER_GENERATION: /* Accept only data from other process
                     * with older or same generation. */
                        if(elem.getGeneration() <= myGeneration) {
                            completed.add(elem);
                            result.addAll(elem.getIndividuals());
                        }
                        break;
                    case NEWER_GENERATION: /* Accept only data from other process
                     * with newer or same generation. */
                        if(elem.getGeneration() >= myGeneration) {
                            completed.add(elem);
                            result.addAll(elem.getIndividuals());
                        }
                        break;
                    case ANY_GENERATION: /* Don't care about generations. */
                    default:
                        completed.add(elem);
                        result.addAll(elem.getIndividuals());
                }
            }
        }
        
        /* Remove all completed migrations. */
        getPendingMigrants().removeAll(completed);
        
        /* Test if the current migrant might be accepted. */
        if(migrant != null) {
            switch(getAcceptIndividualsFrom()) {
                case SAME_GENERATION: /* Accept only data from other process
                 * with the same generation. */
                    if(migrant.getGeneration() == myGeneration) {
                        result.addAll(migrant.getIndividuals());
                    } else if(migrant.getGeneration() > myGeneration) {
                        getPendingMigrants().add(migrant);
                    }
                    break;
                case OLDER_GENERATION: /* Accept only data from other process
                 * with older or same generation. */
                    if(migrant.getGeneration() <= myGeneration) {
                        result.addAll(migrant.getIndividuals());
                    } else {
                        getPendingMigrants().add(migrant);
                    }
                    break;
                case NEWER_GENERATION: /* Accept only data from other process
                 * with newer or same generation. */
                    if(migrant.getGeneration() >= myGeneration) {
                        result.addAll(migrant.getIndividuals());
                    }
                    break;
                case ANY_GENERATION: /* zzz, don't care about generations. */
                default:
                    result.addAll(migrant.getIndividuals());
            }
        }
        
        return result;        
    }
    
    /**
     * Configures this class.<p>
     *
     * Configuration example:
     *
     * <pre>
     * ...
     * &lt;AcceptancePolicy&gt;
     *     &lt;Class&gt;...parallel.migration.GenerationBasedAcceptance&lt;/Class&gt;
     *     &lt;AcceptIndividualsFrom&gt;ANY_GENERATION&lt;/AcceptIndividualsFrom&gt;
     * &lt;/AcceptancePolicy&gt;
     * ...
     * </pre><p>
     *
     * This configures this class for accepting any individual from any
     * generation. The following values are allowed for <code>AcceptIndividualsFrom</code>:
     *
     * <pre>
     *    ANY_GENERATION
     *    OLDER_GENERATION
     *    NEWER_GENERATION
     *    SAME_GENERATION
     * </pre>
     */

    /**
     * Configures this class with a given {@link Configuration}.<p>
     *
     * Configuration example:
     *
     * <pre>
     * ...
     * &lt;AcceptancePolicy&gt;
     *     &lt;Class&gt;...parallel.migration.GenerationBasedAcceptance&lt;/Class&gt;
     *     &lt;AcceptIndividualsFrom&gt;ANY_GENERATION&lt;/AcceptIndividualsFrom&gt;
     * &lt;/AcceptancePolicy&gt;
     * ...
     * </pre><p>
     *
     * This configures this class for accepting any individual from any
     * generation. The following values are allowed for <code>AcceptIndividualsFrom</code>:
     *
     * <pre>
     *    ANY_GENERATION
     *    OLDER_GENERATION
     *    NEWER_GENERATION
     *    SAME_GENERATION
     * </pre>
     */
    @Override
    public void configure(Configuration conf) {
        String acceptFrom = conf.getString("AcceptIndividualsFrom");
        
        if(acceptFrom.equals("ANY_GENERATION")) {
            setAcceptIndividualsFrom(ANY_GENERATION);
        } else if(acceptFrom.equals("OLDER_GENERATION")) {
            setAcceptIndividualsFrom(OLDER_GENERATION);
        } else if(acceptFrom.equals("SAME_GENERATION")) {
            setAcceptIndividualsFrom(SAME_GENERATION);
        } else if(acceptFrom.equals("NEWER_GENERATION")) {
            setAcceptIndividualsFrom(NEWER_GENERATION);
        } else {
            throw new RuntimeException("GenerationBasedAcceptance (configure" +
                "): Wrong parameter " + acceptFrom + ". Only " +
                "ANY_GENERATION, SAME_GENERATION, OLDER_GENERATION and NEWER_GENERATION are allowed." );
        }
    }
    
    @Override
    public String toString() {
        String accept = "";
        
        switch(getAcceptIndividualsFrom()) {
            case SAME_GENERATION:
                accept = "SAME_GENERATION";
                break;
            case OLDER_GENERATION:
                accept = "OLDER_GENERATION";
                break;
            case NEWER_GENERATION:
                accept = "NEWER_GENERATION";
                break;
            case ANY_GENERATION:
            default:
                accept = "ANY_GENERATION";
                break;
        }
        
        return "GenerationBasedAcceptance (" + accept + ")";
    }
    
    /* ---- Getters and setters follow ---- */
    
    public int getAcceptIndividualsFrom() {
        return acceptIndividualsFrom;
    }
    
    /**
     * Configures the way individuals are accepted. The following values are
     * correct:
     *
     * <ul>
     *   <li><code>ANY_GENERATION</code>: Accepts any individual from any generation.</li>
     *   <li><code>SAME_GENERATION</code>: Accepts any individual from the sending
     *                                     node that has the same generation of the receiving node.</li>
     *   <li><code>OLDER_GENERATION</code>: Accepts any individual from the sending
     *                                     node that has the same generation of the receiving node
     *                                     or an older one.</li>
     *   <li><code>NEWER_GENERATION</code>: Accepts any individual from the sending
     *                                     node that has the same generation of the receiving node
     *                                     or a newer one.</li>
     * </ul><p>
     *
     * The default is <code>ANY_GENERATION</code>. A runtime exception is thrown
     * if an invalid value is passed.
     */
    public void setAcceptIndividualsFrom(int acceptIndividualsFrom) {
        if((acceptIndividualsFrom != ANY_GENERATION) &&
            (acceptIndividualsFrom != SAME_GENERATION) &&
            (acceptIndividualsFrom != OLDER_GENERATION) &&
            (acceptIndividualsFrom != NEWER_GENERATION)) {
            
            throw new RuntimeException("GenerationBasedAcceptance (setAcceptIndividualsFrom" +
                "): Wrong parameter. Only " +
                "ANY_GENERATION, SAME_GENERATION, OLDER_GENERATION and NEWER_GENERATION are allowed." );
        }
        
        this.acceptIndividualsFrom = acceptIndividualsFrom;
    }
    
    public List<MigrationObject> getPendingMigrants() {
        return pendingMigrants;
    }
    
    public void setPendingMigrants(List<MigrationObject> pendingMigrants) {
        this.pendingMigrants = pendingMigrants;
    }
}
