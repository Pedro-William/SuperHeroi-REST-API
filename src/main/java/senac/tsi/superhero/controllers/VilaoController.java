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
import senac.tsi.superhero.entities.Vilao;
import senac.tsi.superhero.services.VilaoService;

@RestController
@RequestMapping("/viloes")
public class VilaoController {

    @Autowired
    private VilaoService service;

    @Operation(summary = "Listar todos os vilões")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Vilao>>> listar(@ParameterObject Pageable pageable) {

        Page<Vilao> pagina = service.listar(pageable);

        var lista = pagina.getContent().stream()
                .map(this::toModel)
                .toList();

        PagedModel<EntityModel<Vilao>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Buscar vilão por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vilão encontrado"),
            @ApiResponse(responseCode = "404", description = "Vilão não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Vilao>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(toModel(service.buscarPorId(id)));
    }

    @Operation(summary = "Criar vilão")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vilão criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "dados invalidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Vilao>> criar(@RequestBody @Valid Vilao vilao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toModel(service.salvar(vilao)));
    }

    @Operation(summary = "Atualizar vilão")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Vilao>> atualizar(@PathVariable Long id,
                                                        @RequestBody Vilao vilao) {
        return ResponseEntity.ok(toModel(service.atualizar(id, vilao)));
    }

    @Operation(summary = "Deletar vilão")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vilão deletado com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar vilões por nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vilão encontrado"),
            @ApiResponse(responseCode = "404", description = "Vilão não encontrado")
    })
    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<Vilao>>> buscarPorNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable) {

        Page<Vilao> pagina = service.buscarPorNome(nome, pageable);

        var lista = pagina.getContent().stream()
                .map(this::toModel)
                .toList();

        PagedModel<EntityModel<Vilao>> pagedModel =
                PagedModel.of(lista,
                        new PagedModel.PageMetadata(
                                pagina.getSize(),
                                pagina.getNumber(),
                                pagina.getTotalElements()
                        ));

        return ResponseEntity.ok(pagedModel);
    }

    private EntityModel<Vilao> toModel(Vilao vilao) {
        return EntityModel.of(vilao,
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(VilaoController.class)
                                .buscar(vilao.getId())
                ).withSelfRel(),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(VilaoController.class)
                                .listar(Pageable.unpaged())
                ).withRel("lista"),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(VilaoController.class)
                                .atualizar(vilao.getId(), vilao)
                ).withRel("update"),

                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(VilaoController.class)
                                .deletar(vilao.getId())
                ).withRel("delete")
        );
    }
}