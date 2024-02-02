package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LightControlTest {

    private Room room;
    private List<Light> lights;
    private LightControl lightControl;

    @BeforeEach
    void setUp() {
        // create lights
        lights = new ArrayList<>();
        lights.add(new Light());
        lights.add(new Light());
        lights.add(new Light());

        // create room
        room = new Room("test1", lights);
        lightControl = room.getLightControl();
    }

    @Test
    void lightControlTest() {
        assertNotNull(room.getIllumination());

        lightControl.handleSingleLight(lights.get(0));
        assertEquals(LightOn.class, lights.get(0).getLightState().getClass());

        lightControl.handleIllumination();
        assertEquals(IlluminationOn.class, room.getIllumination().getIlluminationState().getClass());
        for (Light light : room.getIllumination().getLights()) {
            assertEquals(LightOn.class, light.getLightState().getClass());
        }

        lightControl.handleSingleLight(lights.get(1));
        assertEquals(LightOff.class, lights.get(1).getLightState().getClass());

        lightControl.handleIllumination();
        assertEquals(IlluminationOff.class, room.getIllumination().getIlluminationState().getClass());
        for (Light light : room.getIllumination().getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }
    }


}