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

public class GtfsParser {

    public List<Route> parseRoutes(Path file) throws IOException {
        List<Route> routes = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {       //Pomoč AI

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

    public  List<Trip> parseTrips(Path file) throws IOException {
        List<Trip> trips = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {

            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);

                String routeId = parts[0];
                String tripId = parts[2];

                trips.add(new Trip(tripId, routeId));
            }
        }
        return trips;
    }

    //Pomoč AI
    private static Duration parseGtfsTime(String raw) {
        String[] hms = raw.split(":");
        int hours = Integer.parseInt(hms[0]);
        int minutes = Integer.parseInt(hms[1]);
        int seconds = Integer.parseInt(hms[2]);
        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

    public List<StopTimeEntry>  parseStopTimeEntries(Path file) throws IOException {
        List<StopTimeEntry> stopTimeEntries = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {

            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);

                String tripId = parts[0];
                Duration arrivalTime = parseGtfsTime(parts[1]);     //Pomoč AI
                String stopId = parts[3];

                stopTimeEntries.add(new StopTimeEntry(tripId, stopId, arrivalTime));
            }
        }
        return stopTimeEntries;
    }
}