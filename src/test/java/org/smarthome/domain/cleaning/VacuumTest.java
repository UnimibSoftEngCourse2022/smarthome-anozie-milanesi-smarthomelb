package org.smarthome.domain.cleaning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.smarthome.domain.Room;
import org.smarthome.exception.CleaningException;
import org.smarthome.exception.UnidentifiedRoomException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.smarthome.util.Constants.*;

class VacuumTest {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    private Room chargingStationPositionRoom;
    private List<Room> rooms;
    private Vacuum vacuum;

    @BeforeEach
    void setUp() {
        // create rooms
        Room room1 = new Room("test1", null);
        Room room2 = new Room("test2", null);
        chargingStationPositionRoom = new Room("test3", null);
        Room room3 = new Room("test4", null);

        rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(chargingStationPositionRoom);
        rooms.add(room3);

        vacuum = new Vacuum(rooms, chargingStationPositionRoom);

        if (IS_DEBUG_MODE) {
            vacuum.setActionListener(new CleaningActionListener() {
                @Override
                public void onChangePosition(Room currentPosition) {
                    LOGGER.info("vacuum moving to " + currentPosition.getName());
                }

                @Override
                public void onChangeState(VacuumState vacuumState) {
                    LOGGER.info("vacuum change state to " + vacuumState.getClass().getSimpleName());
                }

                @Override
                public void onCompletedCleaning() {
                    LOGGER.info("vacuum completed cleaning");
                }
            });
        }
    }

    @Test
    void createVacuumTest() {
        assertNotNull(vacuum.getHouseMapping());
        assertTrue(vacuum.getHouseMapping().size() > 0);
        assertEquals(chargingStationPositionRoom, vacuum.getChargingStationPosition());
        assertEquals(chargingStationPositionRoom, vacuum.getCurrentPosition());
    }

    @Test
    void createVacuumErrorTest() {
        assertThrows(UnidentifiedRoomException.class, () ->
                vacuum = new Vacuum(rooms, new Room("errorRoom", null)));
    }

    @Test
    void cleaningTest() {
        try {
            vacuum.clean();
            assertEquals(Charging.class, vacuum.getVacuumState().getClass());
            assertEquals(vacuum.getChargingStationPosition(), vacuum.getCurrentPosition());
        } catch (CleaningException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void cleaningErrorTest1() {
        try {
            vacuum.setVacuumState(new Transit(vacuum));
            vacuum.clean();
        } catch (CleaningException e) {
            assertEquals(TRANSIT_TO_CHARGING_STATION_MESSAGE, e.getMessage());
        }
    }

    @Test
    void cleaningErrorTest2() {
        try {
            vacuum.setVacuumState(new Cleaning(vacuum));
            vacuum.clean();
        } catch (CleaningException e) {
            assertEquals(ALREADY_CLEANING_MESSAGE, e.getMessage());
        }
    }

}