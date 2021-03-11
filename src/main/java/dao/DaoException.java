package dao;

/**
 * Exception thrown at DAO layer
 *
 */
public class DaoException extends RuntimeException {

    public DaoException(String message){
        super(message);
    }


    public DaoException(String message, Throwable cause){
        super(message, cause);
    }
}
