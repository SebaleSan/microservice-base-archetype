package ${package}.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import ${package}.pattern.ErrorResponseBuilder;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        ErrorResponseBuilder builder = error(HttpStatus.BAD_REQUEST, "Invalid request", request);
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> builder.field(fieldError.getField(), fieldError.getDefaultMessage()));
        return builder.toResponseEntity();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex,
                                                        HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), request).toResponseEntity();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex,
                                                              HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Malformed JSON request", request).toResponseEntity();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Invalid value for parameter '" + ex.getName() + "'", request)
                .toResponseEntity();
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationUnavailable(AuthenticationServiceException ex,
                                                                         HttpServletRequest request) {
        return error(HttpStatus.SERVICE_UNAVAILABLE, "User service unavailable", request).toResponseEntity();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthenticated(AuthenticationException ex,
                                                               HttpServletRequest request) {
        return error(HttpStatus.UNAUTHORIZED, "Invalid or missing credentials", request).toResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex,
                                                            HttpServletRequest request) {
        return error(HttpStatus.FORBIDDEN, "Access denied", request).toResponseEntity();
    }

    private ErrorResponseBuilder error(HttpStatus status, String message, HttpServletRequest request) {
        return ErrorResponseBuilder.builder()
                .status(status)
                .message(message)
                .path(request.getRequestURI());
    }
}
