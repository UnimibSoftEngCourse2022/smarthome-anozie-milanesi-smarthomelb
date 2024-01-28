package org.smarthome.util;

public class Constants {

    private Constants() {}

    public static final boolean IS_DEBUG_MODE = true;

    public static final String ALREADY_CLEANING_MESSAGE =
            "The vacuum is already in the cleaning state";
    public static final String TRANSIT_TO_CHARGING_STATION_MESSAGE =
            "The vacuum is in transit towards the charging station, wait for it to arrive at its destination";

    public static final int CLEANING_ROOM_MS_DURATION = 1;
    public static final int MOVING_TO_ROOM_MS_DURATION = 1;

}
