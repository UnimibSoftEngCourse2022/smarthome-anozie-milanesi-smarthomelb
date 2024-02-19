package org.smarthome.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.domain.illumination.*;
import org.smarthome.listener.AutomationListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IlluminationControlTest {

    private List<Light> lights;
    private Illumination illumination;
    private IlluminationControl illuminationControl;

    @BeforeEach
    void setUp() {
        lights = new ArrayList<>();
        lights.add(new Light("Light"));
        lights.add(new Light("Light"));
        lights.add(new Light("Light"));
        illumination = new Illumination(lights);

        illuminationControl = new IlluminationControl(illumination);
    }

    @Test
    void illuminationControlTest() {
        assertNotNull(illumination);

        illuminationControl.handleSingleLight(lights.get(0));
        assertEquals(LightOn.class, lights.get(0).getLightState().getClass());

        illuminationControl.handleIllumination();
        assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOn.class, light.getLightState().getClass());
        }

        illuminationControl.handleSingleLight(lights.get(1));
        assertEquals(LightOff.class, lights.get(1).getLightState().getClass());

        illuminationControl.handleIllumination();
        assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }
    }

    @Test
    void illuminationHandleAutomationTest() {
        assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }

        illuminationControl.handleAutomation(true);
        assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOn.class, light.getLightState().getClass());
        }

        illuminationControl.handleAutomation(false);
        assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass());
        for (Light light : illumination.getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }
    }

    @Test
    void automationObserverTest1() {
        illuminationControl.handleAutomation(true);
        illuminationControl.addObserver(automationActive -> {
            assertFalse(automationActive);
            assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
            for (Light light : illumination.getLights()) {
                assertEquals(LightOn.class, light.getLightState().getClass());
            }
        });
        illuminationControl.handleIllumination();
    }

    @Test
    void automationObserverTest2() {
        illuminationControl.addObserver(Assertions::assertTrue);
        illuminationControl.setAutomationActive(true);
    }


}