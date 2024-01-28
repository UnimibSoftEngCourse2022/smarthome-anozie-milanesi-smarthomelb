package org.smarthome.domain.cleaning;

import org.smarthome.domain.Room;

public interface CleaningActionListener {
    void onChangePosition(Room currentPosition);
    void onChangeState(VacuumState vacuumState);
    void onCompletedCleaning();
}
