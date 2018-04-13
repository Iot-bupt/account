package cn.edu.bupt.exception;

public class DatabaseException extends RuntimeException {

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

}
