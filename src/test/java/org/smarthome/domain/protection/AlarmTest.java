package org.smarthome.domain.protection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.exception.WrongSecurityPinException;
import org.smarthome.listener.AlarmListener;
import org.smarthome.util.Constants;
import org.smarthome.util.CountDownLatchWaiter;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class AlarmTest {

    private Alarm alarm;

    @BeforeEach
    void setUp() {
        alarm = new Alarm(new Siren(), new EmergencyService("112"));
    }

    @Test
    void alarmTest() {
        assertNotNull(alarm);
        assertNotNull(alarm.getSiren());
        alarm.handle(Constants.securityPin());
        assertEquals(Armed.class, alarm.getAlarmState().getClass());
        assertFalse(alarm.getSiren().isActive());
        alarm.emergency();
        assertTrue(alarm.getSiren().isActive());
        alarm.handle(Constants.securityPin());
        assertEquals(Disarmed.class, alarm.getAlarmState().getClass());
        assertFalse(alarm.getSiren().isActive());
    }

    @Test
    void alarmErrorTest() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(3);

        alarm.addObserver(new AlarmListener() {
            @Override
            public void onChangeState(AlarmState state) {
                assertNotNull(state);
                assertEquals(Armed.class, state.getClass());
                latch.countDown();
            }

            @Override
            public void onWrongSecurityPinException(WrongSecurityPinException e) {
                assertNotNull(e);
                assertEquals(Constants.wrongSecurityPinMessage(), e.getMessage());
                latch.countDown();
            }
        });

        alarm.handle("wrong");
        alarm.handle(null);
        alarm.handle(Constants.securityPin());

        CountDownLatchWaiter.awaitLatch(latch);
    }


}