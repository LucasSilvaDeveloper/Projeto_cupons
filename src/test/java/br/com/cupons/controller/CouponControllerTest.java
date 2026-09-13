package br.com.cupons.controller;

import br.com.cupons.controller.dto.CouponRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CouponRequest validRequest(String code) {
        return new CouponRequest(
                code,
                "Cupom de teste de integracao",
                new BigDecimal("10.0"),
                Instant.now().plus(30, ChronoUnit.DAYS),
                false
        );
    }

    @Test
    void deveCriarCupomComSucesso() throws Exception {
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest("ABC-123"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.redeemed").value(false));
    }

    @Test
    void deveRetornar400QuandoCodeForVazio() throws Exception {
        CouponRequest request = new CouponRequest(
                "", "Cupom invalido", new BigDecimal("10.0"),
                Instant.now().plus(30, ChronoUnit.DAYS), false
        );

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void deveRetornar400QuandoDiscountValueForAbaixoDoMinimo() throws Exception {
        CouponRequest request = new CouponRequest(
                "XYZ-999", "Desconto baixo", new BigDecimal("0.1"),
                Instant.now().plus(30, ChronoUnit.DAYS), false
        );

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoExpirationDateForNoPassado() throws Exception {
        CouponRequest request = new CouponRequest(
                "OLD-001", "Data no passado", new BigDecimal("5.0"),
                Instant.now().minus(1, ChronoUnit.DAYS), false
        );

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarCupomCriadoAnteriormente() throws Exception {
        String response = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest("GET-001"))))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deveRetornar404QuandoCupomNaoExistir() throws Exception {
        mockMvc.perform(get("/coupon/{id}", "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarCupomERetornar409NaSegundaTentativa() throws Exception {
        String response = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest("DEL-001"))))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isConflict());
    }

}
