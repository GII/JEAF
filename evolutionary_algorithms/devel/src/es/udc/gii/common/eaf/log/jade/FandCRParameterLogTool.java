/*
 *  Copyright (C) 2010 Grupo Integrado de Ingeniería 
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

package es.udc.gii.common.eaf.log.jade;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.EvolutionaryStrategyMutation;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.DEMutationOperator;
import es.udc.gii.common.eaf.log.LogTool;
import es.udc.gii.common.eaf.plugin.parameter.jade.JADECRAdaptiveParameter;
import es.udc.gii.common.eaf.plugin.parameter.jade.JADEFAdaptiveParameter;
import java.util.Observable;

/**
 * This log tool is used to record the mean values of F and CR during the execution of a JADE Algorithm.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class FandCRParameterLogTool extends LogTool {

    private JADEFAdaptiveParameter F_plugin = null;

    private JADECRAdaptiveParameter CR_plugin = null;

    @Override
    public void update(Observable o, Object arg) {

        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm) o;
        DEMutationOperator operator;

        super.update(o, arg);
        operator = (DEMutationOperator) ((EvolutionaryStrategyMutation
                )algorithm.getReproductionChain().getOperators().get(0)).getOperator();

        if (this.F_plugin == null) {

            this.F_plugin = (JADEFAdaptiveParameter) operator.getMutationStrategy().getFPlugin();


        }

        if (this.CR_plugin == null) {
            this.CR_plugin = (JADECRAdaptiveParameter) operator.getCrossOverScheme().getCRPlugin();
        }

        if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE && arg == null) {

            super.getLog().println(algorithm.getGenerations() + " "
                    + this.F_plugin.getMu_f() + " "  + this.CR_plugin.getMu_cr());

        }

    }

    @Override
    public String getLogID() {
        return "fandcr";
    }

}

