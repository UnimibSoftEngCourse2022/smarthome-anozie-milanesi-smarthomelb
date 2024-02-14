package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.illumination.*;
import org.smarthome.domain.protection.*;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.util.CountDownLatchWaiter;
import org.smarthome.util.DebugLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class SensorPresenceControlTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private Room room;
    private List<Room> rooms;
    private Alarm alarm;
    private SensorPresenceControl sensorPresenceControl;
    private ProtectionControl protectionControl;
    private SmartHome smartHome;

    @BeforeEach
    public void setup() {
        room = new SmartHomeRoomBuilder("test1")
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .setPresenceSensor(new PresenceSensor())
                .create();

        Room room1 = new SmartHomeRoomBuilder("test2")
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .create();

        rooms = new ArrayList<>();
        rooms.add(room);
        rooms.add(room1);

        alarm = new Alarm(new Siren(), new EmergencyService("112"));

        protectionControl = new ProtectionControl(alarm, rooms);

        sensorPresenceControl = new SensorPresenceControl(
                room.getPresenceSensor(),
                protectionControl,
                room.getIlluminationControl()
        );

        /* - */

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

        final CountDownLatch latch = new CountDownLatch(4);

        room.getIllumination().addObserver(state -> {
            logger.info("illumination state change: " + state.getClass().getSimpleName());
            assertEquals(IlluminationOn.class, state.getClass());
            latch.countDown();
        });
        for (Light light : room.getIllumination().getLights()) {
            light.addObserver(state -> {
                logger.info("light state change: " + state.getClass().getSimpleName());
                assertEquals(LightOn.class, state.getClass());
                latch.countDown();
            });
        }

        sensorPresenceControl.onDataChange(true);

        CountDownLatchWaiter.awaitLatch(latch);
    }

    @Test
    void presenceSensorControlIlluminationTest2() throws InterruptedException {
        room.getIlluminationControl().setAutomationActive(true);

        final CountDownLatch latch = new CountDownLatch(4);
        final CountDownLatch latch1 = new CountDownLatch(4);

        room.getIllumination().addObserver(state -> {
            logger.info("illumination state change: " + state.getClass().getSimpleName());
            if (state.getClass().equals(IlluminationOn.class)) {
                latch.countDown();
            } else {
                latch1.countDown();
            }
        });
        for (Light light : room.getIllumination().getLights()) {
            light.addObserver(state -> {
                logger.info("light state change: " + state.getClass().getSimpleName());
                if (state.getClass().equals(LightOn.class)) {
                    latch.countDown();
                } else {
                    latch1.countDown();
                }
            });
        }

        sensorPresenceControl.onDataChange(true);

        CountDownLatchWaiter.awaitLatch(latch);

        sensorPresenceControl.onDataChange(false);

        CountDownLatchWaiter.awaitLatch(latch1);
    }

    @Test
    void presenceSensorControlProtectionTest1() throws InterruptedException {
        assertEquals(Disarmed.class, alarm.getAlarmState().getClass());
        protectionControl.handleAlarm();
        assertEquals(Armed.class, alarm.getAlarmState().getClass());

        room.getIlluminationControl().setAutomationActive(true);

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

        final CountDownLatch latch = new CountDownLatch(count);

        alarm.getEmergencyService().addObserver(emergencyNumber -> {
            logger.info("emergency call to: " + emergencyNumber);
            latch.countDown();
        });
        alarm.getSiren().addObserver(active -> {
            logger.info("siren active change: " + active);
            assertTrue(alarm.getSiren().isActive());
            latch.countDown();
        });

        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();

            illumination.addObserver(state -> {
                logger.info("illumination state change: " + state.getClass().getSimpleName());
                assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass());
                latch.countDown();
            });
            for (Light light : illumination.getLights()) {
                light.addObserver(state -> {
                    logger.info("light state change: " + state.getClass().getSimpleName());
                    assertEquals(LightOff.class, light.getLightState().getClass());
                    latch.countDown();
                });
            }
        }

        sensorPresenceControl.onDataChange(true);

        CountDownLatchWaiter.awaitLatch(latch);
    }

    @Test
    void presenceSensorControlProtectionTest2() throws InterruptedException {
        assertEquals(Disarmed.class, alarm.getAlarmState().getClass());
        protectionControl.handleAlarm();
        assertEquals(Armed.class, alarm.getAlarmState().getClass());

        room.getIlluminationControl().setAutomationActive(true);

        final CountDownLatch latch = new CountDownLatch(2);

        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();
            illumination.handle();
            assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
            for (Light light : illumination.getLights()) {
                assertEquals(LightOn.class, light.getLightState().getClass());
            }
        }

        alarm.getSiren().addObserver(active -> {
            if (active) {
                protectionControl.handleAlarm();
            }
            latch.countDown();
        });

        sensorPresenceControl.onDataChange(true);

        CountDownLatchWaiter.awaitLatch(latch);

        assertFalse(alarm.getSiren().isActive());
    }

    /* - */

    @Test
    @Disabled("concurrent debug test")
    void presenceSensorControlIlluminationConcurrentTest1() throws InterruptedException {
        assertNotNull(room.getPresenceSensor());

        room.getIlluminationControl().setAutomationActive(true);

        assertEquals(IlluminationOff.class, room.getIllumination().getIlluminationState().getClass());
        for (Light light : room.getIllumination().getLights()) {
            assertEquals(LightOff.class, light.getLightState().getClass());
        }

        final CountDownLatch latch = new CountDownLatch(4);

        room.getIllumination().addObserver(state -> {
            logger.info("illumination state change: " + state.getClass().getSimpleName());
            assertEquals(IlluminationOn.class, state.getClass());
            latch.countDown();
        });
        for (Light light : room.getIllumination().getLights()) {
            light.addObserver(state -> {
                logger.info("light state change: " + state.getClass().getSimpleName());
                assertEquals(LightOn.class, state.getClass());
                latch.countDown();
            });
        }

        room.getPresenceSimulation().setPresence(true);

        CountDownLatchWaiter.awaitLatch(latch);
    }

    @Test
    @Disabled("concurrent debug test")
    void presenceSensorControlIlluminationConcurrentTest2() throws InterruptedException {
        room.getIlluminationControl().setAutomationActive(true);

        final CountDownLatch latch = new CountDownLatch(4);
        final CountDownLatch latch1 = new CountDownLatch(4);

        room.getIllumination().addObserver(state -> {
            logger.info("illumination state change: " + state.getClass().getSimpleName());
            if (state.getClass().equals(IlluminationOn.class)) {
                latch.countDown();
            } else {
                latch1.countDown();
            }
        });
        for (Light light : room.getIllumination().getLights()) {
            light.addObserver(state -> {
                logger.info("light state change: " + state.getClass().getSimpleName());
                if (state.getClass().equals(LightOn.class)) {
                    latch.countDown();
                } else {
                    latch1.countDown();
                }
            });
        }

        room.getPresenceSimulation().setPresence(true);

        CountDownLatchWaiter.awaitLatch(latch);

        Thread.sleep(1);
        room.getPresenceSimulation().setPresence(false);

        CountDownLatchWaiter.awaitLatch(latch1);
    }

    @Test
    @Disabled("concurrent debug test")
    void presenceSensorControlProtectionConcurrentTest1() throws InterruptedException {
        assertEquals(Disarmed.class, smartHome.getAlarm().getAlarmState().getClass());
        smartHome.getProtectionControl().handleAlarm();
        assertEquals(Armed.class, smartHome.getAlarm().getAlarmState().getClass());

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

        final CountDownLatch latch = new CountDownLatch(count);

        smartHome.getAlarm().getEmergencyService().addObserver(emergencyNumber -> {
            logger.info("emergency call to: " + emergencyNumber);
            latch.countDown();
        });
        smartHome.getAlarm().getSiren().addObserver(active -> {
            logger.info("siren active change: " + active);
            assertTrue(smartHome.getAlarm().getSiren().isActive());
            latch.countDown();
        });

        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();

            illumination.addObserver(state -> {
                logger.info("illumination state change: " + state.getClass().getSimpleName());
                assertEquals(IlluminationOff.class, illumination.getIlluminationState().getClass());
                latch.countDown();
            });
            for (Light light : illumination.getLights()) {
                light.addObserver(state -> {
                    logger.info("light state change: " + state.getClass().getSimpleName());
                    assertEquals(LightOff.class, light.getLightState().getClass());
                    latch.countDown();
                });
            }
        }

        room.getPresenceSimulation().setPresence(true);

        CountDownLatchWaiter.awaitLatch(latch);
    }

    @Test
    @Disabled("concurrent debug test")
    void presenceSensorControlProtectionConcurrentTest2() throws InterruptedException {
        assertEquals(Disarmed.class, smartHome.getAlarm().getAlarmState().getClass());
        smartHome.getProtectionControl().handleAlarm();
        assertEquals(Armed.class, smartHome.getAlarm().getAlarmState().getClass());

        room.getIlluminationControl().setAutomationActive(true);

        List<Room> rooms = smartHome.getRooms();
        final CountDownLatch latch = new CountDownLatch(2);

        for (Room room : rooms) {
            Illumination illumination = room.getIllumination();
            illumination.handle();
            assertEquals(IlluminationOn.class, illumination.getIlluminationState().getClass());
            for (Light light : illumination.getLights()) {
                assertEquals(LightOn.class, light.getLightState().getClass());
            }
        }

        smartHome.getAlarm().getSiren().addObserver(active -> {
            if (active) {
                smartHome.getProtectionControl().handleAlarm();
            }
            latch.countDown();
        });

        room.getPresenceSimulation().setPresence(true);

        CountDownLatchWaiter.awaitLatch(latch);

        assertFalse(smartHome.getAlarm().getSiren().isActive());
    }

}