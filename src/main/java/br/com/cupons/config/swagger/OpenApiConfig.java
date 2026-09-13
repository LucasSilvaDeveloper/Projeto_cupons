package br.com.cupons.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI couponApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coupon API")
                        .description("API para gerenciamento de cupons de desconto, incluindo criacao, consulta e remocao logica (soft delete).")
                        .version("v1.0"));
    }
}
