package example.bookify.server.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(String token, String message) {
        super(MessageFormat.format("Error trying to refresh by token: {0}: {1}", token, message));
    }

    public static Supplier<RefreshTokenException> supply(String message) {
        return () -> new RefreshTokenException(message);
    }

    public static Supplier<RefreshTokenException> supply(String token, String message) {
        return () -> new RefreshTokenException(token, message);
    }
}
