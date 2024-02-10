package org.smarthome.builder;

import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.temperature.AirConditioner;

public interface RoomBuilder {
    RoomBuilder addLight(Light light);
    RoomBuilder setAirConditioner(AirConditioner airConditioner);
}
