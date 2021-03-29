package service.csv.serializer;

import model.CsvSerializable;

import java.util.Collection;
import java.util.Set;


/**
 * Class implement this interface will contain functionality to serialize or deserialize {@link CsvSerializable}
 * objects from and into Csv file
 *
 */
public interface CsvSerializer {

    /**
     * Deserialize CSV file into a Set of {@link CsvSerializable}
     *
     * @param filePath
     * @param pojoType
     * @param <T>
     * @return
     */
    <T extends CsvSerializable> Set<T> deSerialize(String filePath, Class<T> pojoType);


    /**
     * Serialize a Collection of {@link CsvSerializable} into Csv file
     *
     * @param filePath
     * @param pojoType
     * @param pojos
     * @param <T>
     */
    <T extends CsvSerializable> void serialize  (String filePath, Class<T> pojoType, Collection<T> pojos);
}
