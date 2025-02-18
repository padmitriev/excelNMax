package dpa.excelnmax.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    @Bean
    public OpenAPI config() {
        return new OpenAPI().info(
                new Info()
                        .title("Excel N-th Max Service API")
                        .version("1.0")
                        .description("Нахождение N-ного максимального числа из файла")
                        .contact(new Contact()
                                .name("D.P.A.")
                                .email("some_mail@mail.org"))

        );
    }
}
