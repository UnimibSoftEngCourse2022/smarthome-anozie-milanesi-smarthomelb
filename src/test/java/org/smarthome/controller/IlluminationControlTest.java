package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IlluminationControlTest {

    private Room room;
    private IlluminationControl illuminationControl;

    @BeforeEach
    void setUp() {
        // create room
        room = new SmartHomeRoomBuilder("test1")
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .create();
        illuminationControl = room.getIlluminationControl();
    }

    @Test
    void illuminationControlTest() {
        List<Light> lights = room.getIllumination().getLights();
        assertNotNull(room.getIllumination());

        illuminationControl.handleSingleLight(lights.get(0));
        assertEquals(LightOn.class, lights.get(0).getLightState().getClass());

        illuminationControl.handleIllumination();
        assertEquals(IlluminationOn.class, room.getIllumination().getIlluminationState().getClass());
        for (Light light : room.getIllumination().getLights()) {
            assertEquals(LightOn.class, light.getLightState().getClass());
        }

        illuminationControl.handleSingleLight(lights.get(1));
        assertEquals(LightOff.class, lights.get(1).getLightState().getClass());

        illuminationControl.handleIllumination();
        assertEquals(IlluminationOff.class, room.getIllumination().getIlluminationState().getClass());
        for (Light light : room.getIllumination().getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }
    }


}