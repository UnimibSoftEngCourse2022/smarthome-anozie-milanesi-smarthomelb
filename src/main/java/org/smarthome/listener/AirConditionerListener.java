package org.smarthome.listener;

import org.smarthome.domain.temperature.AirConditionerState;

public interface AirConditionerListener extends ElementListener {
    void onChangeState(AirConditionerState state);
    void onTemperatureChange(int temperature);
}
