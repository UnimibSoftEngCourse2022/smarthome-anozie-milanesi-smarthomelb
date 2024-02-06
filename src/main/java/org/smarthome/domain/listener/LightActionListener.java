package org.smarthome.domain.listener;

import org.smarthome.domain.illumination.LightState;

public interface LightActionListener extends ElementListener {
    void onChangeState(LightState lightState);
}
