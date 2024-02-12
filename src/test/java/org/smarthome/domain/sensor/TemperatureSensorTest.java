package org.smarthome.domain.sensor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.simulation.RoomTemperatureSimulation;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureSensorTest {

    private RoomTemperatureSimulation roomTemperatureSimulation;
    private TemperatureSensor temperatureSensor;

    @BeforeEach
    void setUp() {
        roomTemperatureSimulation = new RoomTemperatureSimulation();
        temperatureSensor = new TemperatureSensor();
        temperatureSensor.setRoomTemperatureSimulation(roomTemperatureSimulation);
    }

    @Test
    void presenceSensorTest() throws InterruptedException {
        int target = 24;
        CountDownLatch latch = new CountDownLatch(1);

        temperatureSensor.addObserver(data -> {
            assertNotNull(data);
            assertEquals(roomTemperatureSimulation.getTemperature(), data);
            if (data == target) {
                latch.countDown();
            }
        });

        roomTemperatureSimulation.setTarget(target);
        latch.await();
    }

}