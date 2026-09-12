package org.example.service;

import java.time.Duration;

public class ArrivalFormatter {
    public String formatAbsolute(Duration arrivalTime){
        int hours = arrivalTime.toHoursPart();
        int minutes = arrivalTime.toMinutesPart();
        return String.format("%02d:%02d", hours, minutes);
    }

    public String formatRelative(Duration arrivalTime, Duration now){
        Duration diff = arrivalTime.minus(now);
        long minutes = diff.toMinutes();
        return minutes + "min";
    }
}
