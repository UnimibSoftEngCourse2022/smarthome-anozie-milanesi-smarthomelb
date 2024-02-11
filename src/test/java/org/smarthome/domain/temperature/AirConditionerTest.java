package org.smarthome.domain.temperature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.simulation.RoomTemperatureSimulationListener;
import org.smarthome.simulation.RoomTemperatureSimulation;
import org.smarthome.util.Constants;
import org.smarthome.util.DebugLogger;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class AirConditionerTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private AirConditioner airConditioner;
    private RoomTemperatureSimulation roomTemperatureSimulation;

    @BeforeEach
    void setUp() {
        airConditioner = new AirConditioner();
        roomTemperatureSimulation = new RoomTemperatureSimulation();
        airConditioner.setRoomTemperatureSimulation(roomTemperatureSimulation);
    }

    @Test
    void isOnAirConditionerTest() {
        int expected = Constants.defaultIdealTemperature() + 1;
        airConditioner.addObserver(new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
            }

            @Override
            public void onTemperatureChange(int temperature) {
                assertTrue(airConditioner.isOn());
                assertEquals(expected, airConditioner.getTemperature());
            }
        });

        assertFalse(airConditioner.isOn());
        assertEquals(Constants.defaultIdealTemperature(), airConditioner.getTemperature());
        airConditioner.setTemperature(expected);
    }

    @Test
    void airConditionerTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        int expected = 21;

        roomTemperatureSimulation.setRoomTemperatureListener(new RoomTemperatureSimulationListener() {
            @Override
            public void onTemperatureChange(int temperature) {
                if (temperature == expected) {
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

        airConditioner.handle();
        airConditioner.setTemperature(12);
        Thread.sleep(1);
        airConditioner.setTemperature(expected);
        latch.await();
    }

}
