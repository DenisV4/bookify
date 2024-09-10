package example.bookify.server.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<ResourceNotFoundException> supply(String message) {
        return () -> new ResourceNotFoundException(message);
    }

    public static Supplier<ResourceNotFoundException> supply(String message, Object... args) {
        return () -> new ResourceNotFoundException(message, args);
    }
}
