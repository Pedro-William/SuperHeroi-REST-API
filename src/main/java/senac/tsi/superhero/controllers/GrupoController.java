package senac.tsi.superhero.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import senac.tsi.superhero.entities.Grupo;
import senac.tsi.superhero.services.GrupoService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    private GrupoService service;

    @Operation(summary = "Listar grupos")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Grupo>>> listar(@ParameterObject Pageable pageable) {

        Page<Grupo> pagina = service.listar(pageable);

        var lista = pagina.getContent().stream()
                .map(this::toModel)
                .toList();

        PagedModel<EntityModel<Grupo>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Buscar grupo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Grupo>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar grupo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Grupo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "dados invalidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Grupo>> criar(@RequestBody @Valid Grupo grupo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toModel(service.salvar(grupo)));
    }

    @Operation(summary = "Atualizar grupo")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Grupo>> atualizar(@PathVariable Long id,
                                                        @RequestBody Grupo grupo) {
        return ResponseEntity.ok(toModel(service.atualizar(id, grupo)));
    }

    @Operation(summary = "Deletar grupo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Grupo deletado com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar grupos por nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado")
    })
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<Grupo>>> buscarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable) {

        Page<Grupo> pagina = service.buscarPorNome(nome, pageable);

        var lista = pagina.getContent().stream()
                .map(this::toModel)
                .toList();

        PagedModel<EntityModel<Grupo>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }

    private EntityModel<Grupo> toModel(Grupo grupo) {
        return EntityModel.of(grupo,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(GrupoController.class)
                                .buscar(grupo.getId())
                ).withSelfRel(),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(GrupoController.class)
                                .listar(Pageable.unpaged())
                ).withRel("lista")
        );
    }
}