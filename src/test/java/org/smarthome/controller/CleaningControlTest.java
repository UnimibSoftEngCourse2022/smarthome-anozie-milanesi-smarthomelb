package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.cleaning.Charging;
import org.smarthome.listener.VacuumListener;
import org.smarthome.domain.cleaning.Vacuum;
import org.smarthome.domain.cleaning.VacuumState;
import org.smarthome.exception.CleaningException;
import org.smarthome.util.Constants;
import org.smarthome.util.DebugLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class CleaningControlTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private List<Room> rooms;
    private int chargingStationPositionIndex;
    private Vacuum vacuum;
    private CleaningControl cleaningControl;

    @BeforeEach
    void setUp() {
        // create rooms
        rooms = new ArrayList<>();
        rooms.add(new SmartHomeRoomBuilder("test1").create());
        rooms.add(new SmartHomeRoomBuilder("test2").create());
        rooms.add(new SmartHomeRoomBuilder("test3").create());
        rooms.add(new SmartHomeRoomBuilder("test4").create());
        chargingStationPositionIndex = 2;

        vacuum = new Vacuum(rooms, rooms.get(chargingStationPositionIndex));
        cleaningControl = new CleaningControl(vacuum);
    }

    @Test
    void startCleaningTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
            }

            @Override
            public void onCompletedCleaning() {
                assertEquals(Charging.class, vacuum.getVacuumState().getClass());
                assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
                latch.countDown();
            }

            @Override
            public void onStoppedCleaning() {
            }

            @Override
            public void onCleaningException(CleaningException e) {
            }
        });

        cleaningControl.startCleaning();

        assertTrue(latch.await(10, TimeUnit.SECONDS));

        assertEquals(Charging.class, vacuum.getVacuumState().getClass());
        assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
    }

    @Test
    void stopCleaningTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
            }

            @Override
            public void onCompletedCleaning() {
                assertEquals(Charging.class, vacuum.getVacuumState().getClass());
                assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
                latch.countDown();
            }

            @Override
            public void onStoppedCleaning() {
                latch.countDown();
            }

            @Override
            public void onCleaningException(CleaningException e) {
            }
        });

        cleaningControl.startCleaning();
        Thread.sleep(Constants.cleaningRoomMsDuration() * 3L);
        cleaningControl.stopCleaning();

        assertTrue(latch.await(10, TimeUnit.SECONDS));
    }

    @Test
    void doesNotThrowCleaningTest() {
        assertDoesNotThrow(() -> {
            CleaningControl cleaningControl = new CleaningControl(null);
            cleaningControl.startCleaning();
            cleaningControl.stopCleaning();
        });
    }

    @Test
    void startCleaningConcurrentTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
                logger.info("vacuum change state to " + vacuumState.getClass().getSimpleName());
            }

            @Override
            public void onCompletedCleaning() {
                logger.info("vacuum completed cleaning");
                assertEquals(Charging.class, vacuum.getVacuumState().getClass());
                assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
                latch.countDown();
            }

            @Override
            public void onStoppedCleaning() {
                logger.info("vacuum stop cleaning");
            }

            @Override
            public void onCleaningException(CleaningException e) {
                logger.warning(e.getMessage());
                assertEquals(Constants.alreadyCleaningMessage(), e.getMessage());
                latch.countDown();
            }
        });

        cleaningControl.startCleaning();
        Thread.sleep(Constants.cleaningRoomMsDuration());
        cleaningControl.startCleaning();
        Thread.sleep(Constants.cleaningRoomMsDuration());
        cleaningControl.startCleaning();

        assertTrue(latch.await(10, TimeUnit.SECONDS));

        assertEquals(Charging.class, vacuum.getVacuumState().getClass());
        assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
    }

    @Test
    void stopCleaningConcurrentTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
                logger.info("vacuum change state to " + vacuumState.getClass().getSimpleName());
            }

            @Override
            public void onCompletedCleaning() {
                logger.info("vacuum completed cleaning");
                assertEquals(Charging.class, vacuum.getVacuumState().getClass());
                assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
                latch.countDown();
            }

            @Override
            public void onStoppedCleaning() {
                logger.info("vacuum cleaning operation stopped");
                latch.countDown();
            }

            @Override
            public void onCleaningException(CleaningException e) {
                logger.warning(e.getMessage());
                latch.countDown();
            }
        });

        cleaningControl.startCleaning();
        Thread.sleep(Constants.cleaningRoomMsDuration() * 3L);
        cleaningControl.stopCleaning();
        Thread.sleep(Constants.cleaningRoomMsDuration());
        cleaningControl.stopCleaning();

        assertTrue(latch.await(10, TimeUnit.SECONDS));
    }

}