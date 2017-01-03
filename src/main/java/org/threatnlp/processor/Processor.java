package org.threatnlp.processor;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

import java.util.List;


/**
 * Created by someone on 12/23/16.
 */
public class Processor {

    static NLPSecModelTrainer trainer = new NLPSecModelTrainer();

    static long lastUpdated = 0;

    public static void main(String[] args) {

        while (true) {
            if (lastUpdated == 0 || lastUpdated > System.currentTimeMillis())
                trainer.generateUpdatedNERModel();
        }
    }
}
