package org.threatnlp.processor;


import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by yumna on 12/27/16.
 */
public class ConfigUtil {

    /**
     * NER training properties
     */
    private Properties nerTrainProperties = null;

    private final String NER_TRAIN_PROPS_PATH = "config/ner-train.prop";

    /**
     *
     * @return
     */
    public Properties getNERTrainProperties() {
        if (nerTrainProperties == null) {
            boolean valid = false;
            nerTrainProperties = new Properties();
            File file = new File(NER_TRAIN_PROPS_PATH);
            try (FileInputStream stream = new FileInputStream(file)) {
                nerTrainProperties.load(stream);
            } catch (IOException ex) {
                //TODO: handle it
            }
            // get training path
            if (nerTrainProperties.containsKey("trainDataPath")) {
                String fileList = getTrainFileList(nerTrainProperties.getProperty("trainDataPath"));
                if (fileList != null) {
                    nerTrainProperties.put("trainFileList", fileList);
                    valid = true;
                }
                nerTrainProperties.remove("trainDataPath");
            }

            if (!valid) {
                //TODO: improve message here
                System.out.println("Invalid properties file.");
                System.exit(-1);
            }
        }
        return nerTrainProperties;
    }


    /**
     * Returns a comma-separated list of tsv training files in the given path
     *
     * @param path null if error, empty if no file, else comma-sep paths
     * @return
     */
    public String getTrainFileList(String path) {
        Path dataPath = Paths.get(path);
        StringBuffer buff = new StringBuffer("");
        DirectoryStream.Filter<Path> filter = (Path p) -> !Files.isDirectory(p) && p.toString().contains(".tsv");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataPath, filter)) {
            for (Path p: stream) {
                buff.append(p.toAbsolutePath()).append(",");
            }
            int i = buff.length();
            if(i > 0) //delete the last comma
                buff.deleteCharAt(i-1);
        } catch (IOException ex) {
            //TODO: handle it
            return null;
        }

        return buff.toString().trim();
    }
}
