package com.SuperAdminAbroad.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return createDetailedErrorResponse(HttpStatus.FORBIDDEN, ex, request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return createDetailedErrorResponse(HttpStatus.NOT_FOUND, ex, request.getRequestURI());
    }

    @ExceptionHandler({AuthenticationException.class, AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        return createDetailedErrorResponse(HttpStatus.UNAUTHORIZED, ex, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("exception", ex.getClass().getName());
        body.put("path", request.getRequestURI());

        // Root cause
        Throwable root = ex;
        while (root.getCause() != null && root != root.getCause()) {
            root = root.getCause();
        }
        body.put("rootCause", root.getMessage());

        // Optional: full stack trace as String (helpful during debugging)
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            trace.append(element.toString()).append("\n");
        }
        body.put("stackTrace", trace.toString());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // ✅ FIX: This method must exist and be accessible within the same class
    private ResponseEntity<Map<String, Object>> createDetailedErrorResponse(HttpStatus status, Exception ex, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("exception", ex.getClass().getName());
        body.put("path", path);

        // Extract root cause
        Throwable root = ex;
        while (root.getCause() != null && root != root.getCause()) {
            root = root.getCause();
        }
        body.put("rootCause", root.getMessage());

        // Optional: full stack trace string (for debugging only, remove in prod)
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            trace.append(element.toString()).append("\n");
        }
        body.put("stackTrace", trace.toString());

        return new ResponseEntity<>(body, status);
    }

}