package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.sensor.TemperatureSensor;
import org.smarthome.domain.temperature.AirConditioner;
import org.smarthome.domain.temperature.AirConditionerOff;
import org.smarthome.domain.temperature.AirConditionerOn;
import org.smarthome.domain.temperature.AirConditionerState;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.simulation.RoomTemperatureSimulationListener;
import org.smarthome.util.Constants;
import org.smarthome.util.DebugLogger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class SensorTemperatureControlTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private Room room;
    private SmartHome smartHome;

    @BeforeEach
    public void setup() {
        room = new SmartHomeRoomBuilder("test1")
                .setAirConditioner(new AirConditioner())
                .setTemperatureSensor(new TemperatureSensor())
                .create();

        smartHome = new SmartHomeBuilder()
                .addRoom(room)
                .create();
    }

    @Test
    void sensorTemperatureControlTest1() throws InterruptedException {
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

        Thread.sleep(1);
        room.getTemperatureSimulation().setTarget(
                room.getTemperatureSettings().getIdealTemperature() +
                        (room.getTemperatureSettings().getThreshold() * 2));

        assertTrue(latch.await(10, TimeUnit.SECONDS));

        assertEquals(AirConditionerOff.class, room.getAirConditioner().getAirConditionerState().getClass());
        assertEquals(room.getTemperatureSettings().getIdealTemperature(), room.getAirConditioner().getTemperature());
    }

    @Test
    void sensorTemperatureControlTest2() throws InterruptedException {
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

        assertTrue(latch.await(10, TimeUnit.SECONDS));

        assertEquals(AirConditionerOn.class, room.getAirConditioner().getAirConditionerState().getClass());
        assertNotEquals(room.getTemperatureSettings().getIdealTemperature(), room.getAirConditioner().getTemperature());
    }

}