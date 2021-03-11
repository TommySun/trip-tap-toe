package dao.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import dao.DaoException;
import model.Query;
import model.transactionrecord.TransactionRecord;
import dao.TransactionRecordDao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
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

    private final String transactionDataFilePath;

    public SimpleCSVTransactionRecordDao(String transactionDataFilePath) {
        this.transactionDataFilePath = transactionDataFilePath;
    }


    /**
     * Return record
     *
     * @param query
     * @return
     */
    @Override
    public Map<String, Set<TransactionRecord>> getRecords(Query query) {

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.findAndRegisterModules();
        csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        CsvSchema schema = csvMapper.schemaFor(TransactionRecord.class).withHeader();

        ObjectReader objectReader = csvMapper.readerFor(TransactionRecord.class).with(schema);


        try (Reader reader = new FileReader(transactionDataFilePath)) {
            MappingIterator<TransactionRecord> transactionRecordIterator = objectReader.readValues(reader);
            return transactionRecordIterator.readAll().stream()
                    .filter(transactionRecord -> isInclude(transactionRecord, query))
                    .collect(Collectors.groupingBy(TransactionRecord::getFromAccountId, Collectors.toSet()));
        } catch (FileNotFoundException e) {
            throw new DaoException(String.format(FILE_NOT_FOUND_ERROR_MESSAGE, transactionDataFilePath), e);
        } catch (IOException e) {
            throw new DaoException(String.format(FAILE_PARSE_FILE_ERROR_MESSAGE, transactionDataFilePath), e);
        }
    }

    /**
     * Return true if the {@link TransactionRecord} meet the criteria in given Query
     *
     * @param record
     * @param query
     * @return
     */
    private Boolean isInclude (TransactionRecord record, Query query) {
        return (query.getAccountIds() == null || query.getAccountIds().isEmpty() ||
                query.getAccountIds().contains(record.getFromAccountId()))
                && (query.getDateRange() == null || query.getDateRange().contains(record.getCreatedAt()));

    }


}
