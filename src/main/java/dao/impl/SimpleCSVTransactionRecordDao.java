package dao.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import dao.DaoException;
import dao.TransactionRecordDao;
import model.Query;
import model.transactionrecord.TransactionRecord;
import model.transactionrecord.TransactionType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provide the caller with Transaction Records from the data source of CSV file at the given file path
 *
 * This class requires dependency on Jackson CSV
 *
 */
public class SimpleCSVTransactionRecordDao implements TransactionRecordDao {

    private final String FILE_NOT_FOUND_ERROR_MESSAGE = "Cannot find file [%s]";

    private final String FAILE_PARSE_FILE_ERROR_MESSAGE = "Failed to parse file [%s]";

    private final Set<TransactionRecord> records;

    public SimpleCSVTransactionRecordDao(String transactionDataFilePath) {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.findAndRegisterModules();
        csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        CsvSchema schema = csvMapper.schemaFor(TransactionRecord.class).withHeader();

        ObjectReader objectReader = csvMapper.readerFor(TransactionRecord.class).with(schema);


        try (Reader reader = new FileReader(transactionDataFilePath)) {
            MappingIterator<TransactionRecord> transactionRecordIterator = objectReader.readValues(reader);
            records = new HashSet<>(transactionRecordIterator.readAll());
        } catch (FileNotFoundException e) {
            throw new DaoException(String.format(FILE_NOT_FOUND_ERROR_MESSAGE, transactionDataFilePath), e);
        } catch (IOException e) {
            throw new DaoException(String.format(FAILE_PARSE_FILE_ERROR_MESSAGE, transactionDataFilePath), e);
        }
    }


    /**
     * Return records grouped by the accountId that is meet the query criteria and include no matching reversal
     *
     * @param query
     * @return
     */
    @Override
    public Map<String, Set<TransactionRecord>> getRecords(Query query) {
        return records.stream()
                .filter(record -> !record.getTransactionType().equals(TransactionType.REVERSAL) && isInclude(record, query))
                .collect(Collectors.groupingBy(TransactionRecord::getFromAccountId, Collectors.toSet()));
    }

    /**
     * Return true if the {@link TransactionRecord} meet the criteria in given Query, and has no match reversal
     * transaction
     *
     * @param record
     * @param query
     * @return
     */
    private Boolean isInclude (TransactionRecord record, Query query) {
        return (query.getAccountIds() == null || query.getAccountIds().isEmpty() ||
                query.getAccountIds().contains(record.getFromAccountId()))
                && (query.getDateRange() == null || query.getDateRange().contains(record.getCreatedAt()))
                &&
                !records.stream()
                        .anyMatch(otherRecord -> otherRecord.getRelatedTransaction().equals(record.getTransactionId()));

    }


}
