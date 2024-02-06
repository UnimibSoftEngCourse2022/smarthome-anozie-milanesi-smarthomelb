package org.smarthome.domain;

import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.controller.CleaningControl;
import org.smarthome.controller.ProtectionControl;
import org.smarthome.domain.cleaning.Vacuum;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.domain.protection.Siren;

import java.util.List;

public class SmartHome {

    private final List<Room> rooms;
    private final Vacuum vacuum;
    private final Alarm alarm;
    private final CleaningControl cleaningControl;
    private final ProtectionControl protectionControl;

    public SmartHome(SmartHomeBuilder builder) {
        this.rooms = builder.getRooms();
        Vacuum vacuum = null;
        Room chargingStationPosition = builder.getVacuumChargingStationPosition();
        if (chargingStationPosition != null) {
            vacuum = new Vacuum(rooms, chargingStationPosition);
        }
        this.vacuum = vacuum;
        this.alarm = builder.getAlarm();
        this.cleaningControl = new CleaningControl(vacuum);
        this.protectionControl = new ProtectionControl(alarm);

        for (Room room : rooms) {
            room.setAutomaticControl(this);
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Vacuum getVacuum() {
        return vacuum;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public CleaningControl getCleaningControl() {
        return cleaningControl;
    }

    public ProtectionControl getProtectionControl() {
        return protectionControl;
    }

}
