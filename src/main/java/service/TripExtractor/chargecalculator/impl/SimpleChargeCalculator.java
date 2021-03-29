package service.TripExtractor.chargecalculator.impl;


import model.trip.Trip;
import service.TripExtractor.chargecalculator.ChargeCalculationException;
import service.TripExtractor.chargecalculator.ChargeCalculator;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Calculate Trip charge only based on it's status
 *
 */
public class SimpleChargeCalculator implements ChargeCalculator {

    private final String[] STOPS = new String[] {"Stop1", "Stop2", "Stop3"};


    /**
     * Calculate Charge on given trip
     *
     * @param trip
     * @return
     */
    @Override
    public BigDecimal calculateCharge(Trip trip) {

        if(trip == null || trip.getStatus() == null ) {
            throw new ChargeCalculationException("Cannot Calculate Charge with empty trip or trip with out status");
        }

        BigDecimal charge;


        switch (trip.getStatus()) {
            case CANCELED:
                charge = BigDecimal.ZERO;
                break;

            case INCOMPLETE:
                charge = findMaxCharge (trip.getFromStopId());
                break;

            case COMPLETED:
                charge = calculateCharge(trip.getFromStopId(), trip.getToStopId());
                break;

            default:
                throw new ChargeCalculationException(String.format("Unknown trip status [%s]", trip.getStatus()));
        }


        return charge;
    }


    /**
     * Calculate Trip charge between from and to stop
     *
     * @param fromStopId
     * @param toStopId
     * @return
     */
    private BigDecimal calculateCharge(String fromStopId, String toStopId) {
        BigDecimal charge = BigDecimal.ZERO;

        // If there are 2 stops between from and two stops return max charge
        if(Math.abs(Arrays.asList(STOPS).indexOf(fromStopId) - Arrays.asList(STOPS).indexOf(toStopId)) == 2) {
            charge = new BigDecimal(7.30d);
        }
        // Trip between stop 1 and stop 2
        else if ((fromStopId.equals(STOPS[0]) || fromStopId.equals(STOPS[1])) && (toStopId.equals(STOPS[0]) || toStopId.equals(STOPS[1]))) {
            charge = new BigDecimal(3.25d);
        } else {
            charge = new BigDecimal(5.50d);
        }

        return charge;
    }

    /**
     * Return max charge
     *
     * @param fromStopId
     * @return
     */
    private BigDecimal findMaxCharge(String fromStopId) {
        BigDecimal maxCharge = BigDecimal.ZERO;

        if(fromStopId.equals(STOPS[0]) || fromStopId.equals(STOPS[2])) {
            maxCharge = new BigDecimal(7.30d);
        } else if (fromStopId.equals(STOPS[1])) {
            maxCharge = new BigDecimal(5.50d);
        }

        return maxCharge;
    }
}
