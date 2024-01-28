package org.smarthome.domain.cleaning;

import org.smarthome.domain.Room;
import org.smarthome.exception.CleaningException;
import org.smarthome.exception.UnidentifiedRoomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.smarthome.util.Constants.CLEANING_ROOM_MS_DURATION;
import static org.smarthome.util.Constants.MOVING_TO_ROOM_MS_DURATION;

public class Vacuum {

    private CleaningActionListener actionListener;
    private final List<Room> houseMapping;
    private final Room chargingStationPosition;
    private Room currentPosition;
    private VacuumState vacuumState;

    public Vacuum(List<Room> houseMapping, Room chargingStationPosition) {
        // Shift the list of rooms so that it starts at the chargingStationPosition
        this.houseMapping = new ArrayList<>(houseMapping.size());

        int startIndex = houseMapping.indexOf(chargingStationPosition);
        if (startIndex == -1) {
            throw new UnidentifiedRoomException(chargingStationPosition);
        }

        for (int i = 0; i < houseMapping.size(); i++) {
            int newIndex = (startIndex + i) % houseMapping.size();
            this.houseMapping.add(houseMapping.get(newIndex));
        }

        this.chargingStationPosition = this.houseMapping.get(0);
        this.currentPosition = chargingStationPosition;
        this.vacuumState = new Charging(this);
    }

    public void setActionListener(CleaningActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public List<Room> getHouseMapping() {
        return houseMapping;
    }

    public Room getChargingStationPosition() {
        return chargingStationPosition;
    }

    public Room getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Room currentPosition) {
        if (!Objects.equals(this.currentPosition, currentPosition)) {
            this.currentPosition = currentPosition;
            if (actionListener != null) {
                actionListener.onChangePosition(currentPosition);
            }
        }
    }

    public VacuumState getVacuumState() {
        return vacuumState;
    }

    public void setVacuumState(VacuumState vacuumState) {
        this.vacuumState = vacuumState;
        if (actionListener != null) {
            actionListener.onChangeState(vacuumState);
        }
    }

    public synchronized void clean() throws CleaningException {
        vacuumState.clean();
    }

    protected void cleaningHome() {
        setVacuumState(new Cleaning(this));
        for (Room room : getHouseMapping()) {
            moveToRoom(room);
            cleanCurrentRoom();
        }
        if (actionListener != null) {
            actionListener.onCompletedCleaning();
        }
    }

    protected void transitToChargingStation() {
        setVacuumState(new Transit(this));
        for (int i = getHouseMapping().size() - 1; i >= 0; i--) {
            moveToRoom(getHouseMapping().get(i));
        }
        setVacuumState(new Charging(this));
    }

    private void moveToRoom(Room room) {
        setCurrentPosition(room);
        // simulating vacuum moving to other room
        try {
            Thread.sleep(MOVING_TO_ROOM_MS_DURATION);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void cleanCurrentRoom() {
        // simulating vacuum cleaning room
        try {
            Thread.sleep(CLEANING_ROOM_MS_DURATION);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
