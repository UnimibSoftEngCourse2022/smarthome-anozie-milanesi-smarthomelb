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

    private final List<CleaningActionListener> observers;
    private final List<Room> houseMapping;
    private final Room chargingStationPosition;
    private Room currentPosition;
    private VacuumState vacuumState;

    public Vacuum(List<Room> houseMapping, Room chargingStationPosition) {
        this.observers = new ArrayList<>();
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

    public void addObserver(CleaningActionListener observer) {
        observers.add(observer);
    }

    public void removeObserver(CleaningActionListener observer) {
        observers.remove(observer);
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
        if (!Objects.equals(getCurrentPosition(), currentPosition)) {
            this.currentPosition = currentPosition;
            for (CleaningActionListener observer : observers) {
                observer.onChangePosition(currentPosition);
            }
        }
    }

    public synchronized VacuumState getVacuumState() {
        return vacuumState;
    }

    public synchronized boolean isCleaning() {
        return getVacuumState().getClass() == Cleaning.class;
    }

    public synchronized void setVacuumState(VacuumState vacuumState) {
        if (!Objects.equals(getVacuumState().getClass(), vacuumState.getClass())) {
            this.vacuumState = vacuumState;
            for (CleaningActionListener observer : observers) {
                observer.onChangeState(vacuumState);
            }
        }
    }

    public void clean() throws InterruptedException {
        try {
            getVacuumState().clean();
        } catch (CleaningException e) {
            notifyCleaningException(e);
        }
    }

    public void stop() throws InterruptedException {
        try {
            getVacuumState().stop();
        } catch (CleaningException e) {
            notifyCleaningException(e);
        }
    }

    protected void notifyCompletedCleaning() {
        for (CleaningActionListener observer : observers) {
            observer.onCompletedCleaning();
        }
    }

    protected void notifyStoppedCleaning() {
        for (CleaningActionListener observer : observers) {
            observer.onStoppedCleaning();
        }
    }

    protected void notifyCleaningException(CleaningException e) {
        for (CleaningActionListener observer : observers) {
            observer.onCleaningException(e);
        }
    }

    protected void cleaningHome() throws InterruptedException {
        setVacuumState(new Cleaning(this));
        for (Room room : getHouseMapping()) {
            moveToRoom(room);
            cleanCurrentRoom();
        }
    }

    protected void transitToChargingStation() throws InterruptedException {
        setVacuumState(new Transit(this));
        int startIndex = getHouseMapping().indexOf(getCurrentPosition());
        for (int i = startIndex; i >= 0; i--) {
            moveToRoom(getHouseMapping().get(i));
        }
        setVacuumState(new Charging(this));
    }

    private void moveToRoom(Room room) throws InterruptedException {
        setCurrentPosition(room);
        Thread.sleep(MOVING_TO_ROOM_MS_DURATION);
    }

    private void cleanCurrentRoom() throws InterruptedException {
        // simulating vacuum cleaning room
        Thread.sleep(CLEANING_ROOM_MS_DURATION);
    }

}
