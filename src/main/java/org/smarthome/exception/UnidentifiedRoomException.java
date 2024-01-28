package org.smarthome.exception;

import org.smarthome.domain.Room;

public class UnidentifiedRoomException extends RuntimeException {

    public UnidentifiedRoomException(Room room) {
        super("The room [" + room + ": " + room.getName() + "] is not identified.");
    }

}