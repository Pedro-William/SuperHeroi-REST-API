package senac.tsi.superhero.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Schema(description = "Representa um poder ou habilidade")
public class Poder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do poder", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nome do poder", example = "Super força", required = true)
    private String nome;

    @Size(min = 5, max = 200)
    @Schema(description = "Descrição do poder", example = "Capacidade de levantar objetos extremamente pesados")
    private String descricao;

    @ManyToMany(mappedBy = "poderes")
    @Schema(description = "Heróis que possuem esse poder")
    private List<SuperHeroi> herois;

    @ManyToMany(mappedBy = "poderes")
    @Schema(description = "Vilões que possuem esse poder")
    private List<Vilao> viloes;
}