package org.smarthome.domain.temperature;

import org.junit.jupiter.api.Test;
import org.smarthome.exception.FieldOutOfRangeException;
import org.smarthome.listener.TemperaturePreferenceListener;
import org.smarthome.util.Constants;
import org.smarthome.util.CountDownLatchWaiter;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class TemperaturePreferenceTest {

    @Test
    void temperatureSettingsTest1() throws InterruptedException {
        TemperaturePreference temperaturePreference = new TemperaturePreference();

        int idealTemperatureExpected = 21;
        int thresholdExpected = 4;

        final CountDownLatch latch = new CountDownLatch(4);

        temperaturePreference.addObserver(new TemperaturePreferenceListener() {
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

        temperaturePreference.setIdealTemperature(idealTemperatureExpected);
        temperaturePreference.setThreshold(thresholdExpected);
        temperaturePreference.setIdealTemperature(1000);
        temperaturePreference.setThreshold(1000);

        CountDownLatchWaiter.awaitLatch(latch);
    }

    @Test
    void temperatureSettingsTest2() {
        TemperaturePreference temperaturePreference = new TemperaturePreference(1000, 1000);
        assertNotEquals(1000, temperaturePreference.getIdealTemperature());
        assertEquals(Constants.defaultIdealTemperature(), temperaturePreference.getIdealTemperature());
        assertNotEquals(1000, temperaturePreference.getThreshold());
        assertEquals(Constants.defaultIdealTemperatureThreshold(), temperaturePreference.getThreshold());
    }

}