package org.smarthome.controller;

import org.junit.jupiter.api.Test;
import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.illumination.LightOff;
import org.smarthome.domain.illumination.LightOn;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LightControlTest {
    @Test
    void lightControlTest() {
        // create light list
        List<Light> lights1 = new ArrayList<>();
        lights1.add(new Light());
        lights1.add(new Light());
        lights1.add(new Light());

        // create room
        Room room1 = new Room("test1", lights1);

        LightControl controller = room1.getLightController();

        assertEquals(lights1.get(0).getLightState().getClass(), LightOff.class);
        assertEquals(lights1.get(1).getLightState().getClass(), LightOff.class);
        assertEquals(lights1.get(2).getLightState().getClass(), LightOff.class);

        //turn lights on
        controller.handleLights();

        assertEquals(lights1.get(0).getLightState().getClass(), LightOn.class);
        assertEquals(lights1.get(1).getLightState().getClass(), LightOn.class);
        assertEquals(lights1.get(2).getLightState().getClass(), LightOn.class);

        //turn lights off
        controller.handleLights();

        assertEquals(lights1.get(0).getLightState().getClass(), LightOff.class);
        assertEquals(lights1.get(1).getLightState().getClass(), LightOff.class);
        assertEquals(lights1.get(2).getLightState().getClass(), LightOff.class);

    }


}