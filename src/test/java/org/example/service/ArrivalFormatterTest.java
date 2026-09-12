package org.example.service;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ArrivalFormatterTest {
    private final ArrivalFormatter formatter = new ArrivalFormatter();

    @Test   //Pomoč AI
    void shouldFormatAbsoluteNormalTime() {
        Duration time = Duration.ofHours(10).plusMinutes(5);
        assertEquals("10:05", formatter.formatAbsolute(time));
    }

    @Test
    void shouldFormatAbsolutePastMidnight() {
        Duration time = Duration.ofHours(25).plusMinutes(10);
        assertEquals("01:10", formatter.formatAbsolute(time));
    }

    @Test
    void shouldFormatRelative() {
        Duration now = Duration.ofHours(10);
        Duration arrival = Duration.ofHours(10).plusMinutes(15);
        assertEquals("15min", formatter.formatRelative(arrival, now));
    }
}