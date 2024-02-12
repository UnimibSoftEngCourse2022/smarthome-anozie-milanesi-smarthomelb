package org.smarthome.builder;

import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.domain.sensor.TemperatureSensor;
import org.smarthome.domain.temperature.AirConditioner;
import org.smarthome.domain.temperature.TemperatureSettings;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeRoomBuilder implements RoomBuilder {

    private final String name;
    private final List<Light> lights;
    private TemperatureSettings temperatureSettings;
    private AirConditioner airConditioner;
    private PresenceSensor presenceSensor;
    private TemperatureSensor temperatureSensor;

    public SmartHomeRoomBuilder(String name) {
        this.name = name;
        this.lights = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Light> getLights() {
        return lights;
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

    @Override
    public SmartHomeRoomBuilder addLight(Light light) {
        lights.add(light);
        return this;
    }

    @Override
    public SmartHomeRoomBuilder setTemperatureSettings(TemperatureSettings temperatureSettings) {
        this.temperatureSettings = temperatureSettings;
        return this;
    }

    @Override
    public SmartHomeRoomBuilder setAirConditioner(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
        return this;
    }

    @Override
    public SmartHomeRoomBuilder setPresenceSensor(PresenceSensor presenceSensor) {
        this.presenceSensor = presenceSensor;
        return this;
    }

    @Override
    public SmartHomeRoomBuilder setTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
        return this;
    }

    public Room create() {
        return new Room(this);
    }

}
