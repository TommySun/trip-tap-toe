package service.balancecalculation;

import model.Query;
import model.balancecalculation.BalanceCalculateResult;
import model.transactionrecord.TransactionRecord;

import java.util.List;

public interface AccountBalanceCalculator {

    /**
     * Calculate Account balance over the {@link TransactionRecord} data that meet the given query
     *
     * @param query
     * @return
     */
    List<BalanceCalculateResult> calculateBalance (Query query);
}
