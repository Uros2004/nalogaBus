package org.example.service;

import org.example.model.StopTimeEntry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//Pomoč AI
public class ArrivalTimeFilter {
    public List<StopTimeEntry> filterWithinWindow(List<StopTimeEntry> entries, Duration now, Duration window) {
        List<StopTimeEntry> filteredEntries = new ArrayList<>();
        Duration windowEnd = now.plus(window);

        for (StopTimeEntry entry : entries) {
            Duration arrival = entry.arrivalTime();
            boolean afterOrAtNow = arrival.compareTo(now) >= 0;
            boolean beforeOrAtWindowEnd = arrival.compareTo(windowEnd) <= 0;

            if (afterOrAtNow && beforeOrAtWindowEnd) {
                filteredEntries.add(entry);
            }
        }
        return filteredEntries;
    }
}
