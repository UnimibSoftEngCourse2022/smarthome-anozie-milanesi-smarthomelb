package org.smarthome.controller;

import org.smarthome.domain.sensor.Sensor;
import org.smarthome.domain.listener.SensorListener;

public abstract class SensorControl<T> implements SensorListener<T> {

    private final Sensor<T> sensor;

    public SensorControl(Sensor<T> sensor) {
        this.sensor = sensor;
    }

    public void monitorSensor() {
        sensor.addObserver(this);
    }

}
