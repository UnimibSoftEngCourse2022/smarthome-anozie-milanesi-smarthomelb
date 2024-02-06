package org.smarthome.domain.listener;

import org.smarthome.domain.protection.AlarmState;

public interface AlarmListener extends ElementListener {
    void onChangeState(AlarmState state);
}
