package org.smarthome.util;

import java.util.logging.Logger;

import static org.smarthome.util.Constants.IS_DEBUG_MODE;

public class DebugLogger {

    private final Logger logger;

    public DebugLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String msg) {
        if (IS_DEBUG_MODE) {
            logger.info(msg);
        }
    }

    public void warning(String msg) {
        if (IS_DEBUG_MODE) {
            logger.warning(msg);
        }
    }

}
