package br.com.cupons.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CouponRequest(
        @NotBlank(message = "O código do cupom é obrigatório.")
        String code,

        @NotBlank(message = "A descrição do cupom é obrigatória.")
        String description,

        @NotNull(message = "O valor de desconto é obrigatório.")
        BigDecimal discountValue,

        @NotNull(message = "A data de expiração é obrigatória.")
        Instant expirationDate,

        Boolean published
) {
}
