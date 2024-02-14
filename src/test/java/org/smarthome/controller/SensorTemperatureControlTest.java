package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.sensor.TemperatureSensor;
import org.smarthome.domain.temperature.*;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.simulation.RoomTemperatureSimulationListener;
import org.smarthome.util.Constants;
import org.smarthome.util.CountDownLatchWaiter;
import org.smarthome.util.DebugLogger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class SensorTemperatureControlTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private Room room;
    private SensorTemperatureControl sensorTemperatureControl;
    private SmartHome smartHome;

    @BeforeEach
    public void setup() {
        room = new SmartHomeRoomBuilder("test1")
                .setAirConditioner(new AirConditioner())
                .setTemperatureSettings(new TemperatureSettings())
                .setTemperatureSensor(new TemperatureSensor())
                .create();

        sensorTemperatureControl = new SensorTemperatureControl(
                room.getTemperatureSensor(),
                room.getTemperatureControl()
        );

        /* - */

        smartHome = new SmartHomeBuilder()
                .addRoom(room)
                .create();
    }

    @Test
    void sensorTemperatureControlTest() throws InterruptedException {
        assertNotNull(room.getTemperatureSensor());
        assertNotNull(room.getTemperatureSimulation());
        assertNotNull(room.getTemperatureSettings());

        room.getTemperatureControl().setAutomationActive(true);

        final CountDownLatch latch = new CountDownLatch(1);

        room.getTemperatureSimulation().setTarget(30);
        room.getTemperatureSimulation().setTemperature(30);

        room.getTemperatureSimulation().setRoomTemperatureListener(new RoomTemperatureSimulationListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                logger.info("temperature change: " + temperature);
            }

            @Override
            public void onStopTemperatureChange() {
            }
        });

        room.getAirConditioner().addObserver(new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
                if (state.getClass().equals(AirConditionerOff.class)) {
                    latch.countDown();
                }
                logger.info("air conditioner state change: " + state.getClass().getSimpleName());
            }

            @Override
            public void onTemperatureChange(int temperature) {
                logger.info("air conditioner temperature change: " + temperature);
            }
        });

        sensorTemperatureControl.onDataChange(30);

        CountDownLatchWaiter.awaitLatch(latch);

        assertEquals(AirConditionerOff.class, room.getAirConditioner().getAirConditionerState().getClass());
        assertEquals(room.getTemperatureSettings().getIdealTemperature(), room.getAirConditioner().getTemperature());
    }

    /* - */

    @Test
    @Disabled("concurrent debug test")
    void sensorTemperatureControlConcurrentTest1() throws InterruptedException {
        assertNotNull(room.getTemperatureSensor());
        assertNotNull(room.getTemperatureSimulation());
        assertNotNull(room.getTemperatureSettings());

        room.getTemperatureControl().setAutomationActive(true);

        final CountDownLatch latch = new CountDownLatch(1);

        room.getTemperatureSimulation().setRoomTemperatureListener(new RoomTemperatureSimulationListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                logger.info("temperature change: " + temperature);
            }

            @Override
            public void onStopTemperatureChange() {
            }
        });

        room.getAirConditioner().addObserver(new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
                if (state.getClass().equals(AirConditionerOff.class)) {
                    latch.countDown();
                }
                logger.info("air conditioner state change: " + state.getClass().getSimpleName());
            }

            @Override
            public void onTemperatureChange(int temperature) {
                logger.info("air conditioner temperature change: " + temperature);
            }
        });

        room.getTemperatureSimulation().setTarget(
                room.getTemperatureSettings().getIdealTemperature() +
                        (room.getTemperatureSettings().getThreshold() * 2));

        CountDownLatchWaiter.awaitLatch(latch);

        assertEquals(AirConditionerOff.class, room.getAirConditioner().getAirConditionerState().getClass());
        assertEquals(room.getTemperatureSettings().getIdealTemperature(), room.getAirConditioner().getTemperature());
    }

    @Test
    @Disabled("concurrent debug test")
    void sensorTemperatureControlConcurrentTest2() throws InterruptedException {
        assertNotNull(room.getTemperatureSensor());
        assertNotNull(room.getTemperatureSimulation());
        assertNotNull(room.getTemperatureSettings());

        room.getTemperatureControl().setAutomationActive(true);
        int startTemperature = room.getTemperatureSettings().getIdealTemperature() +
                (room.getTemperatureSettings().getThreshold() * 2);

        room.getTemperatureSimulation().setTarget(startTemperature);
        room.getTemperatureSimulation().setTemperature(startTemperature);

        final CountDownLatch latch = new CountDownLatch(1);

        room.getTemperatureSimulation().setRoomTemperatureListener(new RoomTemperatureSimulationListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                logger.info("temperature change: " + temperature);
            }

            @Override
            public void onStopTemperatureChange() {
            }
        });

        room.getAirConditioner().addObserver(new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
                logger.info("air conditioner state change: " + state.getClass().getSimpleName());
            }

            @Override
            public void onTemperatureChange(int temperature) {
                if (temperature == room.getTemperatureSettings().getIdealTemperature() - 2) {
                    latch.countDown();
                }
                logger.info("air conditioner temperature change: " + temperature);
            }
        });

        Thread.sleep(Constants.temperatureChangeMsDuration() * 3L);
        room.getTemperatureControl().decreaseTemperature();
        room.getTemperatureControl().decreaseTemperature();

        CountDownLatchWaiter.awaitLatch(latch);

        assertEquals(AirConditionerOn.class, room.getAirConditioner().getAirConditionerState().getClass());
        assertNotEquals(room.getTemperatureSettings().getIdealTemperature(), room.getAirConditioner().getTemperature());
    }

}