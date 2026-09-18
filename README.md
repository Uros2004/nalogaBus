# Bus Arrivals – prikaz prihodov avtobusov na postajališču (GTFS)

Konzolna Java aplikacija, ki na podlagi GTFS podatkov izpiše naslednje prihode
avtobusov na izbranem postajališču v naslednjih 2 urah, po linijah.

## Predpogoji
- JDK 26
- Maven

## Zagon

```bash
mvn compile
java -cp target/classes org.example.cli.Main <station_id> <num_buses_per_line> <relative|absolute>
```

Primer:
```bash
java -cp target/classes org.example.cli.Main 2 3 absolute
```

GTFS podatki (`routes.txt`, `stops.txt`, `trips.txt`, `stop_times.txt`) so vključeni v
`src/main/resources/gtfs/`.

## Testi

```bash
mvn test
```

Vsebuje unit teste (parser, filter, grouper, formatter) in integracijski test
(`BusArrivalServiceTest`), ki bere testne GTFS datoteke iz `src/test/resources`.

## Arhitektura

- `model` — nespremenljive (record) podatkovne entitete: `Stop`, `Route`, `Trip`,
  `StopTimeEntry`, `Arrival`
- `gtfs` — branje in parsanje GTFS CSV datotek (`GtfsParser`)
- `service` — poslovna logika, ločena po odgovornosti:
    - `ArrivalTimeFilter` — filtrira prihode znotraj 2h okna
    - `ArrivalGrouper` — grupira po liniji, omeji na N na linijo
    - `ArrivalFormatter` — formatira čas (absolute/relative)
    - `BusArrivalService` — orkestrira zgornje
- `cli` — `Main`, parsanje argumentov ukazne vrstice

## Odločitve in znane omejitve

- **`calendar.txt` ni uporabljen** — testni GTFS podatki imajo veljavnost omejeno na
  obdobje februar–maj 2020, kar ne ustreza dejanskemu datumu poganjanja. Aplikacija
  filtrira samo po uri dneva, ne po dnevu v tednu/datumu.
- **GTFS časi lahko presežejo 24:00:00** (prihod po polnoči istega voznega dne) — za
  to se uporablja `java.time.Duration` namesto `LocalTime`, ker `LocalTime` ne podpira
  ur nad 23.
- **`BusArrivalService.toArrival()`** trenutno predpostavlja, da vsak `trip_id` in
  `route_id` obstajata v ustreznih mapah — pri poškodovanih/nepopolnih podatkih bi
  vrglo `NullPointerException`.

## Razmislek: kako narediti nalogo zahtevnejšo


- Uporaba `calendar.txt`/`calendar_dates.txt` za pravilno filtriranje po dnevu v tednu
  in veljavnosti storitve, ne samo po uri dneva.

- Header-based mapiranje stolpcev (branje imen stolpcev iz prve vrstice) namesto
  trdo kodiranih indeksov `parts[0]`, `parts[2]` — bolj robustno na spremembe v
  vrstnem redu stolpcev.
- Podpora poizvedbi za več postajališč naenkrat.

## Uporaba AI orodij

Pri izdelavi naloge sem uporabil AI (Claude). Konkretno je AI napisal:
- `BusArrivalService.java` in `Main.java`
- `parseGtfsTime()` helper metodo v `GtfsParser`

Preostalo kodo (modeli, `GtfsParser` branje, `ArrivalTimeFilter`, `ArrivalGrouper`,
`ArrivalFormatter`, teste) sem pisal sam.
