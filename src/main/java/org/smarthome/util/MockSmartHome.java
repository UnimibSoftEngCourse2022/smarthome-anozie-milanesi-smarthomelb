package org.smarthome.util;

import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.protection.EmergencyService;
import org.smarthome.domain.protection.Siren;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.domain.sensor.TemperatureSensor;
import org.smarthome.domain.temperature.AirConditioner;

public class MockSmartHome {

    public static SmartHome mock() {
        Room room1 = new SmartHomeRoomBuilder("Stanza 1")
                .addLight(new Light("Light 1"))
                .addLight(new Light("Light 2"))
                .addLight(new Light("Light 3"))
                .addLight(new Light("Light 4"))
                .setAirConditioner(new AirConditioner())
                .setPresenceSensor(new PresenceSensor())
                .setTemperatureSensor(new TemperatureSensor())
                .create();

        Room room2 = new SmartHomeRoomBuilder("Stanza 2")
                .addLight(new Light("Light 1"))
                .addLight(new Light("Light 2"))
                .addLight(new Light("Light 3"))
                .setAirConditioner(new AirConditioner())
                .setPresenceSensor(new PresenceSensor())
                .create();

        Room room3 = new SmartHomeRoomBuilder("Stanza 3")
                .addLight(new Light("Light 1"))
                .addLight(new Light("Light 2"))
                .addLight(new Light("Light 3"))
                .addLight(new Light("Light 4"))
                .addLight(new Light("Light 5"))
                .addLight(new Light("Light 6"))
                .create();

        return new SmartHomeBuilder()
                .addRoom(room1)
                .addRoom(room2)
                .addRoom(room3)
                .setVacuumChargingStationPosition(room1)
                .setProtection(new Siren(), new EmergencyService("911"))
                .create();
    }

}
