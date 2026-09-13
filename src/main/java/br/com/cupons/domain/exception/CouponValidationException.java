package br.com.cupons.domain.exception;

public class CouponValidationException extends RuntimeException {

    public CouponValidationException(String msg) {
        super(msg);
    }
}
