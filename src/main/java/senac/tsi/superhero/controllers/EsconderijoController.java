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
import senac.tsi.superhero.entities.Esconderijo;
import senac.tsi.superhero.services.EsconderijoService;

@RestController
@RequestMapping("/esconderijos")
public class EsconderijoController {

    @Autowired
    private EsconderijoService service;

    @Operation(summary = "Listar todos os esconderijos")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Esconderijo>>> listar(@ParameterObject Pageable pageable) {

        Page<Esconderijo> pagina = service.listar(pageable);

        var lista = pagina.getContent().stream()
                .map(this::toModel)
                .toList();

        PagedModel<EntityModel<Esconderijo>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Buscar esconderijo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Esconderijo encontrado"),
            @ApiResponse(responseCode = "404", description = "Esconderijo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Esconderijo>> buscar(@PathVariable Long id) {
        Esconderijo esconderijo = service.buscarPorId(id);
        return ResponseEntity.ok(toModel(esconderijo));
    }

    @Operation(summary = "Criar esconderijo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Esconderijo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Esconderijo>> criar(@RequestBody @Valid Esconderijo esconderijo) {
        Esconderijo novo = service.salvar(esconderijo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toModel(novo));
    }

    @Operation(summary = "Atualizar esconderijo")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Esconderijo>> atualizar(@PathVariable Long id,
                                                              @RequestBody Esconderijo esconderijo) {
        Esconderijo atualizado = service.atualizar(id, esconderijo);
        return ResponseEntity.ok(toModel(atualizado));
    }

    @Operation(summary = "Deletar esconderijo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Esconderijo deletado com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar esconderijos por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Esconderijo encontrado"),
            @ApiResponse(responseCode = "404", description = "Esconderijo não encontrado")
    })
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<Esconderijo>>> buscarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable) {

        Page<Esconderijo> pagina = service.buscarPorNome(nome, pageable);

        var lista = pagina.getContent().stream()
                .map(this::toModel)
                .toList();

        PagedModel<EntityModel<Esconderijo>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }

    private EntityModel<Esconderijo> toModel(Esconderijo esconderijo) {
        return EntityModel.of(esconderijo,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(EsconderijoController.class)
                                .buscar(esconderijo.getId())
                ).withSelfRel(),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(EsconderijoController.class)
                                .listar(Pageable.unpaged())
                ).withRel("lista"),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(EsconderijoController.class)
                                .atualizar(esconderijo.getId(), esconderijo)
                ).withRel("update"),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(EsconderijoController.class)
                                .deletar(esconderijo.getId())
                ).withRel("delete")
        );
    }
}

