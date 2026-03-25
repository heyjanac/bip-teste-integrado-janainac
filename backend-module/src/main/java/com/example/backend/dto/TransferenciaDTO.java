package com.example.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferenciaDTO(
        @NotNull(message = "fromId é obrigatório")
        Long fromId,

        @NotNull(message = "toId é obrigatório")
        Long toId,

        @NotNull(message = "amount é obrigatório")
        @DecimalMin(value = "0.01", message = "amount deve ser maior que zero")
        BigDecimal amount
) {
}

