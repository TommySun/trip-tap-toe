package model.balancecalculation;

import com.google.common.collect.Range;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The class contain the Balance calculate result
 *
 *
 */
public class BalanceCalculateResult {

   private final String accountId;
   private final BigDecimal balance;
   private final Integer numberOfRecords;
   private final Range<LocalDateTime> dataRange;

    public BalanceCalculateResult(String accountId, BigDecimal balance, Integer numberOfRecords, Range<LocalDateTime> dataRange) {
        this.accountId = accountId;
        this.balance = balance;
        this.numberOfRecords = numberOfRecords;
        this.dataRange = dataRange;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Range<LocalDateTime> getDataRange() {
        return dataRange;
    }

    public Integer getNumberOfRecords() {
        return numberOfRecords;
    }
}
