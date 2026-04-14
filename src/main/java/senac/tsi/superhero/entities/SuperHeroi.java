package senac.tsi.superhero.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import senac.tsi.superhero.enums.NivelPoder;

import java.util.List;

@Entity
@Data
@Schema(description = "Entidade que representa um super-herói")
public class SuperHeroi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do herói", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nome do herói", example = "Homem-Aranha", required = true)
    private String nome;

    @NotBlank
    @Schema(description = "Nome real do herói", example = "Peter Parker", required = true)
    private String nomeReal;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Nível de poder do herói", example = "MEDIO")
    private NivelPoder nivelPoder;

    @ManyToMany
    @JoinTable(
            name = "heroi_poder",
            joinColumns = @JoinColumn(name = "heroi_id"),
            inverseJoinColumns = @JoinColumn(name = "poder_id")
    )
    @Schema(description = "Lista de poderes do herói")
    private List<Poder> poderes;

    @ManyToMany(mappedBy = "herois")
    @Schema(description = "Grupos aos quais o herói pertence")
    @JsonIgnore
    private List<Grupo> grupos;

    @ManyToMany
    @JoinTable(
            name = "heroi_vilao",
            joinColumns = @JoinColumn(name = "heroi_id"),
            inverseJoinColumns = @JoinColumn(name = "vilao_id")
    )
    @Schema(description = "Vilões enfrentados pelo herói")
    @JsonIgnore
    private List<Vilao> viloes;
}