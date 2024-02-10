package org.smarthome.domain.sensor;

import org.smarthome.simulation.RoomPresenceSimulation;

public class PresenceSensor extends Sensor<Boolean> {

    private RoomPresenceSimulation roomPresenceSimulation;

    public PresenceSensor() {
        super();
    }

    public void setRoomPresenceSimulation(RoomPresenceSimulation roomPresenceSimulation) {
        this.roomPresenceSimulation = roomPresenceSimulation;
    }

    @Override
    public Boolean monitor() {
        return roomPresenceSimulation != null && roomPresenceSimulation.isPresence();
    }

    @Override
    public boolean analyze(Boolean detected) {
        if (!detected.equals(data)) {
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
