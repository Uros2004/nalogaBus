package org.example.gtfs;

import org.example.model.Route;
import org.example.model.Stop;
import org.example.model.StopTimeEntry;
import org.example.model.Trip;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GtfsParserTest {

    @Test //Pomoč AI
    void shouldParseRoutes() throws Exception {
        GtfsParser parser = new GtfsParser();

        Path file = Path.of("src/test/resources/routes.txt");

        List<Route> routes = parser.parseRoutes(file);

        assertEquals(7, routes.size());
        assertEquals("101", routes.get(0).id());
        assertEquals("101", routes.get(0).shortName());
    }

    @Test
    void shouldParseStop() throws Exception {
        GtfsParser parser = new GtfsParser();

        Path file = Path.of("src/test/resources/stops.txt");
        List<Stop> stops = parser.parseStops(file);
        assertEquals(9, stops.size());
        assertEquals("2", stops.get(0).id());
        assertEquals("AL Masjid Al-nabawi (Clock Roundabout)", stops.get(0).name());
    }

    @Test
    void shouldParseTrip() throws Exception {
        GtfsParser parser = new GtfsParser();
        Path file = Path.of("src/test/resources/trips.txt");
        List<Trip> trips = parser.parseTrips(file);

        assertEquals(4, trips.size());
        assertEquals("NORMAL_03_101_Return_22:10", trips.get(0).id());
        assertEquals("101", trips.get(0).routeid());
    }

    @Test
    void shouldParseStopTimeEntry() throws Exception {
        GtfsParser parser = new GtfsParser();
        Path file = Path.of("src/test/resources/stop_times.txt");
        List<StopTimeEntry> stopTimes = parser.parseStopTimeEntries(file);

        assertEquals(4, stopTimes.size());
        assertEquals("NORMAL_03_101_Return_22:10", stopTimes.get(0).tripId());
        assertEquals("2", stopTimes.get(0).stopId());
        assertEquals(Duration.ofHours(22).plusMinutes(10), stopTimes.get(0).arrivalTime()); //pomoč AI
    }
}