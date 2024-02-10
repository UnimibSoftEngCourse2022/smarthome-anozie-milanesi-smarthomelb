package org.smarthome.domain.protection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlarmTest {

    private Alarm alarm;

    @BeforeEach
    void setUp() {
        alarm = new Alarm(new Siren());
    }

    @Test
    void alarmTest() {
        assertNotNull(alarm);
        assertNotNull(alarm.getSiren());
        alarm.handle();
        assertEquals(Armed.class, alarm.getAlarmState().getClass());
        assertFalse(alarm.getSiren().isActive());
        alarm.emergency();
        assertTrue(alarm.getSiren().isActive());
        alarm.handle();
        assertEquals(Disarmed.class, alarm.getAlarmState().getClass());
        assertFalse(alarm.getSiren().isActive());
    }

}