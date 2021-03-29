package service.TripExtractor;

import model.tap.Tap;
import model.trip.Trip;
import service.TripExtractor.chargecalculator.ChargeCalculator;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static model.tap.TapType.*;

/**
 * Extract Trip Data from a Set of Raw tap data
 *
 *
 */
public class TripExtractor {


    private final ChargeCalculator tripChargeCalculator;

    public TripExtractor(ChargeCalculator tripChargeCalculator) {
        this.tripChargeCalculator = tripChargeCalculator;
    }


    /**
     * Return a Set of {@link Trip} extract from given raw {@link Tap} data set
     *
     * @param taps
     * @return
     */
    public Set<Trip> extractTrips (Set<Tap> taps) {
        if(taps == null || taps.isEmpty()) {
            return new HashSet<>();
        }

        Map<String, Set<Tap>> panTapMap = taps.stream().collect(Collectors.groupingBy(Tap::getPan, Collectors.toSet()));

        AtomicReference<HashSet<Trip>> tripSet = new AtomicReference<>();

        tripSet.set(new HashSet<>());
        panTapMap.entrySet().parallelStream().forEach(entry -> {

            Set<Tap> allTaps = entry.getValue();
            Set<Tap> onTaps = allTaps.stream()
                                     .filter(tap -> tap.getTapType().equals(ON)).collect(Collectors.toSet());

            allTaps.removeAll(onTaps);

            List<Trip> trips = onTaps.stream().map(onTap -> {
                Tap offTap = findMatchingOffTap(onTap, allTaps);
                return new TripBuilder(onTap, offTap, tripChargeCalculator).build();
            }).collect(Collectors.toList());

            tripSet.get().addAll(trips);

        });

        return tripSet.get().stream().sorted(Comparator.comparing(Trip::getStartDateTime))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    /**
     * Find the matching off tap of given on Tap. Return null if nothing find
     *
     * @param onTap
     * @param taps
     * @return
     */
    private Tap findMatchingOffTap (Tap onTap, Set<Tap> taps) {
        return taps.stream()
                .sorted(Comparator.comparing(Tap::getDateTimeUTC))
                .filter(tap -> tap.getTapType().equals(OFF)
                        && tap.getPan().equals(onTap.getPan())
                        && tap.getDateTimeUTC().isAfter(onTap.getDateTimeUTC())
                        && tap.getBusId().equals(onTap.getBusId())
                        && tap.getCompanyId().equals(onTap.getCompanyId()))
                .findFirst().orElse(null);
    }

}
