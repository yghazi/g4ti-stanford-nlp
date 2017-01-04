package org.threatnlp.processor;

import java.io.IOException;

/**
 * Created by someone on 12/23/16.
 */
public class Processor {

    static ConfigUtil configUtil = new ConfigUtil();

    static NLPSecModelTrainer trainer = new NLPSecModelTrainer();

    static final long syncMillis = configUtil.getSyncHours() * 60 * 60 * 1000;

    public static void main(String[] args) {

        while (true) {
                trainer.generateUpdatedNERModel();
                try {
                    Thread.sleep(syncMillis);
                } catch (InterruptedException ex) {
                    System.err.println("Error occurred.");
                }
        }
    }

}
