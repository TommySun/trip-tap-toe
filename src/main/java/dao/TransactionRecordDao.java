package dao;

import model.Query;
import model.transactionrecord.TransactionRecord;

import java.util.Map;
import java.util.Set;

public interface TransactionRecordDao {

    /**
     * Return a collection of {@link TransactionRecord} that is meet the criteria in the Query object.
     *
     *
     * @param query
     * @return A map of TransactionRecord List, group by accountId
     */
    Map<String, Set<TransactionRecord>> getRecords (Query query);

}
