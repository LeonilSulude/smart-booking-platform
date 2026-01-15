package leonil.sulude.catalog.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles deserialization errors, especially for invalid enum values.
     * Example: when a request provides an unknown category.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        // Specific handling for enum parsing errors
        if (cause instanceof InvalidFormatException formatException) {
            String field = formatException.getPath().get(0).getFieldName();
            String rejectedValue = String.valueOf(formatException.getValue());

            // Extract expected type (usually an enum) and possible values
            String targetType = formatException.getTargetType().getSimpleName();
            String validValues = "";
            if (formatException.getTargetType().isEnum()) {
                Object[] constants = formatException.getTargetType().getEnumConstants();
                validValues = "Valid options: " + List.of(constants);
            }

            FieldErrorDetails error = new FieldErrorDetails(
                    field,
                    rejectedValue,
                    "Invalid value for type " + targetType + ". " + validValues
            );

            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Invalid input",
                    LocalDateTime.now(),
                    List.of(error)
            );

            return ResponseEntity.badRequest().body(apiError);
        }

        // Generic fallback for other unreadable payloads (e.g. malformed JSON)
        ApiError fallback = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Malformed request",
                LocalDateTime.now(),
                Collections.emptyList()
        );

        return ResponseEntity.badRequest().body(fallback);
    }

    /**
     * Handles database integrity issues like null constraint violations or duplicate keys.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String rootMessage = ex.getMostSpecificCause().getMessage().toLowerCase();
        String userMessage;

        if (rootMessage.contains("not-null")) {
            userMessage = "One or more required fields are missing.";
        } else if (rootMessage.contains("unique") || rootMessage.contains("duplicate")) {
            userMessage = "A record with this value already exists.";
        } else {
            userMessage = "Invalid data submitted.";
        }

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Data Integrity Violation",
                userMessage,
                LocalDateTime.now(),
                Collections.emptyList()
        );

        return ResponseEntity.badRequest().body(apiError);
    }

    /**
     * Handles generic exceptions not caught by more specific handlers.
     * Used as a last-resort fallback.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Something went wrong: " + ex.getMessage(),
                LocalDateTime.now(),
                Collections.emptyList()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    /**
     * Handles validation errors triggered by @Valid annotations (e.g. Bean Validation).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldErrorDetails> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDetails(
                        error.getField(),
                        null,  // You can add rejected value if needed
                        error.getDefaultMessage()
                ))
                .toList();

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "One or more fields are invalid.",
                LocalDateTime.now(),
                validationErrors
        );

        return ResponseEntity.badRequest().body(apiError);
    }
}
