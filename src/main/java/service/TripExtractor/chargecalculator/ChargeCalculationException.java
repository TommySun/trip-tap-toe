package service.TripExtractor.chargecalculator;


/**
 * Exception thrown when charge cannot be calculated
 *
 */
public class ChargeCalculationException extends RuntimeException {

    public ChargeCalculationException(String message) {
        super(message);
    }
}
