package org.smarthome.simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.smarthome.util.DebugLogger;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.smarthome.util.Constants.TEMPERATURE_CHANGE_MS_DURATION;

class RoomTemperatureSimulationTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private RoomTemperatureSimulation simulationTemperature;

    @BeforeEach
    void setUp() {
        simulationTemperature = new RoomTemperatureSimulation();
    }

    @Test
    void startTemperatureChangeTest1() throws InterruptedException {
        int target = 24;
        CountDownLatch latch = new CountDownLatch(1);

        simulationTemperature.setRoomTemperatureListener(new RoomTemperatureListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                if (temperature == target) {
                    latch.countDown();
                }
                logger.info("temperature change: " + temperature);
            }

            @Override
            public void onStopTemperatureChange() {
            }
        });

        simulationTemperature.setTarget(target);
        latch.await();
        assertEquals(target, simulationTemperature.getTemperature());
    }

    @Test
    void startTemperatureChangeTest2() throws InterruptedException {
        int target = 16;
        CountDownLatch latch = new CountDownLatch(1);

        simulationTemperature.setRoomTemperatureListener(new RoomTemperatureListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                if (temperature == target) {
                    latch.countDown();
                }
                logger.info("temperature change: " + temperature);
            }

            @Override
            public void onStopTemperatureChange() {

            }
        });

        simulationTemperature.setTarget(target);
        latch.await();
        assertEquals(target, simulationTemperature.getTemperature());
    }


    @Test
    void stopTemperatureChangeTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        simulationTemperature.setRoomTemperatureListener(new RoomTemperatureListener() {
            @Override
            public void onTemperatureChange(int temperature) {
            }

            @Override
            public void onStopTemperatureChange() {
                assertEquals(simulationTemperature.getTemperature(), simulationTemperature.getTarget());
                latch.countDown();
            }
        });

        int target = 30;
        simulationTemperature.setTarget(target);
        Thread.sleep(TEMPERATURE_CHANGE_MS_DURATION * 2);
        simulationTemperature.stopTemperatureChange();
        latch.await();
        assertNotEquals(target, simulationTemperature.getTemperature());
    }

    @Test
    @Disabled("concurrent debug test")
    void startTemperatureChangeConcurrentTest() throws InterruptedException {
        int targetFinal = 18;

        CountDownLatch latch = new CountDownLatch(1);

        simulationTemperature.setRoomTemperatureListener(new RoomTemperatureListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                if (temperature == targetFinal) {
                    latch.countDown();
                }
                logger.info("temperature change: " + temperature);
            }

            @Override
            public void onStopTemperatureChange() {
            }
        });

        int target = 24;
        simulationTemperature.setTarget(target);
        Thread.sleep(TEMPERATURE_CHANGE_MS_DURATION * 2);
        simulationTemperature.setTarget(targetFinal);
        latch.await();
        assertEquals(targetFinal, simulationTemperature.getTemperature());
    }

}