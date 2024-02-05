package org.smarthome.domain.sensor;

import org.smarthome.domain.simulation.RoomPresenceSimulation;

public class PresenceSensor extends Sensor<Boolean> {

    private final RoomPresenceSimulation roomPresenceSimulation;

    public PresenceSensor(RoomPresenceSimulation roomPresenceSimulation) {
        super();
        this.roomPresenceSimulation = roomPresenceSimulation;
    }

    @Override
    public Boolean monitor() {
        return roomPresenceSimulation.isPresence();
    }

    @Override
    public boolean analyze(Boolean detected) {
        if (data != detected) {
            this.data = detected;
            return true;
        }
        return false;
    }

    @Override
    public void plan() {
        // no planning
    }

    @Override
    public void execute() {
        notifyDataChange();
    }

}
