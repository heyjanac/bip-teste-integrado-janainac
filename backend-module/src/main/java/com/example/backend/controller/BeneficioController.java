package com.example.backend.controller;

import com.example.backend.dto.BeneficioCreateDTO;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Benefícios", description = "Operações de CRUD de benefícios")
public class BeneficioController {

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    @Operation(summary = "Listar benefícios ativos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<BeneficioDTO> list() {
        return beneficioService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar benefício por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício encontrado"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    })
    public BeneficioDTO getById(@PathVariable Long id) {
        return beneficioService.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Criar benefício")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload inválido")
    })
    public ResponseEntity<BeneficioDTO> create(@Valid @RequestBody BeneficioCreateDTO request) {
        return ResponseEntity.ok(beneficioService.criar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar benefício")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload inválido"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    })
    public BeneficioDTO update(@PathVariable Long id, @Valid @RequestBody BeneficioCreateDTO request) {
        return beneficioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar benefício (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Benefício desativado"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beneficioService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
