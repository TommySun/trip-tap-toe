

import model.tap.Tap;
import model.trip.Trip;
import service.TripExtractor.TripExtractor;
import service.TripExtractor.chargecalculator.ChargeCalculator;
import service.TripExtractor.chargecalculator.impl.SimpleChargeCalculator;
import service.csv.serializer.impl.GenericJacksonCsvSerializer;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Accept user input from CMD and calculate the account balance
 *
 *
 */
public class SimpleConsole {



    public static void main(String[] args) {


        String filePath;

        if(args.length > 0) {
            filePath = args[0];
        } else {
            filePath = "demo.csv";
        }

        GenericJacksonCsvSerializer csvSerializer = new GenericJacksonCsvSerializer();

        ChargeCalculator tripChargeCalculator = new SimpleChargeCalculator();

        Set<Tap> taps = csvSerializer.deSerialize(filePath, Tap.class);
        Set<Trip> trips = new TripExtractor(tripChargeCalculator).extractTrips(taps);

        if (trips.size() > 0) {
            csvSerializer.serialize("output.csv", Trip.class, trips);
        } else {
            System.out.println("No record found");
        }

        System.exit(0);
    }


}
