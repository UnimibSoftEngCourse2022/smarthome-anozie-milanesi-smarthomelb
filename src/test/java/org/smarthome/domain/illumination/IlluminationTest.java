package org.smarthome.domain.illumination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IlluminationTest {

    private List<Light> lights;
    private Illumination illumination;

    @BeforeEach
    void setUp() {
        // create lights
        lights = new ArrayList<>();
        lights.add(new Light());
        lights.add(new Light());
        lights.add(new Light());

        // create rooms
        illumination = new Illumination(lights);
    }

    @Test
    void illuminationTest() {
        assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }

        illumination.handle();
        assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOn.class, light.getLightState().getClass());
        }

        lights.get(0).handle();
        illumination.handle();
        assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }

        lights.get(0).handle();
        illumination.handle();
        assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOn.class, light.getLightState().getClass());
        }
    }

    @Test
    void illuminationActionListenerTest() {
        illumination.setIlluminationActionListener(illuminationState ->
                assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass()));
        illumination.handle();

        illumination.setIlluminationActionListener(illuminationState ->
                assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass()));
        illumination.handle();
    }

}