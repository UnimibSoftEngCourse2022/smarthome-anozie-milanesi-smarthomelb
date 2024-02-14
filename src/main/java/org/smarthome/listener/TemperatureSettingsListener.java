package org.smarthome.listener;

import org.smarthome.exception.FieldOutOfRangeException;

public interface TemperatureSettingsListener extends ElementListener {
    void onIdealTemperatureChange(int idealTemperature);
    void onThresholdChange(int threshold);
    void onFieldOutOfRangeException(FieldOutOfRangeException e);
}
