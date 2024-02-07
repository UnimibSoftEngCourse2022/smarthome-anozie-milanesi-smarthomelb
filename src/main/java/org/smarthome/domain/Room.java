package org.smarthome.domain;

import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.controller.IlluminationControl;
import org.smarthome.controller.SensorPresenceControl;
import org.smarthome.domain.illumination.Illumination;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.simulation.RoomSimulation;

import java.util.List;

public class Room extends RoomSimulation {

    private final String name;
    private final Illumination illumination;
    private final PresenceSensor presenceSensor;
    private final IlluminationControl illuminationControl;
    private SensorPresenceControl sensorPresenceControl;

    public Room(SmartHomeRoomBuilder builder) {
        super();
        this.name = builder.getName();
        List<Light> lights = builder.getLights();
        if (lights != null && !lights.isEmpty()) {
            this.illumination = new Illumination(lights);
        } else {
            this.illumination = null;
        }
        this.presenceSensor = new PresenceSensor(getPresenceSimulation());
        this.illuminationControl = new IlluminationControl(illumination);
    }

    public void setAutomaticControl(SmartHome smartHome) {
        if (smartHome != null) {
            sensorPresenceControl = new SensorPresenceControl(
                    presenceSensor, smartHome.getProtectionControl(), illuminationControl);
            sensorPresenceControl.monitorSensor();
        }
    }

    public String getName() {
        return name;
    }

    public Illumination getIllumination() {
        return illumination;
    }

    public PresenceSensor getPresenceSensor() {
        return presenceSensor;
    }

    public IlluminationControl getIlluminationControl() {
        return illuminationControl;
    }

    public SensorPresenceControl getSensorPresenceControl() {
        return sensorPresenceControl;
    }

}
