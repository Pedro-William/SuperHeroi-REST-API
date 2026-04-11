package senac.tsi.superhero.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Schema(description = "Local secreto utilizado por grupos ou heróis")
public class Esconderijo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do esconderijo", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nome do esconderijo", example = "Batcaverna", required = true)
    private String nome;

    @NotBlank
    @Schema(description = "Localização do esconderijo", example = "Gotham City", required = true)
    private String localizacao;

    @OneToOne(mappedBy = "esconderijo")
    @Schema(description = "Grupo associado ao esconderijo")
    private Grupo grupo;
}