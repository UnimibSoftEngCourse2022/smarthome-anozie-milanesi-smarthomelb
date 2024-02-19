package org.smarthome.gui;

import org.junit.jupiter.api.Test;
import org.smarthome.util.MockSmartHome;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GUITest {

    @Test
    void test() {
        assertTrue(true);
    }

    public static void main(String[] args) {
        SmartHomeGUI.init(MockSmartHome.mock()).setVisible(true);
    }

}
