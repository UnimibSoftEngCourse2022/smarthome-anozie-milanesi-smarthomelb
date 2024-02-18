package org.smarthome.gui;

import org.junit.jupiter.api.Test;
import org.smarthome.MockSmartHome;
import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.illumination.Light;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GUITest {

    @Test
    void test() {
        assertTrue(true);
    }

    public static void main(String[] args) {
        new SmartHomeGUI(MockSmartHome.mock());
    }

}
