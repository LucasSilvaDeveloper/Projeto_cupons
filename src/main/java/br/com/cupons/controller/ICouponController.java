package br.com.cupons.controller;

import br.com.cupons.controller.dto.CouponRequest;
import br.com.cupons.controller.dto.CouponResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Coupon", description = "Operacoes de criacao, consulta e remocao de cupons")
public interface ICouponController {
    @Operation(summary = "Cria um novo cupom",
            description = "Cadastra um cupom sanitizando o codigo, validando o valor de desconto minimo e a data de expiracao.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos (codigo, desconto ou data de expiracao)")
    })
    CouponResponse create(@Valid CouponRequest request);

    @Operation(summary = "Busca um cupom pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cupom encontrado"),
            @ApiResponse(responseCode = "404", description = "Cupom nao encontrado")
    })
    CouponResponse findById(@Parameter(description = "Identificador do cupom") UUID id);

    @Operation(summary = "Remove logicamente um cupom (soft delete)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cupom removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cupom nao encontrado"),
            @ApiResponse(responseCode = "409", description = "Cupom ja estava deletado")
    })
    ResponseEntity<Void> delete(@Parameter(description = "Identificador do cupom") UUID id);
}
