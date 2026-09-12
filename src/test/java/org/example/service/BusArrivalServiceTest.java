package org.example.service;

import org.example.gtfs.GtfsParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusArrivalServiceTest {

    private final BusArrivalService service =
            new BusArrivalService(new GtfsParser(), Path.of("src/test/resources"));

    @Test
    void shouldReturnArrivalsWithinWindow() throws Exception {
        Duration now = Duration.ofHours(22);       // fiksen "trenutni" čas, NE LocalTime.now()
        Duration window = Duration.ofHours(2);

        List<String> result = service.getUpcomingArrivals("2", 3, "absolute", now, window);

        assertEquals(List.of(
                "Postajališče: AL Masjid Al-nabawi (Clock Roundabout)",
                "101: 22:10"
        ), result);
    }
}