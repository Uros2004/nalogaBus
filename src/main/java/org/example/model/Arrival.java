package org.example.model;

import java.time.Duration;

public record Arrival(String routeShortName, Duration arrivalTime) {

}