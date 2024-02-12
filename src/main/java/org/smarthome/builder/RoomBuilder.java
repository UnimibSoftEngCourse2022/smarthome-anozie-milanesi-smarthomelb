package org.smarthome.builder;

import org.smarthome.controller.TemperatureControl;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.domain.sensor.TemperatureSensor;
import org.smarthome.domain.temperature.AirConditioner;
import org.smarthome.domain.temperature.TemperatureSettings;

public interface RoomBuilder {
    RoomBuilder addLight(Light light);
    RoomBuilder setTemperatureSettings(TemperatureSettings temperatureSettings);
    RoomBuilder setAirConditioner(AirConditioner airConditioner);
    RoomBuilder setPresenceSensor(PresenceSensor presenceSensor);
    RoomBuilder setTemperatureSensor(TemperatureSensor temperatureSensor);
}
