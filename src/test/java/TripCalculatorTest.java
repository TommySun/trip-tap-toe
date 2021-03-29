import model.tap.Tap;
import model.trip.Trip;
import org.junit.Test;
import service.TripExtractor.TripExtractor;
import service.TripExtractor.chargecalculator.ChargeCalculator;
import service.TripExtractor.chargecalculator.impl.SimpleChargeCalculator;
import service.csv.serializer.impl.GenericJacksonCsvSerializer;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import static model.trip.TripType.*;
import static org.junit.Assert.*;

/**
 * This test is depending on the csv file "tap-records.csv" under resources folder. It test extract trip date
 * from raw tap data deserialize from the file
 *
 */
public class TripCalculatorTest {

    private final String testCsvFileName = "tap-records.csv";


    /**
     *
     * Testing extract Trip from the test file. The end result should have 4 result with 2 Completed, 1 Canceled
     * and 1 Incomplete.
     */
    @Test
    public void testExtractTripData () {
        GenericJacksonCsvSerializer csvSerializer = new GenericJacksonCsvSerializer();

        ChargeCalculator tripChargeCalculator = new SimpleChargeCalculator();

        Set<Tap> taps = csvSerializer.deSerialize(getFullCsvFilePath(testCsvFileName), Tap.class);
        Set<Trip> trips = new TripExtractor(tripChargeCalculator).extractTrips(taps);

        ArrayList<Trip> result = new ArrayList<>(trips);
        assertEquals(4, result.size());
        assertEquals(COMPLETED, result.get(0).getStatus());
        assertEquals("$3.25", result.get(0).getChargeAmount());

        assertEquals(CANCELED, result.get(1).getStatus());
        assertEquals("$0.00", result.get(1).getChargeAmount());

        assertEquals(COMPLETED, result.get(2).getStatus());
        assertEquals("$7.30", result.get(2).getChargeAmount());

        assertEquals(INCOMPLETE, result.get(3).getStatus());
        assertEquals("$7.30", result.get(3).getChargeAmount());
    }

    /**
     *
     * Return absolute file path for csv file from `resources`
     *
     * @return
     */
    private String getFullCsvFilePath (String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }
}
