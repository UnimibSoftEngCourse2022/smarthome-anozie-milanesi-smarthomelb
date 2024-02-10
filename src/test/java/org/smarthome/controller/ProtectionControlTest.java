package org.smarthome.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.*;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.domain.protection.Armed;
import org.smarthome.domain.protection.Disarmed;
import org.smarthome.domain.protection.Siren;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProtectionControlTest {

    private List<Room> rooms;
    private Alarm alarm;
    private Siren siren;
    private ProtectionControl protectionController;

    @BeforeEach
    void setUp() {
        // create rooms
        Room room1 = new SmartHomeRoomBuilder("test1")
                .addLight(new Light())
                .addLight(new Light())
                .create();

        Room room2 = new SmartHomeRoomBuilder("test2")
                .addLight(new Light())
                .create();

        rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        siren = new Siren();
        alarm = new Alarm(siren);
        protectionController = new ProtectionControl(alarm, rooms);
    }

    @Test
    void activateDeactivateProtectionTest() {
        alarm.addObserver(Assertions::assertNotNull);

        protectionController.handleAlarm();
        assertEquals(Armed.class, alarm.getAlarmState().getClass());

        protectionController.handleAlarm();
        assertEquals(Disarmed.class, alarm.getAlarmState().getClass());
    }

    @Test
    void emergencySituationWithoutAlarmActivationTest() {
        protectionController.emergencySituation();
        assertFalse(siren.isActive());
    }

    @Test
    void emergencySituationWithAlarmActivationTest() {
        for (Room room : rooms) {
            room.getIlluminationControl().handleIllumination();
        }
        for (Room room : rooms) {
            assertEquals(IlluminationOn.class, room.getIllumination().getIlluminationState().getClass());
            for (Light light : room.getIllumination().getLights()) {
                assertEquals(LightOn.class, light.getLightState().getClass());
            }
        }

        protectionController.handleAlarm();
        protectionController.emergencySituation();
        assertTrue(siren.isActive());
        protectionController.deactivateSiren();
        assertFalse(siren.isActive());

        for (Room room : rooms) {
            assertEquals(IlluminationOff.class, room.getIllumination().getIlluminationState().getClass());
            for (Light light : room.getIllumination().getLights()) {
                assertEquals(LightOff.class, light.getLightState().getClass());
            }
        }
    }

    @Test
    void doesNotThrowCleaningTest() {
        assertDoesNotThrow(() -> {
            ProtectionControl protectionController = new ProtectionControl(null, rooms);
            protectionController.handleAlarm();
            assertFalse(protectionController.emergencySituation());
            protectionController.deactivateSiren();
        });
    }

}