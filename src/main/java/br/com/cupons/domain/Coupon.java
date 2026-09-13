package br.com.cupons.domain;

import br.com.cupons.domain.exception.CouponAlreadyDeletedException;
import br.com.cupons.domain.exception.CouponValidationException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
public class Coupon {

    private static final int CODE_LENGTH = 6;
    private static final BigDecimal MIN_DISCOUNT_VALUE = new BigDecimal("0.5");
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9]");

    private final UUID id;
    private final String code;
    private final String description;
    private final BigDecimal discountValue;
    private final Instant expirationDate;
    private CouponStatus status;
    private final boolean published;
    private final boolean redeemed;

    private Coupon(UUID id, String code, String description, BigDecimal discountValue,
                   Instant expirationDate, CouponStatus status, boolean published, boolean redeemed) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
    }

    private static String sanitizeCode(String rawCode) {
        if (rawCode == null) {
            throw new CouponValidationException("O código do cupom é obrigatório.");
        }

        String sanitized = NON_ALPHANUMERIC.matcher(rawCode).replaceAll("");

        if (sanitized.length() != CODE_LENGTH) {
            throw new CouponValidationException(
                    "O código do cupom deve conter exatamente " + CODE_LENGTH
                            + " caracteres alfanuméricos após a remoção de caracteres especiais.");
        }

        return sanitized;
    }

    public static Coupon reconstitute(UUID id, String code, String description, BigDecimal discountValue,
                                      Instant expirationDate, CouponStatus status, boolean published, boolean redeemed) {
        return new Coupon(id, code, description, discountValue, expirationDate, status, published, redeemed);
    }

    public static Coupon create(String rawCode, String description, BigDecimal discountValue,
                                Instant expirationDate, Boolean published) {

        String sanitizedCode = sanitizeCode(rawCode);
        validateDiscountValue(discountValue);
        validateExpirationDate(expirationDate);

        return new Coupon(
                UUID.randomUUID(),
                sanitizedCode,
                description,
                discountValue,
                expirationDate,
                CouponStatus.ACTIVE,
                published != null && published,
                false
        );
    }

    private static void validateDiscountValue(BigDecimal discountValue) {
        if (discountValue == null || discountValue.compareTo(MIN_DISCOUNT_VALUE) < 0) {
            throw new CouponValidationException(
                    "O valor de desconto deve ser maior ou igual a " + MIN_DISCOUNT_VALUE + ".");
        }
    }

    private static void validateExpirationDate(Instant expirationDate) {
        if (expirationDate == null || expirationDate.isBefore(Instant.now())) {
            throw new CouponValidationException("A data de expiração não pode estar no passado.");
        }
    }

    public void delete() {
        if (this.status == CouponStatus.DELETED) {
            throw new CouponAlreadyDeletedException(this.id.toString());
        }
        this.status = CouponStatus.DELETED;
    }

}
