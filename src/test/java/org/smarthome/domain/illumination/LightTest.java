package org.smarthome.domain.illumination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.listener.LightActionListener;

import static org.junit.jupiter.api.Assertions.*;

class LightTest {

    private Light light;

    @BeforeEach
    void setUp() {
        // create light
        light = new Light("Light");
    }

    @Test
    void lightTest() {
        assertNotNull(light.getName());
        assertEquals(LightOff.class, light.getLightState().getClass());
        light.handle();
        assertEquals(LightOn.class, light.getLightState().getClass());
        assertTrue(light.isOn());
        light.handle();
        assertEquals(LightOff.class, light.getLightState().getClass());
        assertFalse(light.isOn());
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