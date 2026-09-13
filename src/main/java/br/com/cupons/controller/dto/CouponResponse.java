package br.com.cupons.controller.dto;

import br.com.cupons.domain.Coupon;
import br.com.cupons.domain.CouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CouponResponse(
        @Schema(description = "Identificador unico do cupom", example = "cef9d1e3-aae5-4ab6-a297-358c6032b1e7")
        UUID id,

        @Schema(description = "Codigo do cupom, ja sanitizado", example = "ABC123")
        String code,

        @Schema(description = "Descricao do cupom", example = "Cupom de boas-vindas")
        String description,

        @Schema(description = "Valor de desconto do cupom", example = "10.0")
        BigDecimal discountValue,

        @Schema(description = "Data de expiracao do cupom", example = "2027-12-31T23:59:59.000Z")
        Instant expirationDate,

        @Schema(description = "Status atual do cupom")
        CouponStatus status,

        @Schema(description = "Indica se o cupom esta publicado", example = "false")
        boolean published,

        @Schema(description = "Indica se o cupom ja foi resgatado", example = "false")
        boolean redeemed
) {

    public static CouponResponse fromDomain(Coupon cupom) {
        return new CouponResponse(
                cupom.getId(),
                cupom.getCode(),
                cupom.getDescription(),
                cupom.getDiscountValue(),
                cupom.getExpirationDate(),
                cupom.getStatus(),
                cupom.isPublished(),
                cupom.isRedeemed()
        );
    }
}
