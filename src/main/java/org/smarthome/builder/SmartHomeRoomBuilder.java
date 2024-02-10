package org.smarthome.builder;

import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.domain.temperature.AirConditioner;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeRoomBuilder implements RoomBuilder {

    private final String name;
    private final List<Light> lights;
    private AirConditioner airConditioner;
    private PresenceSensor presenceSensor;

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

    public AirConditioner getAirConditioner() {
        return airConditioner;
    }

    public PresenceSensor getPresenceSensor() {
        return presenceSensor;
    }

    @Override
    public SmartHomeRoomBuilder addLight(Light light) {
        lights.add(light);
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

    public Room create() {
        return new Room(this);
    }

}
