package org.smarthome.domain.illumination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LightTest {

    private Light light;

    @BeforeEach
    void setUp() {
        // create light
        light = new Light();
    }

    @Test
    void lightTest() {
        assertEquals(LightOff.class, light.getLightState().getClass());
        light.handle();
        assertEquals(LightOn.class, light.getLightState().getClass());
        light.handle();
        assertEquals(LightOff.class, light.getLightState().getClass());
    }

    @Test
    void lightActionListenerTest() {
        LightActionListener listener = lightState ->
                assertEquals(LightOn.class, light.getLightState().getClass());

        light.addObserver(listener);
        light.handle();
        light.removeObserver(listener);

        listener = lightState ->
                assertEquals(LightOff.class, light.getLightState().getClass());
        light.addObserver(listener);
        light.handle();
    }

}