package org.smarthome.listener;

import org.smarthome.domain.Room;
import org.smarthome.domain.cleaning.VacuumState;
import org.smarthome.exception.CleaningException;

public interface VacuumListener extends ElementListener {
    void onChangePosition(Room currentPosition);
    void onChangeState(VacuumState state);
    void onCompletedCleaning();
    void onStoppedCleaning();
    void onCleaningException(CleaningException e);
}
