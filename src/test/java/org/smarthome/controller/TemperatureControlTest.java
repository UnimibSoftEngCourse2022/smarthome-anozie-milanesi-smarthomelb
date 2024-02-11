package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.domain.temperature.AirConditioner;
import org.smarthome.domain.temperature.AirConditionerOn;
import org.smarthome.domain.temperature.AirConditionerState;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.simulation.RoomTemperatureSimulationListener;
import org.smarthome.simulation.RoomTemperatureSimulation;
import org.smarthome.util.Constants;
import org.smarthome.util.DebugLogger;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureControlTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private AirConditioner airConditioner;
    private RoomTemperatureSimulation roomTemperatureSimulation;
    private TemperatureControl temperatureControl;

    @BeforeEach
    void setUp() {
        airConditioner = new AirConditioner();
        roomTemperatureSimulation = new RoomTemperatureSimulation();
        airConditioner.setRoomTemperatureSimulation(roomTemperatureSimulation);
        temperatureControl = new TemperatureControl(airConditioner);
    }

    @Test
    void airConditionerTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

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
        latch.await();
    }

}