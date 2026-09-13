package br.com.cupons.domain.exception;

public class CouponAlreadyDeletedException extends RuntimeException {

    public CouponAlreadyDeletedException(String couponId) {
        super("Cupom com id " + couponId + " já foi deletado.");
    }
}