package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.illumination.*;
import org.smarthome.domain.protection.Armed;
import org.smarthome.domain.protection.Disarmed;
import org.smarthome.domain.protection.Siren;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class SensorPresenceControlTest {

    private Room room;
    private SmartHome smartHome;

    @BeforeEach
    public void setup() {
        room = new SmartHomeRoomBuilder("test1")
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .create();

        smartHome = new SmartHomeBuilder()
                .addRoom(room)
                .setVacuumChargingStationPosition(room)
                .setSiren(new Siren())
                .create();
    }

    @Test
    void presenceSensorControlIlluminationTest1() throws InterruptedException {
        assertNotNull(room.getPresenceSensor());
        assertNotNull(room.getSensorPresenceControl());

        room.getIlluminationControl().setAutomationActive(true);

        assertEquals(IlluminationOff.class, room.getIllumination().getIlluminationState().getClass());
        for (Light light : room.getIllumination().getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }

        CountDownLatch latch = new CountDownLatch(3);

        for (Light light : room.getIllumination().getLights()) {
            light.addObserver(lightState -> latch.countDown());
        }

        room.getPresenceSimulation().setPresence(true);
        latch.await();

        assertEquals(IlluminationOn.class, room.getIllumination().getIlluminationState().getClass());
        for (Light light : room.getIllumination().getLights()) {
            assertEquals(LightOn.class, light.getLightState().getClass());
        }
    }

    @Test
    void presenceSensorControlIlluminationTest2() throws InterruptedException {
        room.getIlluminationControl().setAutomationActive(true);

        CountDownLatch latch = new CountDownLatch(3);

        for (Light light : room.getIllumination().getLights()) {
            light.addObserver(lightState -> latch.countDown());
        }

        room.getPresenceSimulation().setPresence(true);
        latch.await();

        CountDownLatch latch1 = new CountDownLatch(3);

        for (Light light : room.getIllumination().getLights()) {
            light.addObserver(lightState -> latch1.countDown());
        }

        room.getPresenceSimulation().setPresence(false);
        latch1.await();

        assertEquals(IlluminationOff.class, room.getIllumination().getIlluminationState().getClass());
        for (Light light : room.getIllumination().getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }
    }

    @Test
    void presenceSensorControlProtectionTest() {
        assertEquals(Disarmed.class, smartHome.getAlarm().getAlarmState().getClass());
        smartHome.getProtectionControl().handleAlarm();
        assertEquals(Armed.class, smartHome.getAlarm().getAlarmState().getClass());

        smartHome.getAlarm().getSiren().addObserver(active -> {
            assertTrue(smartHome.getAlarm().getSiren().isActive());
            smartHome.getProtectionControl().deactivateSiren();
            assertFalse(smartHome.getAlarm().getSiren().isActive());
        });

        room.getPresenceSimulation().setPresence(true);
    }

}