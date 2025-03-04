package org.smarthome.domain.cleaning;

import org.smarthome.listener.ObservableElement;
import org.smarthome.domain.Room;
import org.smarthome.listener.VacuumListener;
import org.smarthome.exception.CleaningException;
import org.smarthome.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vacuum extends ObservableElement<VacuumListener> {

    private final List<Room> houseMapping;
    private final Room chargingStationPosition;
    private Room currentPosition;
    private VacuumState vacuumState;

    public Vacuum(List<Room> houseMapping, Room chargingStationPosition) {
        super();
        // Shift the list of rooms so that it starts at the chargingStationPosition
        this.houseMapping = new ArrayList<>(houseMapping.size());

        int startIndex = houseMapping.indexOf(chargingStationPosition);
        for (int i = 0; i < houseMapping.size(); i++) {
            int newIndex = (startIndex + i) % houseMapping.size();
            this.houseMapping.add(houseMapping.get(newIndex));
        }

        this.chargingStationPosition = this.houseMapping.get(0);
        this.currentPosition = chargingStationPosition;
        this.vacuumState = new Charging(this);
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
            for (VacuumListener observer : observers) {
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
            for (VacuumListener observer : observers) {
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
        for (VacuumListener observer : observers) {
            observer.onCompletedCleaning();
        }
    }

    protected void notifyStoppedCleaning() {
        for (VacuumListener observer : observers) {
            observer.onStoppedCleaning();
        }
    }

    protected void notifyCleaningException(CleaningException e) {
        for (VacuumListener observer : observers) {
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
        Thread.sleep(Constants.movingToRoomMsDuration());
    }

    private void cleanCurrentRoom() throws InterruptedException {
        // simulating vacuum cleaning room
        Thread.sleep(Constants.cleaningRoomMsDuration());
    }

}
