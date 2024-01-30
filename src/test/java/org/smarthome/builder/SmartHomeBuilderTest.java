package org.smarthome.builder;

import org.junit.jupiter.api.Test;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.illumination.Light;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SmartHomeBuilderTest {

    @Test
    void createSmartHomeTest() {
        // create lights list
        List<Light> lights1 = new ArrayList<>();
        lights1.add(new Light());
        lights1.add(new Light());

        List<Light> lights2 = new ArrayList<>();
        lights2.add(new Light());

        // create rooms
        Room room1 = new Room("test1", lights1);
        Room room2 = new Room("test2", lights2);

        // create smartHome
        SmartHome smartHome = new SmartHomeBuilder()
                .addRoom(room1)
                .addRoom(room2)
                .create();

        assertNotNull(smartHome.getRooms());

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