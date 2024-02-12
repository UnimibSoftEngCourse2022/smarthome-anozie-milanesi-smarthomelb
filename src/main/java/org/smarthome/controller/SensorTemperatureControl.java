package org.smarthome.controller;

import org.smarthome.domain.sensor.TemperatureSensor;

public class SensorTemperatureControl extends SensorControl<Integer> {

    private final TemperatureControl temperatureControl;

    public SensorTemperatureControl(
            TemperatureSensor temperatureSensor,
            TemperatureControl temperatureControl) {
        super(temperatureSensor);
        this.temperatureControl = temperatureControl;
    }

    @Override
    public void onDataChange(Integer temperature) {
        if (temperature != null && temperatureControl.isAutomationActive()) {
            temperatureControl.handleAutomation(temperature);
        }
    }

}
