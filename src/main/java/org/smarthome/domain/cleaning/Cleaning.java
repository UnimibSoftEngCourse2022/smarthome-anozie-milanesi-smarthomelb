package org.smarthome.domain.cleaning;

import org.smarthome.exception.CleaningException;

import static org.smarthome.util.Constants.ALREADY_CLEANING_MESSAGE;

public class Cleaning extends VacuumState {

    public Cleaning(Vacuum vacuum) {
        super(vacuum);
    }

    @Override
    public void clean() throws CleaningException {
        throw new CleaningException(ALREADY_CLEANING_MESSAGE);
    }

    @Override
    public void stop() throws InterruptedException {
        vacuum.transitToChargingStation();
        vacuum.notifyStoppedCleaning();
    }

}
