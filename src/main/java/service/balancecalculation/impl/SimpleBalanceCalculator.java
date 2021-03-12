package service.balancecalculation.impl;

import dao.TransactionRecordDao;
import model.Query;
import model.balancecalculation.BalanceCalculateResult;
import model.transactionrecord.TransactionRecord;
import model.transactionrecord.TransactionType;
import service.balancecalculation.AccountBalanceCalculator;

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
     * By totalling the amount under each accountId
     *
     * @param query
     * @return
     */
    public List<BalanceCalculateResult> calculateBalance (Query query) {
        Map<String, Set<TransactionRecord>> accountTransactionRecords = transactionRecordDao.getRecords(query);

        List<BalanceCalculateResult> accountBalanceResult = new ArrayList<>();
        accountTransactionRecords.entrySet().stream().forEach(accountTransactionSet -> {

        // Assume starting balance is always 0
        BigDecimal startingBalance = BigDecimal.ZERO;

        BigDecimal total = accountTransactionSet.getValue().stream()
                    .map(TransactionRecord::getAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            accountBalanceResult.add(new BalanceCalculateResult(accountTransactionSet.getKey(),
                    startingBalance.subtract(total) , accountTransactionSet.getValue().size(), query.getDateRange()));
        });

        return accountBalanceResult;
    }



}
