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
                        .description(
                                "API REST desenvolvida para gerenciamento de um universo de super-heróis.\n\n" +

                                        "A aplicação permite o cadastro, consulta, atualização e remoção de entidades que representam personagens e elementos do universo, incluindo:\n\n" +

                                        "• Super-Heróis: personagens principais, contendo nome, identidade secreta e nível de poder.\n" +
                                        "• Vilões: antagonistas com diferentes níveis de poder e habilidades.\n" +
                                        "• Poderes: habilidades que podem ser associadas tanto a heróis quanto a vilões.\n" +
                                        "• Grupos: organizações que podem reunir heróis ou vilões, representando equipes ou alianças.\n" +
                                        "• Esconderijos: locais estratégicos utilizados como base para os grupos.\n\n" +

                                        "Os relacionamentos entre as entidades permitem representar cenários complexos, como:\n" +
                                        "- Heróis e vilões possuindo múltiplos poderes\n" +
                                        "- Participação de personagens em diferentes grupos\n" +
                                        "- Associação de grupos a esconderijos específicos\n" +
                                        "- Conflitos entre heróis e vilões\n\n" +

                                        "Recursos técnicos implementados:\n" +
                                        "- Operações CRUD completas para todas as entidades\n" +
                                        "- Paginação de resultados utilizando Pageable\n" +
                                        "- Consultas personalizadas (busca por nome)\n" +
                                        "- Navegação entre recursos com HATEOAS\n" +
                                        "- Documentação interativa com Swagger/OpenAPI\n\n" +

                                        "A API foi projetada seguindo boas práticas de desenvolvimento REST, garantindo organização e facilidade de uso."
                        )
                        .version("1.0.0"));
    }
}
