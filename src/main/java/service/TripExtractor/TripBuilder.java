package service.TripExtractor;

import model.tap.Tap;
import model.trip.Trip;
import model.trip.TripType;
import service.TripExtractor.chargecalculator.ChargeCalculator;

import java.text.DecimalFormat;
import java.time.Duration;

import static model.trip.TripType.*;

public class TripBuilder {

    private static final DecimalFormat currencyFormater = new DecimalFormat("$#,##0.00");

    private final Tap onTap;

    private final Tap offTap;

    private final ChargeCalculator tripChargeCalculator;

    public TripBuilder(Tap onTap, Tap offTap, ChargeCalculator tripChargeCalculator) {
        this.onTap = onTap;
        this.offTap = offTap;
        this.tripChargeCalculator = tripChargeCalculator;
    }



    public Trip build () {

        Trip trip = new Trip();
        trip.setStatus(getTripStatus());

        trip.setStartDateTime(onTap.getDateTimeUTC());
        trip.setFromStopId(onTap.getStopId());

        if (!trip.getStatus().equals(INCOMPLETE)) {
            trip.setFinishedDateTime(offTap.getDateTimeUTC());
            trip.setToStopId(offTap.getStopId());
            trip.setDurationInSec(Duration.between(onTap.getDateTimeUTC(), offTap.getDateTimeUTC()).getSeconds());
        }

        // Amount
        trip.setChargeAmount(currencyFormater.format(tripChargeCalculator.calculateCharge (trip)));

        trip.setCompany(onTap.getCompanyId());
        trip.setBus(onTap.getBusId());
        trip.setPan(onTap.getPan());

        return trip;
    }




    /**
     * Return {@link TripType} according to the stop id in on and off taps
     *
     * @return
     */
    private TripType getTripStatus() {
        TripType type;

        if (offTap == null) {
            type = INCOMPLETE;
        } else if (onTap.getStopId().equals(offTap.getStopId())) {
            type = CANCELED;
        } else {
            type = COMPLETED;
        }

        return type;
    }

}
