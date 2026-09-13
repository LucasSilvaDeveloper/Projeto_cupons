package br.com.cupons.exceptionhandler;

import br.com.cupons.domain.exception.CouponAlreadyDeletedException;
import br.com.cupons.domain.exception.CouponNotFoundException;
import br.com.cupons.domain.exception.CouponValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CouponValidationException.class)
    public ResponseEntity<ApiError> handleCupomValidation(CouponValidationException ex) {
        ApiError error = ApiError.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ApiError> handleCupomNotFound(CouponNotFoundException ex) {
        ApiError error = ApiError.of(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CouponAlreadyDeletedException.class)
    public ResponseEntity<ApiError> handleCupomAlreadyDeleted(CouponAlreadyDeletedException ex) {
        ApiError error = ApiError.of(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Erro de validação nos campos informados.",
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
