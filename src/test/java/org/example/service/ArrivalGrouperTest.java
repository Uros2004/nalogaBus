package org.example.service;

import org.example.model.Arrival;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ArrivalGrouperTest {
    private final ArrivalGrouper grouper = new ArrivalGrouper();

    @Test   //Pomoč AI
    void shouldGroupAndLimitPerRoute() {
        List<Arrival> arrivals = List.of(
                new Arrival("101", Duration.ofHours(10).plusMinutes(5)),
                new Arrival("101", Duration.ofHours(10).plusMinutes(20)),
                new Arrival("101", Duration.ofHours(10).plusMinutes(35)),
                new Arrival("103", Duration.ofHours(10).plusMinutes(10))
        );

        Map<String, List<Arrival>> result = grouper.groupByRoute(arrivals, 2);

        assertEquals(2, result.size());
        assertEquals(2, result.get("101").size());
        assertEquals(1, result.get("103").size());

        assertEquals(Duration.ofHours(10).plusMinutes(5), result.get("101").get(0).arrivalTime());
        assertEquals(Duration.ofHours(10).plusMinutes(20), result.get("101").get(1).arrivalTime());
    }
}