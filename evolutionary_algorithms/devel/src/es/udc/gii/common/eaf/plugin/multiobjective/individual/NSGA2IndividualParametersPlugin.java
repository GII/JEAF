/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.plugin.multiobjective.individual;

import es.udc.gii.common.eaf.algorithm.population.multiobjective.NSGA2Individual;
import es.udc.gii.common.eaf.plugin.multiobjective.crowding.Crowding;
import es.udc.gii.common.eaf.plugin.multiobjective.crowding.ObjectiveSpaceCrowding;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author pilar
 */
public class NSGA2IndividualParametersPlugin implements MultiobjectiveIndividualParametersPlugin<NSGA2Individual> {

    private Crowding crowding = new ObjectiveSpaceCrowding();

    @Override
    public void configure(Configuration conf) {
        try {
            if (conf.containsKey("Crowding.Class")) {
                this.setCrowding((Crowding) Class.forName(conf.getString("Crowding.Class")).newInstance());
                this.getCrowding().configure(conf);
            } else {
                this.setCrowding(new ObjectiveSpaceCrowding());
                (new ConfWarning("Crowding", "ObjectiveSpaceCrowding")).warn();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void calculateParameters(List<NSGA2Individual> individuals) {
        this.crowding.calculate(individuals);
                
    }

    public Crowding getCrowding() {
        return crowding;
    }

    public void setCrowding(Crowding crowding) {
        this.crowding = crowding;
    }
}
