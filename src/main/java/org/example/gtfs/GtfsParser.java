package org.example.gtfs;

import org.example.model.Route;
import org.example.model.Stop;
import org.example.model.StopTimeEntry;
import org.example.model.Trip;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GtfsParser {

    public List<Route> parseRoutes(Path file) throws IOException {
        List<Route> routes = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                String id = parts[0];
                String shortName = parts[2];
                routes.add(new Route(id, shortName));
            }
        }
        return routes;
    }

    public List<Stop> parseStops(Path file) throws IOException {
        List<Stop> stops = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                String id = parts[0];
                String name = parts[2];
                stops.add(new Stop(id, name));
            }
        }
        return stops;
    }

    public List<Trip> parseTrips(Path file, Predicate<String> tripIdFilter) throws IOException {
        List<Trip> trips = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                String tripId = parts[2];

                if (!tripIdFilter.test(tripId)) {
                    continue;
                }

                String routeId = parts[0];
                trips.add(new Trip(tripId, routeId));
            }
        }
        return trips;
    }

    public List<Trip> parseTrips(Path file) throws IOException {
        return parseTrips(file, tripId -> true);
    }

    //Pomoč AI
    private static Duration parseGtfsTime(String raw) {
        String[] hms = raw.split(":");
        int hours = Integer.parseInt(hms[0]);
        int minutes = Integer.parseInt(hms[1]);
        int seconds = Integer.parseInt(hms[2]);
        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

    public List<StopTimeEntry> parseStopTimeEntries(Path file, Predicate<String> stopIdFilter) throws IOException {
        List<StopTimeEntry> result = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                String stopId = parts[3];

                if (!stopIdFilter.test(stopId)) {
                    continue;
                }

                String tripId = parts[0];
                Duration arrivalTime = parseGtfsTime(parts[1]);
                result.add(new StopTimeEntry(tripId, stopId, arrivalTime));
            }
        }
        return result;
    }

    public List<StopTimeEntry> parseStopTimeEntries(Path file) throws IOException {
        return parseStopTimeEntries(file, stopId -> true);
    }
}