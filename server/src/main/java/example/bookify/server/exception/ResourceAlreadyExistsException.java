package example.bookify.server.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<ResourceAlreadyExistsException> supply(String message) {
        return () -> new ResourceAlreadyExistsException(message);
    }

    public static Supplier<ResourceAlreadyExistsException> supply(String message, Object... args) {
        return () -> new ResourceAlreadyExistsException(message, args);
    }
}
