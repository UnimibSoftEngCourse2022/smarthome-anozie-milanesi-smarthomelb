package org.smarthome.domain.cleaning;

import org.smarthome.exception.CleaningException;
import org.smarthome.util.Constants;

public class Cleaning extends VacuumState {

    public Cleaning(Vacuum vacuum) {
        super(vacuum);
    }

    @Override
    public void clean() throws CleaningException {
        throw new CleaningException(Constants.alreadyCleaningMessage());
    }

    @Override
    public void stop() throws InterruptedException {
        vacuum.transitToChargingStation();
        vacuum.notifyStoppedCleaning();
    }

}
