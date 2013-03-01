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
 * MigrationOperator.java
 *
 * Created on March 12, 2008, 7:48 PM
 *
 */
package es.udc.gii.common.eaf.algorithm.parallel.migration;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.replace.ReplaceOperator;
import es.udc.gii.common.eaf.algorithm.parallel.migration.acceptance.GenerationBasedAcceptance;
import es.udc.gii.common.eaf.algorithm.parallel.migration.acceptance.MigAcceptancePolicy;
import es.udc.gii.common.eaf.algorithm.parallel.migration.culling.MigCullingStrategy;
import es.udc.gii.common.eaf.algorithm.parallel.migration.culling.WorstCull;
import es.udc.gii.common.eaf.algorithm.parallel.migration.selection.BestMigration;
import es.udc.gii.common.eaf.algorithm.parallel.migration.selection.MigSelectionStrategy;
import es.udc.gii.common.eaf.algorithm.parallel.topology.migration.FullConnectedMigrationTopology;
import es.udc.gii.common.eaf.algorithm.parallel.topology.migration.MigrationObject;
import es.udc.gii.common.eaf.algorithm.parallel.topology.migration.MigrationTopology;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * A migration operator is a replace operator that performs the exchange of
 * genetic material between two islands in a parallel evolutionary algorithm,
 * where this makes sense (i.e. in an island model).<p>
 *
 * A migration operator has a {@link MigrationTopology} of islands associated, which takes care
 * of the underlying connections between the islands. A {@link MigAcceptancePolicy}
 * says which individuals from those who have come from other islands are accepted.
 * An example could be to accept only individuals from an older generation than
 * the current island's generation. It also has a {@link MigCullingStrategy},
 * which is in charge of selecting the individuals to be removed from the the
 * current population of the island, and a {@link MigSelectionStrategy} for selecting
 * the individuals that migrate from the current population of the island to
 * the neighbours islands.<p>
 *
 * Migration is controlled by a migration frequency. This frequency is an integer
 * that states how often is migration applied. For example, if this frequency is
 * set to 3, a migration will be performed every 3 generations.<p>
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class MigrationOperator extends ReplaceOperator {

    public static final int NOT_INITIALIZED = -1;
    public static final int SENDING = 0;
    public static final int RECEIVED = 5;
    public static final int ACCEPTED = 10;
    public static final int CULLED = 15;
    private int state = NOT_INITIALIZED;
    /** The topology that connects the islands. */
    private MigrationTopology topology = null;
    /** The strategy to follow for culling individuals from the current population. */
    private MigCullingStrategy migCullingStrategy = null;
    /** The strategy to follow for selecting those individuals to send to other islands. */
    private MigSelectionStrategy migSelectionStrategy = null;
    /** The policy for accepting the incommig individuals. */
    private MigAcceptancePolicy migAcceptancePolicy = null;
    /** The migration frequency [generations / migration]. */
    private int migrationFrequecy = 0;
    /** Synchronize all processes before receiving. */
    private boolean synchronizedReceive = false;
    private List<Individual> currentAccepted = null;
    private List<Individual> currentCulled = null;
    private List<MigrationObject> currentMigrants = null;
    private MigrationObject currentMigrant = null;
    private List<Individual> currentPopulation = null;

    /** Creates a new instance of MigrationOperator. */
    public MigrationOperator() {
    }

    /** Creates a new instance of MigrationOperator. */
    public MigrationOperator(MigrationTopology topology, MigCullingStrategy migCullingStrategy,
            MigSelectionStrategy migSelectionStrategy, MigAcceptancePolicy migAcceptancePolicy,
            int migrationFrequecy) {

        setMigrationTopology(topology);
        setMigCullingStrategy(migCullingStrategy);
        setMigSelectionStrategy(migSelectionStrategy);
        setMigrationFrequecy(migrationFrequecy);
        setMigAcceptancePolicy(migAcceptancePolicy);
    }

    /** The replace method of {@link ReplaceOperator} is overwritten and forced
     * to be final so that no subclass can overwrite it. The migration is forced
     * to be implemented by the {@link MigrationOperator#migrate} method. */
    @Override
    final protected List<Individual> replace(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation) {

        /* Migration is conditioned to a migration frequency. */
        if (algorithm.getGenerations() % migrationFrequecy == 0) {
            return migrate(algorithm, toPopulation);
        } else {
            return toPopulation;
        }
    }

    /** Configures this operator.<p>
     *
     *  Configuration example:
     *
     *  <pre>
     *  &lt;Operator&gt;
     *      &lt;Class&gt;...parallel.migration.MigrationOperator&lt;/Class&gt;
     *
     *      &lt;MigrationFrequency&gt;2&lt;/MigrationFrequency&gt;
     *
     *      &lt;CullingStrategy&gt;
     *          &lt;Class&gt;...&lt;/Class&gt;
     *          ...
     *      &lt;/CullingStrategy&gt;
     *
     *      &lt;SelectionStrategy&gt;
     *          &lt;Class&gt;...&lt;/Class&gt;
     *          ...
     *      &lt;/SelectionStrategy&gt;
     *
     *      &lt;AcceptancePolicy&gt;
     *          &lt;Class&gt;...&lt;/Class&gt;
     *          ...
     *      &lt;/AcceptancePolicy&gt;
     *
     *      &lt;Topology&gt;
     *          &lt;Class&gt;...&lt;/Class&gt;
     *          ...
     *      &lt;/Topology&gt;
     *
     *      &lt;Synchronized/&gt;
     *  &lt;/Operator&gt;
     *  </pre>
     *
     * <p>Migration will be performed every 2 generations and, before receiving,
     * all nodes are synchronized. If no synchronization is needed, simply remove
     * the {@code <Synchronized/>} tag.
     *
     * @param conf Configuration.
     */
    @Override
    public void configure(Configuration conf) {
        try {
            
            MigrationTopology mt = null;
            
            if(conf.containsKey("Topology.Class")) {
                mt = (MigrationTopology) Class.forName(
                        conf.getString("Topology.Class")).newInstance();
                mt.configure(conf.subset("Topology"));
            } else {
                (new ConfWarning("MigrationOperator.Topology.Class", 
                        "FullConnectedMigrationTopology")).warn();                
                mt = new FullConnectedMigrationTopology();
            }
                        
            setMigrationTopology(mt);

            if (mt.isConnected()) {
                int migFreq = 1;
                MigCullingStrategy mCS = null;
                MigSelectionStrategy mSS = null;
                MigAcceptancePolicy mAP = null;
                
                if(conf.containsKey("MigrationFrequency")) {
                    migFreq = conf.getInt("MigrationFrequency");
                } else {                    
                    (new ConfWarning("MigrationOperator.MigrationFrequency", 
                        migFreq)).warn();
                }
                
                setMigrationFrequecy(migFreq);
                
                if(conf.containsKey("CullingStrategy.Class")) {
                    mCS = (MigCullingStrategy) Class.forName(
                        conf.getString("CullingStrategy.Class")).newInstance();
                    mCS.configure(conf.subset("CullingStrategy"));
                } else {
                    mCS = new WorstCull();
                    (new ConfWarning("MigrationOperator.CullingStrategy.Class", 
                        "WorstCull")).warn();                    
                }
                
                setMigCullingStrategy(mCS);
                
                if(conf.containsKey("SelectionStrategy.Class")) {
                    mSS = (MigSelectionStrategy) Class.forName(
                        conf.getString("SelectionStrategy.Class")).newInstance();
                    mSS.configure(conf.subset("SelectionStrategy"));
                } else {
                    (new ConfWarning("MigrationOperator.SelectionStrategy." +
                            "Class", "BestMigration")).warn();
                    mSS = new BestMigration();
                }
                
                setMigSelectionStrategy(mSS);
                
                if(conf.containsKey("AcceptancePolicy.Class")) {
                    mAP = (MigAcceptancePolicy) Class.forName(
                        conf.getString("AcceptancePolicy.Class")).newInstance();
                    mAP.configure(conf.subset("AcceptancePolicy"));
                } else {
                    (new ConfWarning("MigrationOperator.AcceptancePolicy." +
                            "Class", "GenerationBasedAcceptance")).warn();
                    mAP = new GenerationBasedAcceptance();                    
                }
                
                setMigAcceptancePolicy(mAP);

                setSynchronized(conf.containsKey("Synchronized"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Performs migration. */
    protected List<Individual> migrate(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation) {

        /* Make sure that we operate with a connected topology. */
        if (!getMigrationTopology().isConnected() && getMigrationTopology().isInitialized()) {
            /* If the topology *is* initialized but this process is not connected
             * to it, do nothing! */
            return toPopulation;
        }

        /* From here on we asume the topology is correctly initialized and the
         * process connected to it. */

        /* Result population. */
        List<Individual> resultPopulation = new ArrayList<Individual>();
        resultPopulation.addAll(toPopulation);
        this.currentPopulation = resultPopulation;

        /* Collect data to migrate. */
        MigrationObject migrant = selectIndividualsForMigration(algorithm, toPopulation);

        /* Notify observers what we send. */
        this.currentMigrant = migrant;
        notifyObservers(SENDING);

        /* Send data. */
        getMigrationTopology().send(migrant);

        /* Synchronize all processes if requested. */
        if (isSynchronized()) {
            getMigrationTopology().synchronize();
        }

        /* Receive data. */
        List<MigrationObject> migrants = getMigrationTopology().receive();

        /* Notify observers what we've received. */
        this.currentMigrants = migrants;
        notifyObservers(RECEIVED);

        /* Accept the incomming individuals. */
        List<Individual> acceptedIndividuals = acceptIndividuals(algorithm, toPopulation, migrants);

        /* Notify observers what we've accepted. */
        this.currentAccepted = acceptedIndividuals;
        notifyObservers(ACCEPTED);

        /* Get the individuals to cull. */
        List<Individual> individualsToCull =
                cullIndividuals(algorithm, acceptedIndividuals, toPopulation);

        /* Cull. Don't use 'removeAll()' since we want to remove only one occurrence
         * of each individual. */
        for (int i = 0; i < individualsToCull.size(); i++) {
            resultPopulation.remove(individualsToCull.get(i));
        }

        /* Notify observers what we culled. */
        this.currentCulled = individualsToCull;
        notifyObservers(CULLED);

        /* Add the accepted individuals. */
        resultPopulation.addAll(acceptedIndividuals);

        this.currentAccepted = null;
        this.currentCulled = null;
        this.currentMigrant = null;
        this.currentMigrants = null;
        this.currentPopulation = null;

        return resultPopulation;
    }

    public void notifyObservers(int state) {
        setState(state);
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Selects the individuals that will be sent to other nodes. */
    protected MigrationObject selectIndividualsForMigration(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation) {

        /* Collect data to migrate. */
        MigrationObject migrant = new MigrationObject();
        migrant.setGeneration(algorithm.getGenerations());
        migrant.setIndividuals(getMigSelectionStrategy().
                getIndividualsForMigration(algorithm, toPopulation));

        return migrant;
    }

    /**
     * Returns the individuals that satisfy some criterion (see {@link MigAcceptancePolicy})
     * among those which are received from other nodes.
     */
    protected List<Individual> acceptIndividuals(EvolutionaryAlgorithm algorithm,
            List<Individual> toPopulation, List<MigrationObject> migrants) {

        List<Individual> acceptedIndividuals = new ArrayList<Individual>();

        for (int i = 0; i < migrants.size(); i++) {
            acceptedIndividuals.addAll(getMigAcceptancePolicy().accept(migrants.get(i),
                    algorithm, toPopulation));
        }

        return acceptedIndividuals;
    }

    /**
     * Returns the individuals which are to be culled (removed) from the current
     * population considering the individuals which are beeing received and
     * accepted from other nodes.
     */
    protected List<Individual> cullIndividuals(EvolutionaryAlgorithm algorithm,
            List<Individual> acceptedIndividuals, List<Individual> toPopulation) {

        return getMigCullingStrategy().getIndividualsToCull(algorithm, acceptedIndividuals,
                toPopulation);
    }

    /** The name of this operator. */
    protected String myName() {
        return "MigrationOperator";
    }

    @Override
    public String toString() {
        return myName() + "Topology: " + topology +
                " MigCullingStrategy: " + getMigCullingStrategy() +
                " MigSelectionStrategy: " + getMigSelectionStrategy() +
                " Migration frecuency: " + getMigrationFrequecy() +
                " Synchronized: " + isSynchronized() +
                " Acceptance policy: " + getMigAcceptancePolicy();
    }

    /**
     * Terminates this migration operator correctly.
     */
    public void finish() {
        getMigrationTopology().finish();
    }

    /* ---- Getters and setters follow ---- */
    public boolean isSynchronized() {
        return synchronizedReceive;
    }

    public int getMigrationFrequecy() {
        return migrationFrequecy;
    }

    public void setMigrationFrequecy(int migrationFrequecy) {
        this.migrationFrequecy = migrationFrequecy;
    }

    public MigrationTopology getMigrationTopology() {
        return topology;
    }

    public void setMigrationTopology(MigrationTopology topology) {
        this.topology = topology;
        topology.initialize();
    }

    public MigCullingStrategy getMigCullingStrategy() {
        return migCullingStrategy;
    }

    public void setMigCullingStrategy(MigCullingStrategy migCullingStrategy) {
        this.migCullingStrategy = migCullingStrategy;
    }

    public MigSelectionStrategy getMigSelectionStrategy() {
        return migSelectionStrategy;
    }

    public void setMigSelectionStrategy(MigSelectionStrategy migSelectionStrategy) {
        this.migSelectionStrategy = migSelectionStrategy;
    }

    /** If set to {@code true} then before receiving the individuals a synchronization
     * of all process within the topology is performed. This is usefull for ensuring
     * that each process has sent its data when accepting individuals from only
     * the same generation and that all processes will hence receive data in the same generation. <p>
     *
     * The default is {@code false}.
     */
    public void setSynchronized(boolean synchronizedReceive) {
        this.synchronizedReceive = synchronizedReceive;
    }

    public MigAcceptancePolicy getMigAcceptancePolicy() {
        return migAcceptancePolicy;
    }

    public void setMigAcceptancePolicy(MigAcceptancePolicy migAcceptancePolicy) {
        this.migAcceptancePolicy = migAcceptancePolicy;
    }

    public int getState() {
        return state;
    }

    protected void setState(int state) {
        this.state = state;
    }

    public List<Individual> getCurrentAccepted() {
        return currentAccepted;
    }

    public List<Individual> getCurrentCulled() {
        return currentCulled;
    }

    public MigrationObject getCurrentMigrant() {
        return currentMigrant;
    }

    public List<MigrationObject> getCurrentMigrants() {
        return currentMigrants;
    }

    public List<Individual> getCurrentPopulation() {
        return currentPopulation;
    }
}
