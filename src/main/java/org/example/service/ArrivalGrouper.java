package org.example.service;

import org.example.model.Arrival;
import org.example.model.StopTimeEntry;

import java.util.*;

//Pomoč AI
public class ArrivalGrouper {
    public Map<String, List<Arrival>> groupByRoute(List<Arrival> arrivals, int maxPerRoute) {
        Map<String, List<Arrival>> map = new TreeMap<>();

        for (Arrival arrival : arrivals) {
            map.computeIfAbsent(arrival.routeShortName(), k -> new ArrayList<>()).add(arrival);
        }
        for (List<Arrival> group : map.values()) {
            group.sort(Comparator.comparing(Arrival::arrivalTime));
            if (group.size() > maxPerRoute) {
                group.subList(maxPerRoute, group.size()).clear();
            }
        }

        return map;
    }
}
