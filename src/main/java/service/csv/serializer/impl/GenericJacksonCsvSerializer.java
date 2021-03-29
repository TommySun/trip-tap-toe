package service.csv.serializer.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import model.CsvSerializable;
import service.csv.CsvSerializeException;
import service.csv.serializer.CsvSerializer;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * This class handles serialize and deserialize {@link CsvSerializable} objects into and from CSV files
 */
public class GenericJacksonCsvSerializer implements CsvSerializer {

    private final String FILE_NOT_FOUND_ERROR_MESSAGE = "Cannot find file [%s]";

    private final String FAILED_PARSE_FILE_ERROR_MESSAGE = "Failed to parse file [%s]";

    private final String FAILED_TO_WRITE_INTO_FILE = "Failed to write into file [%s]";


    @Override
    public <T extends CsvSerializable>  Set<T> deSerialize(String filePath, Class<T> pojoType) {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.findAndRegisterModules();
        csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        CsvSchema schema = csvMapper.schemaFor(pojoType).withHeader();

        ObjectReader objectReader = csvMapper.readerFor(pojoType).with(schema);


        try (Reader reader = new FileReader(filePath)) {
            MappingIterator<T> transactionRecordIterator = objectReader.readValues(reader);
            return new HashSet<>(transactionRecordIterator.readAll());
        } catch (FileNotFoundException e) {
            throw new CsvSerializeException(String.format(FILE_NOT_FOUND_ERROR_MESSAGE, filePath), e);
        } catch (IOException e) {
            throw new CsvSerializeException(String.format(FAILED_PARSE_FILE_ERROR_MESSAGE, filePath), e);
        }
    }

    @Override
    public <T extends CsvSerializable> void serialize(String filePath, Class<T> pojoType, Collection<T> pojos) {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.findAndRegisterModules();
        csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        CsvSchema schema = csvMapper.schemaFor(pojoType).withHeader();

        ObjectWriter writer = csvMapper.writerFor(pojoType).with(schema);

        File outPutFile = new File(filePath);

        try {
            writer.writeValues(outPutFile).writeAll(pojos);
        } catch (IOException e) {
            throw new CsvSerializeException(String.format(FAILED_TO_WRITE_INTO_FILE, filePath), e);
        }
    }


}
