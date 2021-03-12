import com.google.common.collect.Range;
import dao.TransactionRecordDao;
import dao.impl.SimpleCSVTransactionRecordDao;
import model.Query;
import model.balancecalculation.BalanceCalculateResult;
import service.balancecalculation.impl.SimpleBalanceCalculator;


import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Accept user input from CMD and calculate the account balance
 *
 *
 */
public class SimpleConsole {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DecimalFormat currencyFormater = new DecimalFormat("$#,##0.00;-$#,##0.00");

    public static void main(String[] args) {
        Query query = parseQuery(args);

        String filePath;

        if(args.length > 0) {
            filePath = args[0];
        } else {
            filePath = "transaction.csv";
        }

        TransactionRecordDao transactionRecordDao = new SimpleCSVTransactionRecordDao(filePath);

        List<BalanceCalculateResult> resultList = new SimpleBalanceCalculator(transactionRecordDao).calculateBalance(query);

        if (resultList != null && !resultList.isEmpty()) {
            resultList.stream().forEach(result -> {
                System.out.println("____________________________________________________________________");
                System.out.println(result.getAccountId() + ":");
                System.out.println(String.format("Relative balance for the period is: %s", currencyFormater.format(result.getBalance())));
                System.out.println(String.format("Number of transactions included is: %d", result.getNumberOfRecords()));
                System.out.println("____________________________________________________________________");

            });
        } else {
            System.out.println("No Record found");
        }

        System.exit(0);
    }


    /**
     * Parse the argument from CMD to a {@link Query} object
     *
     * @param cmdArgs
     * @return
     */
    private static Query parseQuery(String[] cmdArgs) {

        AtomicReference<Set<String>> accountIds = new AtomicReference<>();
        AtomicReference<LocalDateTime> fromDate = new AtomicReference<>();
        AtomicReference<LocalDateTime> toDate = new AtomicReference<>();

        if(cmdArgs.length > 1) {
            IntStream.range(1, cmdArgs.length).forEach(index -> {
                switch (index) {
                    case 1:
                        accountIds.set(Arrays.stream(cmdArgs[1].split(",")).map(String::trim).collect(Collectors.toSet()));
                        break;

                    case 2:
                        fromDate.set(LocalDateTime.parse(cmdArgs[2], dateTimeFormatter));
                        break;

                    case 3:
                        toDate.set(LocalDateTime.parse(cmdArgs[3], dateTimeFormatter));
                        break;
                }
            });
        }


        Range<LocalDateTime> dateTimeRange = null;

        if(fromDate.get() != null && toDate.get() != null) {
            dateTimeRange = Range.closed(fromDate.get(), toDate.get());
        } else if (toDate.get() != null) {
            dateTimeRange = Range.atMost(toDate.get());
        } else if (fromDate.get() != null){
            dateTimeRange = Range.atLeast(fromDate.get());
        }


        return new Query(accountIds.get(), dateTimeRange);
    }

}
