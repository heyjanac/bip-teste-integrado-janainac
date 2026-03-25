package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.example.ejb.BeneficioEjbService;
import com.example.ejb.exception.TransferenciaInvalidaException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private BeneficioEjbService beneficioEjbService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Test
    void deveDelegarTransferenciaParaServicoEjb() {
        transferenciaService.transferir(1L, 2L, new BigDecimal("10.00"));

        verify(beneficioEjbService).transfer(1L, 2L, new BigDecimal("10.00"));
    }

    @Test
    void devePropagarExcecaoDeTransferenciaInvalida() {
        doThrow(new TransferenciaInvalidaException("Origem e destino não podem ser iguais"))
                .when(beneficioEjbService)
                .transfer(1L, 1L, new BigDecimal("10.00"));

        assertThrows(
                TransferenciaInvalidaException.class,
                () -> transferenciaService.transferir(1L, 1L, new BigDecimal("10.00"))
        );
    }
}

