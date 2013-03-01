/*
 *  Copyright (C) 2011 Grupo Integrado de Ingeniería
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.common.eaf.stoptest;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import org.apache.commons.configuration.Configuration;

/**
 * This stop test checks if the evolutionary process has exceeded an user
 * defined wall time in milliseconds. The current time is checked against a 
 * reference time stored the first (and subsequent) time(s) that the method
 * {@link #reset(es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm)}
 * is called.<p/>
 *
 * The configuration of the stop test is as follows:
 *
 * <pre>
 * {@code
 * <StopTest>
 *      <Class>es.udc.gii.common.eaf.stoptest.WallTimeStopTest</Class>
 *      <MaxWallTime>value</MaxWallTime>
 * </StopTest>
 * }
 * </pre>
 *
 * Where {@code value >= 0}. If this condition is not met, an {@link AssertionError}
 * is thrown.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class WallTimeStopTest extends SimpleStopTest {

    private long maxWallTime = 0;
    private long init = 0;

    public WallTimeStopTest() {
    }

    @Override
    public void reset(EvolutionaryAlgorithm algorithm) {
        this.init = System.currentTimeMillis();
    }

    @Override
    public boolean isReach(EvolutionaryAlgorithm algorithm) {
        return (System.currentTimeMillis() - this.init) >= this.maxWallTime;
    }

    @Override
    public void configure(Configuration conf) {
        this.maxWallTime = conf.getLong("MaxWallTime");
        if (this.maxWallTime < 0) {
            throw new AssertionError("Maximum wall time must greather than cero.");
        }
    }
}
