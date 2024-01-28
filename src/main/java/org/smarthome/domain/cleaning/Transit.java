package org.smarthome.domain.cleaning;

import org.smarthome.exception.CleaningException;

import static org.smarthome.util.Constants.TRANSIT_TO_CHARGING_STATION_MESSAGE;

public class Transit extends VacuumState {

    public Transit(Vacuum vacuum) {
        super(vacuum);
    }

    @Override
    public void clean() throws CleaningException {
        throw new CleaningException(TRANSIT_TO_CHARGING_STATION_MESSAGE);
    }

}
