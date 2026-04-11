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
import senac.tsi.superhero.entities.Poder;
import senac.tsi.superhero.services.PoderService;

@RestController
@RequestMapping("/poderes")
public class PoderController {

    @Autowired
    private PoderService service;

    @Operation(summary = "Listar poderes")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Poder>>> listar(@ParameterObject Pageable pageable) {

        Page<Poder> pagina = service.listar(pageable);

        var lista = pagina.getContent().stream()
                .map(this::toModel)
                .toList();

        PagedModel<EntityModel<Poder>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Buscar poderes por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poder encontrado"),
            @ApiResponse(responseCode = "404", description = "Poder não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Poder>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar Poder")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Poder criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "dados invalidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Poder>> criar(@RequestBody @Valid Poder poder) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toModel(service.salvar(poder)));
    }

    @Operation(summary = "Atualizar Poder")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Poder>> atualizar(@PathVariable Long id,
                                                        @RequestBody Poder poder) {
        return ResponseEntity.ok(toModel(service.atualizar(id, poder)));
    }

    @Operation(summary = "Deletar poder")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Poder deletado com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar poderes por nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poder encontrado"),
            @ApiResponse(responseCode = "404", description = "Poder não encontrado")
    })
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<Poder>>> buscarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable) {

        Page<Poder> pagina = service.buscarPorNome(nome, pageable);

        var lista = pagina.getContent().stream()
                .map(this::toModel)
                .toList();

        PagedModel<EntityModel<Poder>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }

    private EntityModel<Poder> toModel(Poder poder) {
        return EntityModel.of(poder,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PoderController.class)
                                .buscar(poder.getId())
                ).withSelfRel(),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PoderController.class)
                                .listar(Pageable.unpaged())
                ).withRel("lista"),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PoderController.class)
                                .atualizar(poder.getId(), poder)
                ).withRel("update"),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PoderController.class)
                                .deletar(poder.getId())
                ).withRel("delete")
        );
    }
}