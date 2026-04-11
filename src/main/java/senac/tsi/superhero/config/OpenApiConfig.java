package senac.tsi.superhero.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Super-Heróis")
                        .description("API REST desenvolvida para gerenciar um universo de super-heróis, vilões, grupos, poderes e esconderijos. "
                                + "Permite realizar operações completas de CRUD, consultas personalizadas com paginação e navegação entre recursos utilizando HATEOAS.")
                        .version("1.0.0"));
    }
}
