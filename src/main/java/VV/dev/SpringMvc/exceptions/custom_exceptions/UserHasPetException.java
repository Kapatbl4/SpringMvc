package VV.dev.SpringMvc.exceptions.custom_exceptions;

public class UserHasPetException extends RuntimeException {
    public UserHasPetException(String message) {
        super(message);
    }
}
