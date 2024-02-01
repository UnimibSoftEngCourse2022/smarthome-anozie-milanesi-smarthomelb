package org.smarthome.domain.simulation;

public class RoomPresenceSimulation {

    private RoomPresenceListener roomPresenceListener;
    private boolean presence;

    public RoomPresenceSimulation() {
        this.presence = false;
    }

    public void setRoomPresenceListener(RoomPresenceListener roomPresenceListener) {
        this.roomPresenceListener = roomPresenceListener;
    }

    public boolean isPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

}
