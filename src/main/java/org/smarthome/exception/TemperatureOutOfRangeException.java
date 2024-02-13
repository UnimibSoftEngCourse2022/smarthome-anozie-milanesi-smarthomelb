package org.smarthome.exception;

public class TemperatureOutOfRangeException extends Exception {

    public TemperatureOutOfRangeException(String message, int value, int bottomRange, int upperRange) {
        super(message + ": {value:" + value + ", range: [" + bottomRange + ", " + upperRange + "]");
    }

}
