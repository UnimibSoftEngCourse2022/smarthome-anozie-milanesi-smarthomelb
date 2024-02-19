package org.smarthome.gui.util;

import javax.swing.*;

public class DialogOpener {

    private static final String HTML = "<html><body style='width: %1spx'>%1s";

    private DialogOpener() {}

    public static void openMessageDialog(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(
                null,
                String.format(HTML, 300, message),
                title,
                messageType
        );
    }

    public static String openInputDialog(String title, String message, int messageType) {
       return JOptionPane.showInputDialog(
                null,
                String.format(HTML, 300, message),
                title,
                messageType
        );
    }

    public static String openInputDialog(String title, String message, int messageType, String initialValue) {
        return (String) JOptionPane.showInputDialog(
                null,
                String.format(HTML, 300, message),
                title,
                messageType,
                null,
                null,
                initialValue
        );
    }


}
