package org.smarthome.gui;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.illumination.Light;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeGUITest {
    public static void main(String[] args) {
        List<Light> lights1 = new ArrayList<>();
        lights1.add(new Light());
        lights1.add(new Light());
        lights1.add(new Light());
        lights1.add(new Light());

        List<Light> lights2 = new ArrayList<>();
        lights2.add(new Light());
        lights2.add(new Light());

        List<Light> lights3 = new ArrayList<>();
        lights3.add(new Light());
        lights3.add(new Light());
        lights3.add(new Light());
        lights3.add(new Light());
        lights3.add(new Light());
        lights3.add(new Light());
        lights3.add(new Light());
        lights3.add(new Light());

        Room r1 = new Room("Stanza 1", lights1);
        Room r2 = new Room("Stanza 2", lights2);
        Room r3 = new Room("Stanza 3", lights3);


        SmartHome home1 = new SmartHomeBuilder().addRoom(r1).addRoom(r2).addRoom(r3).create();

        SmartHomeGUI homeGUI = new SmartHomeGUI(home1);
        homeGUI.setVisible(true);

    }
}
