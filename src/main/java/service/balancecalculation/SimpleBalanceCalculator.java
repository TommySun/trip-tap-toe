package service.balancecalculation;

import dao.TransactionRecordDao;
import model.Query;
import model.balancecalculation.BalanceCalculateResult;
import model.transactionrecord.TransactionRecord;
import model.transactionrecord.TransactionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static model.transactionrecord.TransactionType.PAYMENT;
import static model.transactionrecord.TransactionType.REVERSAL;

public class SimpleBalanceCalculator implements AccountBalanceCalculator {

    private final TransactionRecordDao transactionRecordDao;

    public SimpleBalanceCalculator(TransactionRecordDao transactionRecordDao) {
        this.transactionRecordDao = transactionRecordDao;
    }


    /**
     * Calculate Account balance over the {@link TransactionRecord} data that meet the given query.
     * by simple sum up Reversal and Payment records. And subtract total Payment amount by total
     * Reversal amount
     *
     * @param query
     * @return
     */
    public List<BalanceCalculateResult> calculateBalance (Query query) {
        Map<String, Set<TransactionRecord>> accountTransactionRecords = transactionRecordDao.getRecords(query);

        List<BalanceCalculateResult> accountBalanceResult = new ArrayList<>();
        accountTransactionRecords.entrySet().stream().forEach(accountTransactionSet -> {

            BigDecimal totalPaymentAmount = getTotalValue(accountTransactionSet.getValue(), PAYMENT);
            BigDecimal totalReverseAmount = getTotalValue(accountTransactionSet.getValue(), REVERSAL);

            accountBalanceResult.add(new BalanceCalculateResult(accountTransactionSet.getKey(),
                    totalReverseAmount.subtract(totalPaymentAmount) , accountTransactionSet.getValue().size(), query.getDateRange()));
        });

        return accountBalanceResult;
    }

    /**
     * Return a total amount of given {@link TransactionType} from a Set of TransactionRecords. Return 0 if no
     * {@link TransactionRecord} with givent {@link TransactionType} found.
     *
     *
     * @param transactionRecords
     * @param type
     * @return
     */
    private BigDecimal getTotalValue (Set<TransactionRecord> transactionRecords, TransactionType type) {

        return transactionRecords.stream()
                .filter(transactionRecord -> transactionRecord.getTransactionType().equals(type))
                .map(TransactionRecord::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


}
