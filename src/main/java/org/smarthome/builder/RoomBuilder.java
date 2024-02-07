package org.smarthome.builder;

import org.smarthome.domain.illumination.Light;

public interface RoomBuilder {
    RoomBuilder addLight(Light light);
}
