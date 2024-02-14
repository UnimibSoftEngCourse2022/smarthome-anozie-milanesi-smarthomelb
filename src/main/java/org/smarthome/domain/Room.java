package org.smarthome.domain;

import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.controller.IlluminationControl;
import org.smarthome.controller.SensorPresenceControl;
import org.smarthome.controller.SensorTemperatureControl;
import org.smarthome.domain.illumination.Illumination;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.sensor.TemperatureSensor;
import org.smarthome.domain.temperature.AirConditioner;
import org.smarthome.controller.TemperatureControl;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.domain.temperature.TemperatureSettings;
import org.smarthome.simulation.RoomSimulation;

import java.util.List;

public class Room extends RoomSimulation {

    private final String name;
    private final Illumination illumination;
    private final TemperatureSettings temperatureSettings;
    private final AirConditioner airConditioner;
    private final PresenceSensor presenceSensor;
    private final TemperatureSensor temperatureSensor;
    private final IlluminationControl illuminationControl;
    private final TemperatureControl temperatureControl;

    public Room(SmartHomeRoomBuilder builder) {
        super();
        this.name = builder.getName();

        List<Light> lights = builder.getLights();
        if (lights != null && !lights.isEmpty()) {
            this.illumination = new Illumination(lights);
        } else {
            this.illumination = null;
        }

        this.airConditioner = builder.getAirConditioner();
        if (airConditioner != null) {
            airConditioner.setRoomTemperatureSimulation(getTemperatureSimulation());
        }

        this.presenceSensor = builder.getPresenceSensor();
        if (presenceSensor != null) {
            presenceSensor.setRoomPresenceSimulation(getPresenceSimulation());
        }

        TemperatureSettings initTemperatureSettings = builder.getTemperatureSettings();
        this.temperatureSettings = initTemperatureSettings == null ?
                new TemperatureSettings() : initTemperatureSettings;

        this.temperatureSensor = builder.getTemperatureSensor();
        if (temperatureSensor != null) {
            temperatureSensor.setRoomTemperatureSimulation(getTemperatureSimulation());
        }

        this.illuminationControl = new IlluminationControl(illumination);
        this.temperatureControl = new TemperatureControl(temperatureSettings, airConditioner);
    }

    public void setAutomaticControl(SmartHome smartHome) {
        if (smartHome != null) {
            new SensorPresenceControl(
                    presenceSensor,
                    smartHome.getProtectionControl(),
                    illuminationControl
            ).monitorSensor();

            new SensorTemperatureControl(
                    temperatureSensor,
                    temperatureControl
            ).monitorSensor();
        }
    }

    public String getName() {
        return name;
    }

    public Illumination getIllumination() {
        return illumination;
    }

    public TemperatureSettings getTemperatureSettings() {
        return temperatureSettings;
    }

    public AirConditioner getAirConditioner() {
        return airConditioner;
    }

    public PresenceSensor getPresenceSensor() {
        return presenceSensor;
    }

    public TemperatureSensor getTemperatureSensor() {
        return temperatureSensor;
    }

    public IlluminationControl getIlluminationControl() {
        return illuminationControl;
    }

    public TemperatureControl getTemperatureControl() {
        return temperatureControl;
    }

}
