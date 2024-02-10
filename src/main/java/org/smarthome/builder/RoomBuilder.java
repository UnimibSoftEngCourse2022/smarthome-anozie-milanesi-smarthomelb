package org.smarthome.builder;

import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.temperature.AirConditioner;

public interface RoomBuilder {
    SmartHomeRoomBuilder addLight(Light light);
    SmartHomeRoomBuilder setAirConditioner(AirConditioner airConditioner);
}
