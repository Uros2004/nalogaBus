package org.example.service;

import org.example.gtfs.GtfsParser;
import org.example.model.Arrival;
import org.example.model.Route;
import org.example.model.Stop;
import org.example.model.StopTimeEntry;
import org.example.model.Trip;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BusArrivalService {

    private final GtfsParser parser;
    private final Path gtfsDirectory;

    public BusArrivalService(GtfsParser parser, Path gtfsDirectory) {
        this.parser = parser;
        this.gtfsDirectory = gtfsDirectory;
    }

    public List<String> getUpcomingArrivals(String stopId, int maxPerRoute, String format,
                                            Duration now, Duration window) throws IOException {

        List<Route> routes = parser.parseRoutes(gtfsDirectory.resolve("routes.txt"));
        List<Stop> stops = parser.parseStops(gtfsDirectory.resolve("stops.txt"));
        List<Trip> trips = parser.parseTrips(gtfsDirectory.resolve("trips.txt"));
        List<StopTimeEntry> stopTimeEntries = parser.parseStopTimeEntries(gtfsDirectory.resolve("stop_times.txt"));

        //pomoč AI
        Optional<Stop> stopOpt = stops.stream()
                .filter(s -> s.id().equals(stopId))
                .findFirst();

        if (stopOpt.isEmpty()) {
            return List.of("Postajališče z ID " + stopId + " ne obstaja.");
        }
        Stop stop = stopOpt.get();

        Map<String, Trip> tripsById = trips.stream()
                .collect(Collectors.toMap(Trip::id, t -> t, (existing, duplicate) -> existing));

        Map<String, Route> routesById = routes.stream()
                .collect(Collectors.toMap(Route::id, r -> r,  (existing, duplicate) -> existing));

        List<StopTimeEntry> atThisStop = stopTimeEntries.stream()
                .filter(e -> e.stopId().equals(stopId))
                .toList();

        List<StopTimeEntry> withinWindow = new ArrivalTimeFilter()
                .filterWithinWindow(atThisStop, now, window);

        List<Arrival> arrivals = withinWindow.stream()
                .map(entry -> toArrival(entry, tripsById, routesById))
                .toList();

        Map<String, List<Arrival>> grouped = new ArrivalGrouper()
                .groupByRoute(arrivals, maxPerRoute);

        ArrivalFormatter formatter = new ArrivalFormatter();
        List<String> output = new ArrayList<>();
        output.add("Postajališče: " + stop.name());

        //Pomoč AI
        for (Map.Entry<String, List<Arrival>> group : grouped.entrySet()) {
            String times = group.getValue().stream()
                    .map(a -> "absolute".equals(format)
                            ? formatter.formatAbsolute(a.arrivalTime())
                            : formatter.formatRelative(a.arrivalTime(), now))
                    .collect(Collectors.joining(", "));

            output.add(group.getKey() + ": " + times);
        }

        return output;
    }

    private Arrival toArrival(StopTimeEntry entry, Map<String, Trip> tripsById, Map<String, Route> routesById) {
        Trip trip = tripsById.get(entry.tripId());
        Route route = routesById.get(trip.routeid());
        return new Arrival(route.shortName(), entry.arrivalTime());
    }
}