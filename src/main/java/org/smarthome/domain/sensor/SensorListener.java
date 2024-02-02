package org.smarthome.domain.sensor;

public interface SensorListener<T> {
    void onDataChange(T data);
}
