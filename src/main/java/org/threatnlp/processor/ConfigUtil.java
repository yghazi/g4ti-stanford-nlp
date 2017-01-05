package org.threatnlp.processor;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Yumna Ghazi
 * @version 1.0
 * @since 2016-12-27
 *
 */
public class ConfigUtil {
    private final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
    /**
     * NER training properties
     */
    private Properties nerTrainProperties = null;

    private final String NER_TRAIN_PROPS_PATH = System.getProperty("user.dir")+"/config/ner-train.prop";

    private long syncHours = 1;


    public long getSyncHours() {
        return syncHours;
    }

    /**
     * @return Properties
     */
    public Properties getNERTrainProperties() {
        if (nerTrainProperties == null) {
            boolean valid = false;
            nerTrainProperties = new Properties();
            File file = new File(NER_TRAIN_PROPS_PATH);
            try (FileInputStream stream = new FileInputStream(file)) {
                nerTrainProperties.load(stream);
            } catch (IOException ex) {
                ex.printStackTrace();
                //TODO: handle it
            }
            // get training path
            String tmp = getTrainFileList(nerTrainProperties.getProperty("trainDataPath", null));
            if (tmp != null) {
                nerTrainProperties.put("trainFileList", tmp);
                valid = true;
            }
            nerTrainProperties.remove("trainDataPath");

            //get syncHours
            tmp = nerTrainProperties.getProperty("syncHours", "1");
            syncHours = Long.parseLong(tmp);
            nerTrainProperties.remove("syncHours");

                if (!valid) {
                    //TODO: improve message here
                    logger.error("Invalid properties file {} , bye bye ....", NER_TRAIN_PROPS_PATH);
                    System.exit(-1);
                }
        }
        return nerTrainProperties;
    }


    /**
     * Returns a comma-separated list of tsv training files in the given path
     *
     * @param path null if error, empty if no file, else comma-sep paths
     * @return string
     */
    public String getTrainFileList(String path) {

        Path dataPath = Paths.get(path);
        StringBuffer buff = new StringBuffer("");
        DirectoryStream.Filter<Path> filter = (Path p) -> !Files.isDirectory(p) && p.toString().contains(".tsv");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataPath, filter)) {
            stream.forEach(p -> {
                buff.append(p.toAbsolutePath()).append(",");
            });
            int i = buff.length();
            if (i > 0) //delete the last comma
                buff.deleteCharAt(i - 1);
        } catch (IOException ex) {
            //TODO: handle it
            logger.error("", ex);
            return null;
        }

        return buff.toString().trim();
    }
}
