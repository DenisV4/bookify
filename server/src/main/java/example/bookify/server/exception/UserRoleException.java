package example.bookify.server.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class UserRoleException extends RuntimeException {
    public UserRoleException(String message) {
        super(message);
    }

    public UserRoleException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<RefreshTokenException> supply(String message) {
        return () -> new RefreshTokenException(message);
    }

    public static Supplier<RefreshTokenException> supply(String token, String message) {
        return () -> new RefreshTokenException(token, message);
    }
}
