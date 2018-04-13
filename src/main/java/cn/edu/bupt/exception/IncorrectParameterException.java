package cn.edu.bupt.exception;


public class IncorrectParameterException extends RuntimeException {

    public IncorrectParameterException(String message) {
        super(message);
    }

    public IncorrectParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
