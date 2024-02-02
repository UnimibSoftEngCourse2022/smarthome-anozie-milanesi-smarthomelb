package org.smarthome.builder;

import org.smarthome.domain.illumination.Light;

public interface RoomBuilder {
    SmartHomeRoomBuilder addLight(Light light);
}
