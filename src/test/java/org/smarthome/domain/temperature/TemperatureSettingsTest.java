package org.smarthome.domain.temperature;

import org.junit.jupiter.api.Test;
import org.smarthome.exception.FieldOutOfRangeException;
import org.smarthome.listener.TemperatureSettingsListener;
import org.smarthome.util.Constants;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureSettingsTest {

    @Test
    void temperatureSettingsTest1() throws InterruptedException {
        TemperatureSettings temperatureSettings = new TemperatureSettings();

        int idealTemperatureExpected = 21;
        int thresholdExpected = 4;

        CountDownLatch latch = new CountDownLatch(4);

        temperatureSettings.addObserver(new TemperatureSettingsListener() {
            @Override
            public void onIdealTemperatureChange(int idealTemperature) {
                assertEquals(idealTemperatureExpected, idealTemperature);
                latch.countDown();
            }

            @Override
            public void onThresholdChange(int threshold) {
                assertEquals(thresholdExpected, threshold);
                latch.countDown();
            }

            @Override
            public void onFieldOutOfRangeException(FieldOutOfRangeException e) {
                assertNotNull(e);
                latch.countDown();
            }
        });

        temperatureSettings.setIdealTemperature(idealTemperatureExpected);
        temperatureSettings.setThreshold(thresholdExpected);
        temperatureSettings.setIdealTemperature(1000);
        temperatureSettings.setThreshold(1000);

        latch.await();
    }

    @Test
    void temperatureSettingsTest2() {
        TemperatureSettings temperatureSettings = new TemperatureSettings(1000, 1000);
        assertNotEquals(1000, temperatureSettings.getIdealTemperature());
        assertEquals(Constants.defaultIdealTemperature(), temperatureSettings.getIdealTemperature());
        assertNotEquals(1000, temperatureSettings.getThreshold());
        assertEquals(Constants.defaultIdealTemperatureThreshold(), temperatureSettings.getThreshold());
    }

}