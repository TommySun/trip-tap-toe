import com.google.common.collect.Range;
import dao.DaoException;
import dao.impl.SimpleCSVTransactionRecordDao;
import model.Query;
import model.balancecalculation.BalanceCalculateResult;
import org.junit.Test;
import service.balancecalculation.impl.SimpleBalanceCalculator;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class SimpleBalanceCalculatorTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final String testCsvFileName = "transaction-records.csv";


    /**
     * Test given accountId to the SimpleBalanceCalculator.
     *
     * The expected behaviour is the calculation will only be based on the given accountId and time range
     *
     */
    @Test
    public void TestGivenAccountAndDateRange () {

        SimpleCSVTransactionRecordDao transactionRecordDao = new SimpleCSVTransactionRecordDao(getFullCsvFilePath());

        SimpleBalanceCalculator calculator = new SimpleBalanceCalculator(transactionRecordDao);


        Set<String> accountIds = new HashSet<>();
        accountIds.add("ACC334455");

        LocalDateTime fromDate = LocalDateTime.parse("20/10/2018 12:00:00", formatter);
        LocalDateTime toDate = LocalDateTime.parse("20/10/2018 19:00:00", formatter);

        Query query = new Query(accountIds, Range.closed(fromDate, toDate));


        List<BalanceCalculateResult> resultList = calculator.calculateBalance(query);
        assertEquals(1, resultList.size());
        assertEquals("ACC334455", resultList.get(0).getAccountId());
        assertEquals(Integer.valueOf(1), resultList.get(0).getNumberOfRecords());
        assertEquals(0, BigDecimal.valueOf(-25.00d).compareTo(resultList.get(0).getBalance()));
    }

    /**
     * Test NO given accountId to the SimpleBalanceCalculator.
     *
     * The expected behaviour is the calculation will only be based all accountId and given time range
     *
     */
    @Test
    public void TestNoGivenAccountAndGivenDateRange () {

        SimpleCSVTransactionRecordDao transactionRecordDao = new SimpleCSVTransactionRecordDao(getFullCsvFilePath());

        SimpleBalanceCalculator calculator = new SimpleBalanceCalculator(transactionRecordDao);



        LocalDateTime fromDate = LocalDateTime.parse("20/10/2018 12:00:00", formatter);
        LocalDateTime toDate = LocalDateTime.parse("20/10/2018 19:00:00", formatter);

        Query query = new Query(null,Range.closed(fromDate, toDate));


        List<BalanceCalculateResult> resultList = calculator.calculateBalance(query).stream()
                .sorted(Comparator.comparing(BalanceCalculateResult::getAccountId)).collect(Collectors.toList());


        assertEquals(2, resultList.size());
        assertEquals("ACC334455", resultList.get(0).getAccountId());
        assertEquals(Integer.valueOf(1), resultList.get(0).getNumberOfRecords());
        assertEquals(0, BigDecimal.valueOf(-25.0d).compareTo(resultList.get(0).getBalance()));

        assertEquals("ACC998877", resultList.get(1).getAccountId());
        assertEquals(Integer.valueOf(1), resultList.get(1).getNumberOfRecords());
        assertEquals(0, BigDecimal.valueOf(-5.00d).compareTo(resultList.get(1).getBalance()));
    }


    /**
     * Test NO given accountId and Date Range to the SimpleBalanceCalculator.
     *
     * The expected behaviour is the calculation will be based on All records
     *
     */
    @Test
    public void TestNoGivenAccountAndNoGivenDateRange () {

        SimpleCSVTransactionRecordDao transactionRecordDao = new SimpleCSVTransactionRecordDao(getFullCsvFilePath());

        SimpleBalanceCalculator calculator = new SimpleBalanceCalculator(transactionRecordDao);
        Query query = new Query(null, null);

        List<BalanceCalculateResult> resultList = calculator.calculateBalance(query).stream()
                .sorted(Comparator.comparing(BalanceCalculateResult::getAccountId)).collect(Collectors.toList());

        assertEquals(2, resultList.size());
        assertEquals("ACC334455", resultList.get(0).getAccountId());
        assertEquals(Integer.valueOf(2), resultList.get(0).getNumberOfRecords());
        assertEquals(0, BigDecimal.valueOf(-32.25d).compareTo(resultList.get(0).getBalance()));

        assertEquals("ACC998877", resultList.get(1).getAccountId());
        assertEquals(Integer.valueOf(1), resultList.get(1).getNumberOfRecords());
        assertEquals(0, BigDecimal.valueOf(-5.00d).compareTo(resultList.get(1).getBalance()));
    }

    /**
     * Test Only from date is given
     *
     * The expected behaviour is the calculation will be based on All records that is started with the given from date
     *
     */
    @Test
    public void TestOnlyFromDateGiven () {

        SimpleCSVTransactionRecordDao transactionRecordDao = new SimpleCSVTransactionRecordDao(getFullCsvFilePath());

        SimpleBalanceCalculator calculator = new SimpleBalanceCalculator(transactionRecordDao);


        Set<String> accountIds = new HashSet<>();
        accountIds.add("ACC334455");

        LocalDateTime fromDate = LocalDateTime.parse("20/10/2018 17:00:00", formatter);


        Query query = new Query(accountIds, Range.greaterThan(fromDate));


        List<BalanceCalculateResult> resultList = calculator.calculateBalance(query);
        assertEquals(1, resultList.size());
        assertEquals("ACC334455", resultList.get(0).getAccountId());
        assertEquals(Integer.valueOf(1), resultList.get(0).getNumberOfRecords());
        assertEquals(0, BigDecimal.valueOf(-7.25d).compareTo(resultList.get(0).getBalance()));
    }

    /**
     * Test when given file path cannot be found.
     *
     * Expected behaviour is throwing {@link DaoException} so the caller can handle it.
     *
     */
    @Test (expected = DaoException.class)
    public void TestNoFileFoundException () {
        SimpleCSVTransactionRecordDao transactionRecordDao = new SimpleCSVTransactionRecordDao("Dumb.csv");

        SimpleBalanceCalculator calculator = new SimpleBalanceCalculator(transactionRecordDao);
        Query query = new Query(null ,null);

        calculator.calculateBalance(query);
    }


    /**
     *
     * Return absolute file path for csv file from `resources`
     *
     * @return
     */
    private String getFullCsvFilePath () {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(testCsvFileName).getFile());
        return file.getAbsolutePath();
    }
}
