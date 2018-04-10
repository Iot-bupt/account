package cn.edu.bupt.dao;

/**
 * Created by CZX on 2018/4/10.
 */
public class DataValidationException extends RuntimeException{

    public DataValidationException(String message) {
        super(message);
    }

    public DataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
