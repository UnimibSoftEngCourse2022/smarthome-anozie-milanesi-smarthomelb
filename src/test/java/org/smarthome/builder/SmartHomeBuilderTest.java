package org.smarthome.builder;

import org.junit.jupiter.api.Test;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.illumination.Light;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SmartHomeBuilderTest {

    @Test
    void createSmartHomeTest() {
        // create rooms
        Room room1 = new SmartHomeRoomBuilder("test1")
                .addLight(new Light())
                .addLight(new Light())
                .create();

        Room room2 = new SmartHomeRoomBuilder("test2")
                .addLight(new Light())
                .create();

        // create smartHome
        SmartHome smartHome = new SmartHomeBuilder()
                .addRoom(room1)
                .addRoom(room2)
                .setVacuumChargingStationPosition(room1)
                .create();

        assertNotNull(smartHome.getRooms());
        assertNotNull(smartHome.getVacuum());

        assertEquals(2, smartHome.getRooms().size());
        assertEquals("test1", smartHome.getRooms().get(0).getName());

        for (Room room : smartHome.getRooms()) {
            assertNotNull(room.getIllumination());
            for (Light light : room.getIllumination().getLights()) {
                assertNotNull(light);
            }
        }
    }

}