package org.smarthome.domain.cleaning;

public class Charging extends VacuumState {

    public Charging(Vacuum vacuum) {
        super(vacuum);
    }

    @Override
    public void clean() {
        vacuum.cleaningHome();
        vacuum.transitToChargingStation();
    }

}
