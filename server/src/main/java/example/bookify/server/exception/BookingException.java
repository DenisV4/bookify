package example.bookify.server.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class BookingException extends RuntimeException {

    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<BookingException> supply(String message) {
        return () -> new BookingException(message);
    }

    public static Supplier<BookingException> supply(String message, Object... args) {
        return () -> new BookingException(message, args);
    }
}
