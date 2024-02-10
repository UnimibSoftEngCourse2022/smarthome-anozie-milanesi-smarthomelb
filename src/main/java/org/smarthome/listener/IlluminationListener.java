package org.smarthome.listener;

import org.smarthome.domain.illumination.IlluminationState;

public interface IlluminationListener extends ElementListener {
    void onChangeState(IlluminationState state);
}
