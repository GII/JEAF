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
package es.udc.gii.common.eaf.log;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class LogPattern {

    private static String log_id = "ID";
    private static String algorithm_id = "ALG";
    private static String objective_id = "OF";
    private static String population_id = "POP";
    private static String dimension_id = "DIM";
    private static String timestamp_id = "TS";
    private static String node_id = "ND";
    private static String user_tag = "USR";

    public static String replace(String input, EvolutionaryAlgorithm algorithm, LogTool log) {

        String output;
        String time_stamp;

        Pattern pattern = Pattern.compile(log_id);
        Matcher matcher = pattern.matcher(input);
        output = matcher.replaceAll(log.getLogID());

        pattern = Pattern.compile(algorithm_id);
        matcher = pattern.matcher(output);
        output = matcher.replaceAll(algorithm.getAlgorithmID());

        if (algorithm.getProblem() != null && algorithm.getProblem().getObjectiveFunctions().size() > 0) {
            pattern = Pattern.compile(objective_id);
            matcher = pattern.matcher(output);
            output = matcher.replaceAll(algorithm.getProblem().getObjectiveFunctions().get(0).getClass().getSimpleName());
        }

        pattern = Pattern.compile(population_id);
        matcher = pattern.matcher(output);
        output = matcher.replaceAll(Integer.toString(algorithm.getPopulation().getSize()));

        pattern = Pattern.compile(dimension_id);
        matcher = pattern.matcher(output);
        output = matcher.replaceAll(Integer.toString(
                algorithm.getPopulation().getIndividual(0).getDimension()));

        pattern = Pattern.compile(timestamp_id);
        matcher = pattern.matcher(output);
        time_stamp = getTimeStamp();
        output = matcher.replaceAll(time_stamp);

        pattern = Pattern.compile(node_id);
        matcher = pattern.matcher(output);
        output = matcher.replaceAll(log.getNodeID());

        pattern = Pattern.compile(user_tag);
        matcher = pattern.matcher(output);
        output = matcher.replaceAll(algorithm.getUserTag());

        return output;

    }

    private static String getTimeStamp() {

        Calendar calendar = Calendar.getInstance();
        String timestamp = "";
        int month, day, hours, minutes, seconds, milliseconds;

        //Se añade el año:
        timestamp += calendar.get(Calendar.YEAR);

        //Se añade el mes --> primero hay que sumarle 1, y después añadirle un cero
        //a la izquierda si es menor que 10:
        month = calendar.get(Calendar.MONTH) + 1;
        timestamp += (month < 10 ? "0" + month : month);

        //Se añade el día --> hay que añadirle un 0 si es menor que 10:
        day = calendar.get(Calendar.DAY_OF_MONTH);
        timestamp += (day < 10 ? "0" + day : day);

        //Se añade la hora del día (en formato 24 horas):
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        timestamp += (hours < 10 ? "0" + hours : hours);

        //Se añaden los minutos:
        minutes = calendar.get(Calendar.MINUTE);
        timestamp += (minutes < 10 ? "0" + minutes : minutes);

        //Se añaden los segundos:
        seconds = calendar.get(Calendar.SECOND);
        timestamp += (seconds < 10 ? "0" + seconds : seconds);

        //Se añaden los milisegundos:
        milliseconds = calendar.get(Calendar.MILLISECOND);
        if (milliseconds < 10) {
            timestamp += "00" + milliseconds;
        } else if (milliseconds < 100) {
            timestamp += "0" + milliseconds;
        } else {
            timestamp += milliseconds;
        }

        return timestamp;

    }
}
