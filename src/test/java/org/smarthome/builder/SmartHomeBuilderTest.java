package org.smarthome.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.cleaning.Vacuum;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.protection.Siren;
import org.smarthome.exception.UnidentifiedRoomException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                .setSiren(new Siren())
                .create();

        assertNotNull(smartHome.getRooms());
        assertNotNull(smartHome.getVacuum());
        assertNotNull(smartHome.getAlarm());
        assertNotNull(smartHome.getCleaningControl());
        assertNotNull(smartHome.getProtectionControl());

        assertEquals(2, smartHome.getRooms().size());
        assertEquals("test1", smartHome.getRooms().get(0).getName());

        for (Room room : smartHome.getRooms()) {
            assertNotNull(room.getIllumination());
            assertNotNull(room.getIlluminationControl());
            for (Light light : room.getIllumination().getLights()) {
                assertNotNull(light);
            }
        }
    }

    @Test
    void createVacuumErrorTest() {
        assertThrows(UnidentifiedRoomException.class, () -> {
            // create rooms
            Room room1 = new SmartHomeRoomBuilder("test1")
                    .addLight(new Light())
                    .addLight(new Light())
                    .create();

            Room room2 = new SmartHomeRoomBuilder("test2")
                    .addLight(new Light())
                    .create();

            // create smartHome
            new SmartHomeBuilder()
                    .addRoom(room1)
                    .setVacuumChargingStationPosition(room2)
                    .create();
        });
    }

}