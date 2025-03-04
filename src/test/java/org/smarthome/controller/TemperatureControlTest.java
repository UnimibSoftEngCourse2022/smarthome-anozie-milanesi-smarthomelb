package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.smarthome.domain.temperature.*;
import org.smarthome.exception.FieldOutOfRangeException;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.listener.TemperaturePreferenceListener;
import org.smarthome.simulation.RoomTemperatureSimulationListener;
import org.smarthome.simulation.RoomTemperatureSimulation;
import org.smarthome.util.Constants;
import org.smarthome.util.CountDownLatchWaiter;
import org.smarthome.util.DebugLogger;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureControlTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private TemperaturePreference temperaturePreference;
    private AirConditioner airConditioner;
    private RoomTemperatureSimulation roomTemperatureSimulation;
    private TemperatureControl temperatureControl;

    @BeforeEach
    void setUp() {
        temperaturePreference = new TemperaturePreference();
        airConditioner = new AirConditioner();
        roomTemperatureSimulation = new RoomTemperatureSimulation();
        airConditioner.setRoomTemperatureSimulation(roomTemperatureSimulation);
        temperatureControl = new TemperatureControl(temperaturePreference, airConditioner);
    }

    @Test
    void airConditionerTest() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        roomTemperatureSimulation.setRoomTemperatureListener(new RoomTemperatureSimulationListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                if (temperature == Constants.defaultIdealTemperature() - 1) {
                    latch.countDown();
                }
                logger.info("temperature change: " + temperature);
            }

            @Override
            public void onStopTemperatureChange() {

            }
        });

        airConditioner.addObserver(new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
                assertEquals(AirConditionerOn.class, state.getClass());
            }

            @Override
            public void onTemperatureChange(int temperature) {
                assertTrue(airConditioner.isOn());
            }
        });

        temperatureControl.handleAirConditioner();
        temperatureControl.increaseTemperature();
        temperatureControl.increaseTemperature();
        temperatureControl.increaseTemperature();

        temperatureControl.decreaseTemperature();
        temperatureControl.decreaseTemperature();
        temperatureControl.decreaseTemperature();
        temperatureControl.decreaseTemperature();

        CountDownLatchWaiter.awaitLatch(latch);
    }

    @Test
    void temperatureHandleAutomationTest() throws InterruptedException {
        int idealTemperatureExpected = 21;
        int thresholdExpected = 4;

        final CountDownLatch latch = new CountDownLatch(2);

        temperaturePreference.addObserver(new TemperaturePreferenceListener() {
            @Override
            public void onIdealTemperatureChange(int idealTemperature) {
                assertEquals(idealTemperatureExpected, idealTemperature);
                latch.countDown();
            }

            @Override
            public void onThresholdChange(int threshold) {
                assertEquals(thresholdExpected, threshold);
                latch.countDown();
            }

            @Override
            public void onFieldOutOfRangeException(FieldOutOfRangeException e) {
            }
        });

        temperaturePreference.setIdealTemperature(idealTemperatureExpected);
        temperaturePreference.setThreshold(thresholdExpected);

        CountDownLatchWaiter.awaitLatch(latch);

        final CountDownLatch latch1 = new CountDownLatch(1);

        roomTemperatureSimulation.setTemperature(28);
        roomTemperatureSimulation.setTarget(28);
        roomTemperatureSimulation.setRoomTemperatureListener(new RoomTemperatureSimulationListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                if (temperature == temperaturePreference.getIdealTemperature()) {
                    latch1.countDown();
                }
                logger.info("temperature change: " + temperature);
            }

            @Override
            public void onStopTemperatureChange() {
            }
        });

        airConditioner.addObserver(new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
                logger.info("air conditioner state change: " + state.getClass().getSimpleName());
            }

            @Override
            public void onTemperatureChange(int temperature) {
                logger.info("air conditioner temperature change: " + temperature);
            }
        });

        temperatureControl.handleAutomation(roomTemperatureSimulation.getTemperature());

        CountDownLatchWaiter.awaitLatch(latch1);

        assertEquals(AirConditionerOn.class, airConditioner.getAirConditionerState().getClass());
        assertEquals(temperaturePreference.getIdealTemperature(), airConditioner.getTemperature());

        temperatureControl.handleAutomation(roomTemperatureSimulation.getTemperature());
        assertEquals(AirConditionerOff.class, airConditioner.getAirConditionerState().getClass());
    }

    @Test
    void temperatureControlDoesNotThrowTest() {
        assertDoesNotThrow(() -> {
            TemperatureControl temperatureControl =
                    new TemperatureControl(null, null);
            temperatureControl.handleAirConditioner();
            temperatureControl.increaseTemperature();
            temperatureControl.decreaseTemperature();
            temperatureControl.handleAutomation(null);
            temperatureControl.handleAutomation(30);
        });
    }

}