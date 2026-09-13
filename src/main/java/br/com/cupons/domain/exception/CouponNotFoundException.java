package br.com.cupons.domain.exception;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String couponId) {
        super("Coupon not found: " + couponId);
    }
}
