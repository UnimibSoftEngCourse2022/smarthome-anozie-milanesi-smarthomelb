package org.smarthome.builder;

import org.smarthome.domain.Room;
import org.smarthome.domain.cleaning.Vacuum;

public interface HomeBuilder {
    HomeBuilder addRoom(Room room);
    HomeBuilder setVacuum(Vacuum vacuum);
}
