package org.smarthome.domain.cleaning;

import org.smarthome.exception.CleaningException;
import org.smarthome.util.Constants;

public class Charging extends VacuumState {

    public Charging(Vacuum vacuum) {
        super(vacuum);
    }

    @Override
    public void clean() throws InterruptedException {
        vacuum.cleaningHome();
        vacuum.transitToChargingStation();
        vacuum.notifyCompletedCleaning();
    }

    @Override
    public void stop() throws CleaningException {
        throw new CleaningException(Constants.notCleaningYetMessage());
    }

}
