package org.smarthome.listener;

import org.smarthome.exception.TemperatureOutOfRangeException;

public interface TemperatureSettingsListener extends ElementListener {
    void onIdealTemperatureChange(int idealTemperature);
    void onThresholdChange(int threshold);
    void onTemperatureOutOfRangeException(TemperatureOutOfRangeException e);
}
