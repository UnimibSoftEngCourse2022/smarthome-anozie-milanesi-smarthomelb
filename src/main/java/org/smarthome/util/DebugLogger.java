package org.smarthome.util;

import java.util.logging.Logger;

public class DebugLogger {

    private final Logger logger;

    public DebugLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String msg) {
        if (Constants.debugLoggerActive()) {
            logger.info(msg);
        }
    }

    public void warning(String msg) {
        if (Constants.debugLoggerActive()) {
            logger.warning(msg);
        }
    }

}
