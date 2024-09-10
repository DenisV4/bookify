package example.bookify.server.web.controller;

import example.bookify.server.exception.*;
import example.bookify.server.web.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exc) {
        var message = exc.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.debug("Constraint errors: {}", message);

        return buildErrorResponse(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exc) {
        var message = exc.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "))
                .replaceAll("java.lang.", "");

        log.debug("Request parameter errors: {}", message);

        return buildErrorResponse(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exc) {
        var message = "Invalid value of request parameter " + exc.getParameter().getParameterName();
        log.debug(message);

        return buildErrorResponse(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestParameterException(
            MissingServletRequestParameterException exc) {

        var message = MessageFormat.format(
                "Required request parameter {0} is not present", exc.getParameterName());
        log.debug(message);

        return buildErrorResponse(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exc) {
        log.debug(exc.getMessage());
        return buildErrorResponse("Invalid request body");
    }

    @ExceptionHandler({ResourceAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleResourceAlreadyExistsException(ResourceAlreadyExistsException exc) {
        log.debug(exc.getMessage());
        return buildErrorResponse(exc.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exc) {
        log.debug(exc.getMessage());
        return buildErrorResponse(exc.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exc) {
        log.debug(exc.getMessage());
        return buildErrorResponse(exc.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoResourceFoundException(NoResourceFoundException exc) {
        log.debug(exc.getMessage());
        return buildErrorResponse(exc.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(ResourceNotFoundException exc) {
        log.debug("Error when try to get resource. {}", exc.getMessage());
        return buildErrorResponse(exc.getMessage());
    }

    @ExceptionHandler({BookingException.class, UserRoleException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleBookingException(Exception exc) {
        log.debug(exc.getMessage());
        return buildErrorResponse(exc.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException exc) {
        log.debug(exc.getMessage());
        return buildErrorResponse(exc.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class, RefreshTokenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(Exception exc) {
        log.debug(exc.getMessage());
        return buildErrorResponse(exc.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleExceptions(Exception exc) {
        log.error("Exception occurred", exc);
        return buildErrorResponse("Internal server error");
    }

    private ErrorResponse buildErrorResponse(String message) {
        return ErrorResponse.builder()
                .errorMessage(message)
                .build();
    }
}
