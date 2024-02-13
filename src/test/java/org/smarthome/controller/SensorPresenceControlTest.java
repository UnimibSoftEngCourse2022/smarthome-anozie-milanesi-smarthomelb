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
import org.smarthome.domain.protection.EmergencyService;
import org.smarthome.domain.protection.Siren;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.listener.EmergencyServiceListener;

import java.util.List;
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
                .setPresenceSensor(new PresenceSensor())
                .create();

        smartHome = new SmartHomeBuilder()
                .addRoom(room)
                .setProtection(new Siren(), new EmergencyService("112"))
                .create();
    }

    @Test
    void presenceSensorControlIlluminationTest1() throws InterruptedException {
        assertNotNull(room.getPresenceSensor());

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
    void presenceSensorControlProtectionTest1() throws InterruptedException {
        room.getIlluminationControl().setAutomationActive(true);

        CountDownLatch latch = new CountDownLatch(1);
        List<Room> rooms = smartHome.getRooms();

        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();
            illumination.handle();
            assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
        }

        assertEquals(Disarmed.class, smartHome.getAlarm().getAlarmState().getClass());
        smartHome.getProtectionControl().handleAlarm();
        assertEquals(Armed.class, smartHome.getAlarm().getAlarmState().getClass());

        smartHome.getAlarm().getSiren().addObserver(active -> {
            assertTrue(smartHome.getAlarm().getSiren().isActive());
            latch.countDown();
        });

        room.getPresenceSimulation().setPresence(true);
        latch.await();
    }

    @Test
    void presenceSensorControlProtectionTest2() throws InterruptedException {
        room.getIlluminationControl().setAutomationActive(true);

        List<Room> rooms = smartHome.getRooms();
        int count = 2;

        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();
            illumination.handle();
            assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
            for (Light light : illumination.getLights()) {
                assertEquals(LightOn.class, light.getLightState().getClass());
                count++;
            }
            count++;
        }

        CountDownLatch latch = new CountDownLatch(count);

        assertEquals(Disarmed.class, smartHome.getAlarm().getAlarmState().getClass());
        smartHome.getProtectionControl().handleAlarm();
        assertEquals(Armed.class, smartHome.getAlarm().getAlarmState().getClass());

        smartHome.getAlarm().getEmergencyService().addObserver(emergencyNumber -> latch.countDown());
        smartHome.getAlarm().getSiren().addObserver(active -> latch.countDown());
        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();

            illumination.addObserver(state -> latch.countDown());
            for (Light light : illumination.getLights()) {
                light.addObserver(state -> latch.countDown());
            }
        }

        room.getPresenceSimulation().setPresence(true);
        latch.await();

        assertTrue(smartHome.getAlarm().getSiren().isActive());
        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();
            assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass());
            for (Light light : illumination.getLights()) {
                assertEquals(LightOff.class, light.getLightState().getClass());
            }
        }
    }

    @Test
    void presenceSensorControlProtectionTest3() throws InterruptedException {
        room.getIlluminationControl().setAutomationActive(true);

        List<Room> rooms = smartHome.getRooms();
        CountDownLatch latch = new CountDownLatch(2);

        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();
            illumination.handle();
        }

        assertEquals(Disarmed.class, smartHome.getAlarm().getAlarmState().getClass());
        smartHome.getProtectionControl().handleAlarm();
        assertEquals(Armed.class, smartHome.getAlarm().getAlarmState().getClass());

        smartHome.getAlarm().getSiren().addObserver(active -> {
            if (active) {
                smartHome.getProtectionControl().handleAlarm();
            }
            latch.countDown();
        });

        room.getPresenceSimulation().setPresence(true);
        latch.await();

        assertFalse(smartHome.getAlarm().getSiren().isActive());
        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();
            assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
            for (Light light : illumination.getLights()) {
                assertEquals(LightOn.class, light.getLightState().getClass());
            }
        }
    }

}