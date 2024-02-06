package org.smarthome.domain.listener;

import org.smarthome.domain.Room;
import org.smarthome.domain.cleaning.VacuumState;
import org.smarthome.exception.CleaningException;

public interface CleaningActionListener extends ElementListener {
    void onChangePosition(Room currentPosition);
    void onChangeState(VacuumState vacuumState);
    void onCompletedCleaning();
    void onStoppedCleaning();
    void onCleaningException(CleaningException e);
}
