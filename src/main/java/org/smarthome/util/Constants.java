package org.smarthome.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class Constants {

    private static final DebugLogger logger = new DebugLogger(Logger.getLogger(Constants.class.getName()));

    private static final String RESOURCE_FILE = "application.properties";
    private static final Properties properties = new Properties();

    private Constants() {}

    static {
        try (InputStream input = Constants.class.getClassLoader().getResourceAsStream(RESOURCE_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

    public static boolean debugModeActive() {
        return Boolean.parseBoolean(properties.getProperty("debug.mode.active"));
    }

    public static boolean debugLoggerActive() {
        return Boolean.parseBoolean(properties.getProperty("debug.logger.active"));
    }

    public static String alreadyCleaningMessage() {
        return properties.getProperty("already.cleaning.message");
    }

    public static String transitToChargingStationMessage() {
        return properties.getProperty("transit.to.charging.station.message");
    }

    public static String notCleaningYetMessage() {
        return properties.getProperty("not.cleaning.yet.message");
    }

    public static String cleaningAlreadyTerminatedMessage() {
        return properties.getProperty("cleaning.already.terminated.message");
    }

    public static int cleaningRoomMsDuration() {
        if (debugModeActive()) {
            return Integer.parseInt(properties.getProperty("cleaning.room.ms.duration.debug"));
        } else {
            return Integer.parseInt(properties.getProperty("cleaning.room.ms.duration"));
        }
    }

    public static int movingToRoomMsDuration() {
        if (debugModeActive()) {
            return Integer.parseInt(properties.getProperty("moving.to.room.ms.duration.debug"));
        } else {
            return Integer.parseInt(properties.getProperty("moving.to.room.ms.duration"));
        }
    }

    public static int defaultIdealTemperature() {
        return Integer.parseInt(properties.getProperty("default.ideal.temperature"));
    }

    public static int temperatureChangeMsDuration() {
        if (debugModeActive()) {
            return Integer.parseInt(properties.getProperty("temperature.change.ms.duration.debug"));
        } else {
            return Integer.parseInt(properties.getProperty("temperature.change.ms.duration"));
        }
    }

    public static int presenceTimerMsDuration() {
        if (debugModeActive()) {
            return Integer.parseInt(properties.getProperty("presence.timer.ms.duration.debug"));
        } else {
            return Integer.parseInt(properties.getProperty("presence.timer.ms.duration"));
        }
    }

    public static int sensorIterationPeriodMsDuration() {
        return Integer.parseInt(properties.getProperty("sensor.iteration.period.ms.duration"));
    }

    public static int emergencyTimerMsDuration() {
        if (debugModeActive()) {
            return Integer.parseInt(properties.getProperty("emergency.timer.ms.duration.debug"));
        } else {
            return Integer.parseInt(properties.getProperty("emergency.timer.ms.duration"));
        }
    }

}
