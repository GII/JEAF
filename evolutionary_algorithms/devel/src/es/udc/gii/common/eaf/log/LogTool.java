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
import es.udc.gii.common.eaf.config.Configurable;
import es.udc.gii.common.eaf.util.ConfWarning;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;
import org.apache.commons.configuration.Configuration;

/**
 * This abstract class represents a log tool, this is used to record the information generated
 * by the algorithm while its execution.<p>
 *
 * This tool implements the observer pattern, so the observable objects call to the <i>update</i> method
 * to print the information.<p>
 *
 * To configure a log tool that extend this class, the xml code of the configuration file should be as
 * follows:<p>
 *
 * <pre>
 * {@code
 * <LogTool>
 *      <Class>value</Class>
 *      <Folder>value</Folder>
 *      <Name>value</Name>
 *      <!-- More parameters if they are necessary -->
 * </LogToo>
 * }
 * </pre>
 *
 * Where the tag <i>Class</i> is mandatory, and indicates the specific class to be used. The tags
 * <i>Folder</i> and <i>Name</i> are optional, and indicates the folder where the log will be recorded
 * and the name of the file, respectively. If these two tags do not appear, their defautl value is used.<p>
 *
 * Default values:
 * <ul>
 * <li>Folder default value is "working_directory/OF".
 * <li>Name default value is ALG_POP_TS.txt (monoprocessor environment) or ALG_POP_TS_ND.txt (distributed environment).
 * </ul>
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public abstract class LogTool implements Observer, Configurable {

    private PrintStream log;
    private String folder =
            System.getProperty("user.dir") + File.separatorChar + "OF" + File.separatorChar;
    protected String name = "ALG_POP_TS";
    protected String fileExtension = ".txt";
    private boolean doCreateFile = true;

    protected String oldName;
    protected String oldFolder;
    
    private void createFile(String folder, String file_name) {

        File new_file;
        File new_folder;

        try {
            if(!folder.endsWith(String.valueOf(File.separatorChar))) {
                folder += File.separatorChar;
            }          
            
            new_folder = new File(folder);
            new_folder.mkdirs();
            new_file = new File(folder + file_name);

            this.log = new PrintStream(new_file, "UTF-8");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void configure(Configuration conf) {
        if (conf.containsKey("Folder")) {
            this.folder = conf.getString("Folder");
        } else {
            ConfWarning w = new ConfWarning("Folder", this.folder);
            w.warn();
        }
        if (conf.containsKey("Name")) {
            this.name = conf.getString("Name");
        } else {
            ConfWarning w = new ConfWarning("Name", this.name);
            w.warn();
        }
    }

    public PrintStream getLog() {

        return this.log;

    }

    public void setFile(String fileName) {
        this.folder = fileName;
    }

    public String getLogID() {
        return "log";
    }

    public String getNodeID() {
        return "0";
    }

    @Override
    public void update(Observable o, Object arg) {

        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm) o;
        String folder_name, file_name;

//        if (algorithm.getState() == EvolutionaryAlgorithm.INIT_STATE) {
        if (doCreateFile) {
            doCreateFile = false;
            this.oldFolder = this.folder;
            folder_name = LogPattern.replace(this.folder, algorithm, this);
            this.oldName = this.name;
            file_name = LogPattern.replace(this.name + this.fileExtension, algorithm, this);

            this.createFile(folder_name, file_name);
        }

        if (algorithm.getState() == EvolutionaryAlgorithm.CLOSE_LOGS_STATE) {
            doCreateFile = true;
            this.folder = this.oldFolder;
            this.name = this.oldName;
            this.log.close();        
        }
        
//        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        getLog().close();
    } 
    
    public void setDoCreateFile(boolean doCreateFile) {
        this.doCreateFile = doCreateFile;
    }
}
