package org.threatnlp.processor;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.Timing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Yumna Ghazi
 * @version 1.0
 * @since 2016-12-31
 */
public class NLPSecModelTrainer {

    final Logger logger = LoggerFactory.getLogger(NLPSecModelTrainer.class);

    ConfigUtil configUtil;

    CRFClassifier classifier;

    String serializeTo;

    String path = "g4ti-ner-model.ser.gz";


    public NLPSecModelTrainer() {
        configUtil = new ConfigUtil();
        classifier = new CRFClassifier(configUtil.getNERTrainProperties());
        serializeTo = classifier.flags.serializeTo;
    }

    public CRFClassifier getModel() {

            classifier = CRFClassifier.getClassifierNoExceptions(path);
        if (classifier == null) {
            generateUpdatedNERModel();
        }
        return classifier;
    }

    public void doTagging(String inputPath) {
        String input = null;
        try {
            input = new String(Files.readAllBytes(Paths.get(inputPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        input = input.trim();
        input = getModel().classifyToString(input);
        String[] x = input.split("\\s+");
        String s = "";
        for (int i = 0; i < x.length; i++) {

            System.out.println(x[i].replace("/", "\t"));
        }
    }

    public void generateUpdatedNERModel() {
        //CoreNLP class for logging timing metrics
        Timing timing = new Timing();

        int knownLCWordsLimit = classifier.flags.maxAdditionalKnownLCWords;
        classifier.flags.maxAdditionalKnownLCWords = -1;
        logger.info("Known LC words {}", knownLCWordsLimit );
        classifier.train();
        classifier.flags.maxAdditionalKnownLCWords = knownLCWordsLimit;
        timing.end("Seems like we're done!");
        classifier.loadTagIndex();

        if (serializeTo != null) {
            classifier.serializeClassifier(serializeTo);
        } else {
            //TODO might want to change to another default path
            classifier.serializeClassifier("/opt/g4ti/");
        }

    }


}
