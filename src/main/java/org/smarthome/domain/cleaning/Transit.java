package org.smarthome.domain.cleaning;

import org.smarthome.exception.CleaningException;
import org.smarthome.util.Constants;

public class Transit extends VacuumState {

    public Transit(Vacuum vacuum) {
        super(vacuum);
    }

    @Override
    public void clean() throws CleaningException {
        throw new CleaningException(Constants.transitToChargingStationMessage());
    }

    @Override
    public void stop() throws CleaningException {
        throw new CleaningException(Constants.cleaningAlreadyTerminatedMessage());
    }

}
