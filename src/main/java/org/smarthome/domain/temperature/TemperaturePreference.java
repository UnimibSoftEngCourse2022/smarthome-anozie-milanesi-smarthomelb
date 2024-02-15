package org.smarthome.domain.temperature;

import org.smarthome.exception.FieldOutOfRangeException;
import org.smarthome.listener.ObservableElement;
import org.smarthome.listener.TemperaturePreferenceListener;
import org.smarthome.util.Constants;

public class TemperaturePreference extends ObservableElement<TemperaturePreferenceListener> {

    private int idealTemperature;
    private int threshold;

    public TemperaturePreference() {
        this.idealTemperature = Constants.defaultIdealTemperature();
        this.threshold = Constants.defaultIdealTemperatureThreshold();
    }

    public TemperaturePreference(int idealTemperature, int threshold) {
        if (idealTemperature >= Constants.airConditionerBottomRangeValue() &&
                idealTemperature <= Constants.airConditionerUpperRangeValue()) {
            this.idealTemperature = idealTemperature;
        } else {
            this.idealTemperature = Constants.defaultIdealTemperature();
        }
        if (threshold >= Constants.temperatureThresholdBottomRangeValue() &&
                threshold <= Constants.temperatureThresholdUpperRangeValue()) {
            this.threshold = threshold;
        } else {
            this.threshold = Constants.defaultIdealTemperatureThreshold();
        }
    }

    public synchronized int getIdealTemperature() {
        return idealTemperature;
    }

    public synchronized void setIdealTemperature(int idealTemperature) {
        if (idealTemperature >= Constants.airConditionerBottomRangeValue() &&
                idealTemperature <= Constants.airConditionerUpperRangeValue() &&
                this.idealTemperature != idealTemperature) {
            this.idealTemperature = idealTemperature;
            for (TemperaturePreferenceListener observer : observers) {
                observer.onIdealTemperatureChange(idealTemperature);
            }
        } else {
            for (TemperaturePreferenceListener observer : observers) {
                observer.onFieldOutOfRangeException(
                        new FieldOutOfRangeException("Ideal temperature out of range",
                                idealTemperature,
                                Constants.airConditionerBottomRangeValue(),
                                Constants.airConditionerUpperRangeValue()));
            }
        }
    }

    public synchronized int getThreshold() {
        return threshold;
    }

    public synchronized void setThreshold(int threshold) {
        if (threshold >= Constants.temperatureThresholdBottomRangeValue() &&
                threshold <= Constants.temperatureThresholdUpperRangeValue() &&
                this.threshold != threshold) {
            this.threshold = threshold;
            for (TemperaturePreferenceListener observer : observers) {
                observer.onThresholdChange(threshold);
            }
        } else {
            for (TemperaturePreferenceListener observer : observers) {
                observer.onFieldOutOfRangeException(
                        new FieldOutOfRangeException("Threshold out of range",
                                threshold,
                                Constants.temperatureThresholdBottomRangeValue(),
                                Constants.temperatureThresholdUpperRangeValue()));
            }
        }
    }

}
