package org.threatnlp.processor;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.Timing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    public NLPSecModelTrainer() {
        configUtil = new ConfigUtil();
        classifier = new CRFClassifier(configUtil.getNERTrainProperties());
        serializeTo = classifier.flags.serializeTo;
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
