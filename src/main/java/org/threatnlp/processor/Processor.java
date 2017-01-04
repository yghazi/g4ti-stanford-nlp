package org.threatnlp.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yumna Ghazi
 * @version 1.0
 * @since 2016-12-23
 *
 */
public class Processor {

    static final Logger logger = LoggerFactory.getLogger(Processor.class);

    static ConfigUtil configUtil = new ConfigUtil();

    static NLPSecModelTrainer trainer = new NLPSecModelTrainer();

    static final long syncMillis = configUtil.getSyncHours() * 60 * 60 * 1000;

    public static void main(String[] args) {

        while (true) {
                trainer.generateUpdatedNERModel();
                try {
                    Thread.sleep(syncMillis);
                } catch (InterruptedException ex) {
                    logger.error("Error occured", ex);
                }
        }
    }

}
