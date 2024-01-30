package org.smarthome.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.smarthome.domain.Room;
import org.smarthome.domain.cleaning.Charging;
import org.smarthome.domain.cleaning.CleaningActionListener;
import org.smarthome.domain.cleaning.Vacuum;
import org.smarthome.domain.cleaning.VacuumState;
import org.smarthome.exception.CleaningException;
import org.smarthome.util.DebugLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.smarthome.util.Constants.ALREADY_CLEANING_MESSAGE;

@Disabled
class CleaningControlTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private List<Room> rooms;
    private int chargingStationPositionIndex;
    private Vacuum vacuum;
    private CleaningControl cleaningControl;

    @BeforeEach
    void setUp() {
        // create rooms
        Room room1 = new Room("test1", null);
        Room room2 = new Room("test2", null);
        Room room3 = new Room("test3", null);
        Room room4 = new Room("test4", null);

        rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        rooms.add(room4);
        chargingStationPositionIndex = 2;

        vacuum = new Vacuum(rooms, rooms.get(chargingStationPositionIndex));
        cleaningControl = new CleaningControl(vacuum);
    }

    @Test
    void startCleaningTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        vacuum.setCleaningActionListener(new CleaningActionListener() {
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
                assertEquals(ALREADY_CLEANING_MESSAGE, e.getMessage());
                latch.countDown();
            }
        });

        cleaningControl.startCleaning();
        Thread.sleep(1);
        cleaningControl.startCleaning();
        Thread.sleep(1);
        cleaningControl.startCleaning();
        latch.await();
        assertEquals(Charging.class, vacuum.getVacuumState().getClass());
        assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
    }

    @Test
    void stopCleaningTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        vacuum.setCleaningActionListener(new CleaningActionListener() {
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
        Thread.sleep(3);
        cleaningControl.stopCleaning();
        Thread.sleep(1);
        cleaningControl.stopCleaning();
        latch.await();
    }

}