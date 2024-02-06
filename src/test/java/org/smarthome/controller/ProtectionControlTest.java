package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.domain.protection.Armed;
import org.smarthome.domain.protection.Disarmed;
import org.smarthome.domain.protection.Siren;

import static org.junit.jupiter.api.Assertions.*;

class ProtectionControlTest {

    private Alarm alarm;
    private Siren siren;
    private ProtectionControl protectionController;

    @BeforeEach
    public void setUp() {
        siren = new Siren();
        alarm = new Alarm(siren);
        protectionController = new ProtectionControl(alarm);
    }

    @Test
    public void activateDeactivateProtectionTest() {
        protectionController.handleAlarm();
        assertEquals(Armed.class, alarm.getAlarmState().getClass());

        protectionController.handleAlarm();
        assertEquals(Disarmed.class, alarm.getAlarmState().getClass());
    }

    @Test
    public void emergencySituationWithoutAlarmActivationTest() {
        protectionController.emergencySituation();
        assertFalse(siren.isActive());
    }

    @Test
    public void emergencySituationWithAlarmActivationTest() {
        protectionController.handleAlarm();
        protectionController.emergencySituation();
        assertTrue(siren.isActive());
        protectionController.deactivateSiren();
        assertFalse(siren.isActive());
    }

}