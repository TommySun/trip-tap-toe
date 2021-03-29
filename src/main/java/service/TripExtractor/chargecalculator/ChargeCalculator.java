package service.TripExtractor.chargecalculator;

import model.trip.Trip;

import java.math.BigDecimal;

/**
 * Class implement this interface will provide functionality to calculate the charge off a trip
 *
 */
public interface ChargeCalculator {

    BigDecimal calculateCharge(Trip trip);
}
