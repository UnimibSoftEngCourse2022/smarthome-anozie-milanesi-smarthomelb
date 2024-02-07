package org.smarthome.simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.smarthome.simulation.RoomTemperatureListener;
import org.smarthome.simulation.RoomTemperatureSimulation;
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
        CountDownLatch latch = new CountDownLatch(1);

        simulationTemperature.setRoomTemperatureListener(new RoomTemperatureListener() {
            @Override
            public void onTemperatureChange(int temperature) {
            }

            @Override
            public void onTargetTemperatureReached() {
                latch.countDown();
            }

            @Override
            public void onStopTemperatureTargetGoal() {
            }
        });

        int target = 24;
        simulationTemperature.startTemperatureChange(target);
        latch.await();
        assertEquals(target, simulationTemperature.getTemperature());
    }

    @Test
    void startTemperatureChangeTest2() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        simulationTemperature.setRoomTemperatureListener(new RoomTemperatureListener() {
            @Override
            public void onTemperatureChange(int temperature) {
            }

            @Override
            public void onTargetTemperatureReached() {
                latch.countDown();
            }

            @Override
            public void onStopTemperatureTargetGoal() {
            }
        });

        int target = 16;
        simulationTemperature.startTemperatureChange(target);
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
            public void onTargetTemperatureReached() {
            }

            @Override
            public void onStopTemperatureTargetGoal() {
                latch.countDown();
            }
        });

        int target = 30;
        simulationTemperature.startTemperatureChange(target);
        Thread.sleep(TEMPERATURE_CHANGE_MS_DURATION * 2);
        simulationTemperature.stopTemperatureChange();
        latch.await();
        assertNotEquals(target, simulationTemperature.getTemperature());
    }

    @Test
    @Disabled("concurrent debug test")
    void startTemperatureChangeConcurrentTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        simulationTemperature.setRoomTemperatureListener(new RoomTemperatureListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                logger.info("temperature change to" + temperature);
            }

            @Override
            public void onTargetTemperatureReached() {
                logger.info("temperature target reached");
                latch.countDown();
            }

            @Override
            public void onStopTemperatureTargetGoal() {
            }
        });

        int target = 24;
        simulationTemperature.startTemperatureChange(target);
        Thread.sleep(TEMPERATURE_CHANGE_MS_DURATION * 2);
        target = 18;
        simulationTemperature.startTemperatureChange(target);
        latch.await();
        assertEquals(target, simulationTemperature.getTemperature());
    }

    @Test
    @Disabled("concurrent debug test")
    void stopTemperatureChangeConcurrentTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        simulationTemperature.setRoomTemperatureListener(new RoomTemperatureListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                logger.info("temperature change to" + temperature);
            }

            @Override
            public void onTargetTemperatureReached() {
            }

            @Override
            public void onStopTemperatureTargetGoal() {
                logger.info("temperature stop to change");
                latch.countDown();
            }
        });

        int target = 30;
        simulationTemperature.startTemperatureChange(target);
        Thread.sleep(TEMPERATURE_CHANGE_MS_DURATION * 2);
        simulationTemperature.stopTemperatureChange();
        latch.await();
        assertNotEquals(target, simulationTemperature.getTemperature());
    }

}