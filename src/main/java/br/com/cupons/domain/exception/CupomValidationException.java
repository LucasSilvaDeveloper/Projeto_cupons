package br.com.cupons.domain.exception;

public class CupomValidationException extends RuntimeException {

    public CupomValidationException(String msg) {
        super(msg);
    }
}
