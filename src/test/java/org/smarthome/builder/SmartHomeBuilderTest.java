package org.smarthome.builder;

import org.junit.jupiter.api.Test;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;

import static org.junit.jupiter.api.Assertions.*;

public class SmartHomeBuilderTest {

    @Test
    void createSmartHomeTest() {
        SmartHome smartHome = new SmartHomeBuilder()
                .addRoom(new Room("test1"))
                .addRoom(new Room("test2"))
                .create();
        assertNotNull(smartHome.getRooms());
        assertEquals(smartHome.getRooms().size(), 2);
    }

}