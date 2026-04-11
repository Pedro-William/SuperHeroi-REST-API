package senac.tsi.superhero.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Schema(description = "Grupo de heróis")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do grupo", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nome do grupo", example = "Vingadores", required = true)
    private String nome;

    @ManyToMany
    @JoinTable(
            name = "grupo_heroi",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "heroi_id")
    )
    @Schema(description = "Lista de heróis do grupo")
    private List<SuperHeroi> herois;

    @ManyToMany
    @JoinTable(
            name = "grupo_vilao",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "vilao_id")
    )
    private List<Vilao> viloes;

    @OneToOne
    @JoinColumn(name = "esconderijo_id")
    @Schema(description = "Esconderijo do grupo")
    private Esconderijo esconderijo;
}