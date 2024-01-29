package org.smarthome.domain.cleaning;

import org.smarthome.domain.Room;
import org.smarthome.exception.CleaningException;

public interface CleaningActionListener {
    void onChangePosition(Room currentPosition);
    void onChangeState(VacuumState vacuumState);
    void onCompletedCleaning();
    void onStoppedCleaning();
    void onCleaningException(CleaningException e);
}
