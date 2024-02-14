package org.smarthome.exception;

public class FieldOutOfRangeException extends Exception {

    public FieldOutOfRangeException(String message, int value, int bottomRange, int upperRange) {
        super(message + ": {value:" + value + ", range: [" + bottomRange + ", " + upperRange + "]");
    }

}
