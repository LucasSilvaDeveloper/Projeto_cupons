package br.com.cupons.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.regex.Pattern;

public class Cupom {

    private static final int CODE_LENGTH = 6;
    private static final BigDecimal MIN_DISCOUNT_VALUE = new BigDecimal("0.5");
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9]");

    private final UUID id;
    private final String code;
    private final String description;
    private final BigDecimal discountValue;
    private final Instant expirationDate;
    private CupomStatus status;
    private final boolean published;
    private final boolean redeemed;

    private Cupom(UUID id, String code, String description, BigDecimal discountValue,
                   Instant expirationDate, CupomStatus status, boolean published, boolean redeemed) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
    }
}
