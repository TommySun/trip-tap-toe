package service.csv;

public class CsvSerializeException extends RuntimeException{

    public CsvSerializeException(String message, Throwable e) {
        super(message, e);
    }
}
