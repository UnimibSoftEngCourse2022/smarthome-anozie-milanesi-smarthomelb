package org.smarthome.listener;

import org.smarthome.domain.protection.AlarmState;

public interface AlarmListener extends ElementListener {
    void onChangeState(AlarmState state);
}
