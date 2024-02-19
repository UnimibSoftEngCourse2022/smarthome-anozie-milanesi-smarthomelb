package org.smarthome.gui.util;

import javax.swing.*;

public class DialogOpener {

    private static final String html = "<html><body style='width: %1spx'>%1s";

    public static void openMessageDialog(String title, String message, int messageType) {

        JOptionPane.showMessageDialog(
                null,
                String.format(html, 300, message),
                title,
                messageType
        );
    }

    public static String openInputDialog(String title, String message, int messageType) {
       return JOptionPane.showInputDialog(
                null,
                String.format(html, 300, message),
                title,
                messageType
        );
    }

    public static String openInputDialog(String title, String message, int messageType, String initialValue) {
        return (String) JOptionPane.showInputDialog(
                null,
                String.format(html, 300, message),
                title,
                messageType,
                null,
                null,
                initialValue
        );
    }


}
