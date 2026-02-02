package io.github.gabrielgnoga.nexus_core_ledger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nexus Core Ledger API")
                        .version("1.0.0")
                        .description("API para gestão financeira e contábil (Ledger).")
                        .contact(new Contact()
                                .name("Gabriel Noga")
                                .url("https://github.com/gabrielgnoga")
                                .email("seu.email@exemplo.com")));
    }
}