package VV.dev.SpringMvc.custom_exceptions.user;

public class UserHasPetException extends RuntimeException {
    public UserHasPetException(String message) {
        super(message);
    }
}
