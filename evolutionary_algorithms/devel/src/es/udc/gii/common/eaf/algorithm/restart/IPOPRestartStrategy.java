/*
 * Copyright (C) 2011 Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
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
package es.udc.gii.common.eaf.algorithm.restart;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.config.EAFConfiguration;
import es.udc.gii.common.eaf.util.ConfWarning;
import org.apache.commons.configuration.Configuration;

/**
 * This class implements a restrart strategy that increments the size of the population of the
 * algorithm before the independent restart is perform. The population is increased as a power of the
 * incr_factor parameter and the current number of restarts.
 * 
 * To config this operator the xml code is:<p>
 *
 * <pre>
 * {@code
 * <RestartStrategy>
 *      <Class>es.udc.gii.common.eaf.algorithm.restart.IPOPRestartStrategy</Class>
 *      <MaxNumRestarts>...</MaxNumRestarts>
 *      <IncrPopFactor>...</IncrPopFactor>
 * </RestartStrategy>
 * }
 * </pre>
 * 
 * where the tag MaxNumRestarts indicates the maximum number of restarts allowed, its default value is 1,
 * and the tag IncrPopFactor indicates the population incrementation factor, its default value is 1.0.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class IPOPRestartStrategy implements RestartStrategy {

    /**
     * This variable indicates the population increment factor.
     */
    private double incr_factor = 1.0;
    private int max_num_restarts = 1;
    private int curr_num_restarts = 0;

    @Override
    public void restart(EvolutionaryAlgorithm algorithm) {

        int curr_pop_size, new_pop_size;

        if (curr_num_restarts < max_num_restarts) {
            
            curr_num_restarts++;

            curr_pop_size = algorithm.getPopulation().getSize();
            new_pop_size = (int) Math.ceil(curr_pop_size * Math.pow(this.incr_factor, this.curr_num_restarts));

            //Re-configure the algorithm:
            algorithm.configure(EAFConfiguration.getInstance());
            
            algorithm.setPopulationSize(new_pop_size);
            

            algorithm.setState(EvolutionaryAlgorithm.INIT_STATE);
            
            
        } else {
            //Continue with the algorithm:
            algorithm.setState(EvolutionaryAlgorithm.SELECT_STATE);
        }
    }

    @Override
    public void configure(Configuration conf) {
        
        if (conf.containsKey("MaxNumRestarts")) {
            this.max_num_restarts = conf.getInt("MaxNumRestarts");
        } else {
            ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".MaxNumRestarts", 
                    this.max_num_restarts);
            w.warn();
        }
        
        if (conf.containsKey("IncrPopFactor")) {
            this.incr_factor = conf.getDouble("IncrPopFactor");
        } else {
            ConfWarning w = new ConfWarning(this.getClass().getSimpleName() + ".IncrPopFactor", 
                    this.incr_factor);
            w.warn();
        }
    }
}
