package senac.tsi.superhero.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import senac.tsi.superhero.entities.SuperHeroi;
import senac.tsi.superhero.services.SuperHeroiService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/herois")
public class SuperHeroiController {

    @Autowired
    private SuperHeroiService service;


    @Operation(summary = "Listar todos os heróis")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<SuperHeroi>>> listar(Pageable pageable) {

        Page<SuperHeroi> pagina = service.listar(pageable);

        var heroisModel = pagina.getContent().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        PagedModel<EntityModel<SuperHeroi>> pagedModel =
                PagedModel.of(heroisModel,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }


    @Operation(summary = "Buscar herói por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herói encontrado"),
            @ApiResponse(responseCode = "404", description = "Herói não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SuperHeroi>> buscar(@PathVariable Long id) {
        SuperHeroi heroi = service.buscarPorId(id);
        return ResponseEntity.ok(toModel(heroi));
    }


    @Operation(summary = "Criar herói")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Herói criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<SuperHeroi>> criar(@RequestBody @Valid SuperHeroi heroi) {
        SuperHeroi novoHeroi = service.salvar(heroi);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toModel(novoHeroi));
    }

    @Operation(summary = "Atualizar herói")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SuperHeroi>> atualizar(@PathVariable Long id,
                                                             @RequestBody SuperHeroi heroi) {
        SuperHeroi atualizado = service.atualizar(id, heroi);
        return ResponseEntity.ok(toModel(atualizado));
    }


    @Operation(summary = "Deletar herói")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Herói deletado com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Buscar heróis por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herói encontrado"),
            @ApiResponse(responseCode = "404", description = "Herói não encontrado")
    })
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<SuperHeroi>>> buscarPorNome(
            @RequestParam String nome,  @ParameterObject Pageable pageable) {

        Page<SuperHeroi> pagina = service.buscarPorNome(nome, pageable);

        var heroisModel = pagina.getContent().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        PagedModel<EntityModel<SuperHeroi>> pagedModel =
                PagedModel.of(heroisModel,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }


    private EntityModel<SuperHeroi> toModel(SuperHeroi heroi) {
        return EntityModel.of(heroi,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(SuperHeroiController.class)
                                .buscar(heroi.getId())
                ).withSelfRel(),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(SuperHeroiController.class)
                                .listar(Pageable.unpaged())
                ).withRel("lista"),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(SuperHeroiController.class)
                                .atualizar(heroi.getId(), heroi)
                ).withRel("update"),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(SuperHeroiController.class)
                                .deletar(heroi.getId())
                ).withRel("delete")
        );
    }
}
