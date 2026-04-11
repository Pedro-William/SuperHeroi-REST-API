package senac.tsi.superhero.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import senac.tsi.superhero.enums.NivelPoder;

import java.util.List;

@Entity
@Data
@Schema(description = "Entidade que representa um vilão")
public class Vilao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do vilão", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nome do vilão", example = "Coringa", required = true)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Nível de poder do vilão", example = "ALTO")
    private NivelPoder nivelPoder;

    @ManyToMany(mappedBy = "viloes")
    @Schema(description = "Lista de heróis relacionados ao vilão")

    private List<SuperHeroi> herois;

    @ManyToMany
    @JoinTable(
            name = "vilao_poder",
            joinColumns = @JoinColumn(name = "vilao_id"),
            inverseJoinColumns = @JoinColumn(name = "poder_id")
    )
    @Schema(description = "Lista de poderes do vilão")
    private List<Poder> poderes;

    @ManyToMany(mappedBy = "viloes")
    @Schema(description = "Grupos aos quais o vilão pertence")
    private List<Grupo> grupos;

}