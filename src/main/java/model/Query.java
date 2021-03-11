package model;

import com.google.common.collect.Range;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Contain criteria that Record will be selected from data store
 *
 */
public class Query {

    private Set<String> accountIds;

    private Range<LocalDateTime> dateRange;

    public Query(Set<String> accountIds, Range<LocalDateTime> dateRange) {
        this.accountIds = accountIds;
        this.dateRange = dateRange;
    }

    public Set<String> getAccountIds() {
        return accountIds;
    }


    public Range<LocalDateTime> getDateRange() {
        return dateRange;
    }

}
