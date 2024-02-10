package org.smarthome.domain.cleaning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.listener.VacuumListener;
import org.smarthome.exception.CleaningException;
import org.smarthome.util.DebugLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.smarthome.util.Constants.*;

class VacuumTest {

    private final DebugLogger logger = new DebugLogger(Logger.getLogger(getClass().getName()));

    private List<Room> rooms;
    private int chargingStationPositionIndex;
    private Vacuum vacuum;

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

        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
                logger.info("vacuum moving to " + currentPosition.getName());
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
                logger.info("vacuum change state to " + vacuumState.getClass().getSimpleName());
            }

            @Override
            public void onCompletedCleaning() {
                logger.info("vacuum completed cleaning");
            }

            @Override
            public void onStoppedCleaning() {
                logger.info("vacuum stop cleaning");
            }

            @Override
            public void onCleaningException(CleaningException e) {
                logger.warning(e.getMessage());
            }
        });
    }

    @Test
    void createVacuumTest() {
        assertNotNull(vacuum.getHouseMapping());
        assertTrue(vacuum.getHouseMapping().size() > 0);
        assertEquals(rooms.get(chargingStationPositionIndex), vacuum.getChargingStationPosition());
        assertEquals(rooms.get(chargingStationPositionIndex), vacuum.getCurrentPosition());
    }

    @Test
    void transitToChargingStationTest() throws InterruptedException {
        vacuum.setCurrentPosition(rooms.get(1));
        vacuum.transitToChargingStation();
        assertEquals(Charging.class, vacuum.getVacuumState().getClass());
        assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());

        vacuum.setCurrentPosition(rooms.get(3));
        vacuum.transitToChargingStation();
        assertEquals(Charging.class, vacuum.getVacuumState().getClass());
        assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
    }

    @Test
    void cleaningTest() throws InterruptedException {
        vacuum.clean();
        assertEquals(Charging.class, vacuum.getVacuumState().getClass());
        assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
    }

    @Test
    void cleaningErrorTest1() throws InterruptedException {
        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
            }

            @Override
            public void onCompletedCleaning() {
            }

            @Override
            public void onStoppedCleaning() {
            }

            @Override
            public void onCleaningException(CleaningException e) {
                assertEquals(TRANSIT_TO_CHARGING_STATION_MESSAGE, e.getMessage());
            }
        });

        vacuum.setVacuumState(new Transit(vacuum));
        vacuum.clean();
    }

    @Test
    void cleaningErrorTest2() throws InterruptedException {
        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
            }

            @Override
            public void onCompletedCleaning() {
            }

            @Override
            public void onStoppedCleaning() {
            }

            @Override
            public void onCleaningException(CleaningException e) {
                assertEquals(ALREADY_CLEANING_MESSAGE, e.getMessage());
            }
        });

        vacuum.setVacuumState(new Cleaning(vacuum));
        vacuum.clean();
    }

    @Test
    void stopCleaningTest() throws InterruptedException {
        vacuum.setVacuumState(new Cleaning(vacuum));
        vacuum.setCurrentPosition(rooms.get(1));
        vacuum.stop();
        assertEquals(Charging.class, vacuum.getVacuumState().getClass());
        assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
    }

    @Test
    void stopCleaningErrorTest1() throws InterruptedException {
        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
            }

            @Override
            public void onCompletedCleaning() {
            }

            @Override
            public void onStoppedCleaning() {
            }

            @Override
            public void onCleaningException(CleaningException e) {
                assertEquals(NOT_CLEANING_YET_MESSAGE, e.getMessage());
            }
        });

        vacuum.stop();
        assertEquals(Charging.class, vacuum.getVacuumState().getClass());
        assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
    }

    @Test
    void stopCleaningErrorTest2() throws InterruptedException {
        vacuum.addObserver(new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
            }

            @Override
            public void onChangeState(VacuumState vacuumState) {
            }

            @Override
            public void onCompletedCleaning() {
            }

            @Override
            public void onStoppedCleaning() {
            }

            @Override
            public void onCleaningException(CleaningException e) {
                assertEquals(CLEANING_ALREADY_TERMINATED_MESSAGE, e.getMessage());
            }
        });

        vacuum.setVacuumState(new Transit(vacuum));
        vacuum.setCurrentPosition(rooms.get(1));
        vacuum.stop();
    }

}
