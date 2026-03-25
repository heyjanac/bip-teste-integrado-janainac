package com.example.backend.controller;

import com.example.backend.dto.TransferenciaDTO;
import com.example.backend.service.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transferencias")
@Tag(name = "Transferências", description = "Operações de transferência entre benefícios")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    @Operation(summary = "Transferir valor entre dois benefícios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload inválido ou transferência inválida"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado"),
            @ApiResponse(responseCode = "409", description = "Saldo insuficiente")
    })
    public ResponseEntity<Void> transferir(@Valid @RequestBody TransferenciaDTO request) {
        transferenciaService.transferir(request.fromId(), request.toId(), request.amount());
        return ResponseEntity.ok().build();
    }
}

