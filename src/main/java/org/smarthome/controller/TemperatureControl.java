package org.smarthome.controller;

import org.smarthome.domain.temperature.AirConditioner;
import org.smarthome.domain.temperature.TemperatureSettings;

public class TemperatureControl extends AutomaticControl<Integer> {

    private final TemperatureSettings temperatureSettings;
    private final AirConditioner airConditioner;

    public TemperatureControl(TemperatureSettings temperatureSettings, AirConditioner airConditioner) {
        this.temperatureSettings = temperatureSettings;
        this.airConditioner = airConditioner;
    }

    public void handleAirConditioner() {
        if (airConditioner != null) {
            setAutomationActive(false);
            airConditioner.handle();
        }
    }

    public void increaseTemperature() {
        if (airConditioner != null) {
            setAutomationActive(false);
            airConditioner.setTemperature(airConditioner.getTemperature() + 1);
        }
    }

    public void decreaseTemperature() {
        if (airConditioner != null) {
            setAutomationActive(false);
            airConditioner.setTemperature(airConditioner.getTemperature() - 1);
        }
    }

    @Override
    public void handleAutomation(Integer temperature) {
        if (temperatureSettings != null && airConditioner != null && temperature != null) {
            int temperatureDifference = temperature - temperatureSettings.getIdealTemperature();

            if (Math.abs(temperatureDifference) > temperatureSettings.getThreshold()) {
                // Temperature exceeds the threshold, adjust it to the ideal temperature
                airConditioner.setTemperature(temperatureSettings.getIdealTemperature());
            } else if (temperatureDifference == 0) {
                airConditioner.off();
            }
        }
    }

}
