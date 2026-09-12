package org.example.cli;

import org.example.gtfs.GtfsParser;
import org.example.service.BusArrivalService;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class Main {
    static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Uporaba: busTrips <station_id> <num_buses_per_line> <relative|absolute>");
            return;
        }

        String stopId = args[0];

        int maxPerRoute;
        try {
            maxPerRoute = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("num_buses_per_line mora biti celo število, dobil sem: " + args[1]);
            return;
        }

        //Pomoč AI
        String format = args[2];
        if (!format.equals("relative") && !format.equals("absolute")) {
            System.err.println("Tretji argument mora biti 'relative' ali 'absolute', dobil sem: " + format);
            return;
        }

        Clock clock = Clock.systemDefaultZone();
        Duration now = Duration.between(LocalTime.MIDNIGHT, LocalTime.now(clock));
        Duration window = Duration.ofHours(2);

        Path gtfsDirectory = Path.of("src/main/resources/gtfs");
        GtfsParser parser = new GtfsParser();
        BusArrivalService service = new BusArrivalService(parser, gtfsDirectory);

        //Pomoč AI
        List<String> result;
        try {
            result = service.getUpcomingArrivals(stopId, maxPerRoute, format, now, window);
        } catch (IOException e) {
            System.err.println("Napaka pri branju GTFS podatkov: " + e.getMessage());
            return;
        }

        for (String line : result) {
            System.out.println(line);
        }
    }
}