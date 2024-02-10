package org.smarthome.gui;

import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.protection.Siren;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GUITest {

    @Test
    void GUItest() {
        assertTrue(true);
    }

    public static void main(String[] args) {
        Room r1 = new SmartHomeRoomBuilder("Stanza 1")
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .create();
        Room r2 = new SmartHomeRoomBuilder("Stanza 2")
                .addLight(new Light())
                .addLight(new Light())
                .create();
        Room r3 = new SmartHomeRoomBuilder("Stanza 3")
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .addLight(new Light())
                .create();

        SmartHome home1 = new SmartHomeBuilder()
                .addRoom(r1)
                .addRoom(r2)
                .addRoom(r3)
                .create();

        SmartHomeGUI homeGUI = new SmartHomeGUI(home1);
        homeGUI.setVisible(true);
    }

}
