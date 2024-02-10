package org.smarthome.simulation;

public class RoomPresenceSimulation {

    private boolean presence;

    public RoomPresenceSimulation() {
        this.presence = false;
    }

    public boolean isPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

}
