package org.smarthome.exception;

import org.smarthome.util.Constants;

public class WrongSecurityPinException extends Exception {

    public WrongSecurityPinException() {
        super(Constants.wrongSecurityPinMessage());
    }

}
