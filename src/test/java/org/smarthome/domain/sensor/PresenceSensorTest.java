package org.smarthome.domain.sensor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.simulation.RoomPresenceSimulation;
import org.smarthome.util.CountDownLatchWaiter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class PresenceSensorTest {

    private RoomPresenceSimulation roomPresenceSimulation;
    private PresenceSensor presenceSensor;

    @BeforeEach
    void setUp() {
        roomPresenceSimulation = new RoomPresenceSimulation();
        presenceSensor = new PresenceSensor();
        presenceSensor.setRoomPresenceSimulation(roomPresenceSimulation);
    }

    @Test
    void presenceSensorTest() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        presenceSensor.addObserver(data -> {
            assertNotNull(data);
            assertEquals(roomPresenceSimulation.isPresence(), data);
            latch.countDown();
        });

        roomPresenceSimulation.setPresence(true);

        CountDownLatchWaiter.awaitLatch(latch);
    }

}