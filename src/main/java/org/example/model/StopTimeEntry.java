package org.example.model;
import java.time.Duration;

public record StopTimeEntry (String tripId, String stopId, Duration arrivalTime) {

}

