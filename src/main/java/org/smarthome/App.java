package org.smarthome;

import org.smarthome.gui.SmartHomeGUIController;
import org.smarthome.util.MockSmartHome;

public class App {

    public static void main(String[] args) {
        new SmartHomeGUIController(MockSmartHome.mock())
                .getSmartHomeGUI().setVisible(true);
    }

}
