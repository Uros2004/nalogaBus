package org.example.service;

import org.example.model.StopTimeEntry;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrivalTimeFilterTest {

    private final ArrivalTimeFilter filter = new ArrivalTimeFilter();

    @Test //pomoč AI
    void shouldKeepArrivalInsideWindow() {
        Duration now = Duration.ofHours(10);
        Duration window = Duration.ofHours(2);

        StopTimeEntry entry = new StopTimeEntry("trip1", "stop1", Duration.ofHours(11));

        List<StopTimeEntry> result = filter.filterWithinWindow(List.of(entry), now, window);

        assertEquals(1, result.size());
        assertEquals(entry, result.get(0));
    }

    @Test
    void shouldKeepArrivalExactlyAtWindowEnd() {
        Duration now = Duration.ofHours(10);
        Duration window = Duration.ofHours(2);

        StopTimeEntry entry = new StopTimeEntry("trip1", "stop1", Duration.ofHours(12));

        List<StopTimeEntry> result = filter.filterWithinWindow(List.of(entry), now, window);

        assertEquals(1, result.size());
    }

    @Test
    void shouldDropArrivalOutsideWindow() {
        Duration now = Duration.ofHours(10);
        Duration window = Duration.ofHours(2);

        StopTimeEntry entry = new StopTimeEntry("trip1", "stop1", Duration.ofHours(12).plusMinutes(1));

        List<StopTimeEntry> result = filter.filterWithinWindow(List.of(entry), now, window);

        assertTrue(result.isEmpty());
    }
}