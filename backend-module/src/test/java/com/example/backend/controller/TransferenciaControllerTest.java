package com.example.backend.controller;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.exception.GlobalExceptionHandler;
import com.example.backend.service.TransferenciaService;
import com.example.ejb.BeneficioEjbService;
import com.example.ejb.exception.SaldoInsuficienteException;
import com.example.ejb.exception.TransferenciaInvalidaException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransferenciaController.class)
@Import(GlobalExceptionHandler.class)
class TransferenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferenciaService transferenciaService;

    @MockBean
    private BeneficioEjbService beneficioEjbService;

    @Test
    void deveRetornar200QuandoTransferenciaForValida() throws Exception {
        String payload = "{\"fromId\":1,\"toId\":2,\"amount\":10.00}";

        mockMvc.perform(post("/api/v1/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar400QuandoPayloadInvalido() throws Exception {
        String payloadInvalido = "{\"fromId\":1,\"toId\":2,\"amount\":0}";

        mockMvc.perform(post("/api/v1/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Campos inválidos"));
    }

    @Test
    void deveRetornar400QuandoTransferenciaForInvalida() throws Exception {
        doThrow(new TransferenciaInvalidaException("Origem e destino não podem ser o mesmo benefício"))
                .when(transferenciaService)
                .transferir(1L, 1L, new BigDecimal("10.00"));

        String payload = "{\"fromId\":1,\"toId\":1,\"amount\":10.00}";

        mockMvc.perform(post("/api/v1/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Requisição inválida"));
    }

    @Test
    void deveRetornar409QuandoSaldoInsuficiente() throws Exception {
        doThrow(new SaldoInsuficienteException(
                1L,
                new BigDecimal("5.00"),
                new BigDecimal("10.00")
        )).when(transferenciaService).transferir(1L, 2L, new BigDecimal("10.00"));

        String payload = "{\"fromId\":1,\"toId\":2,\"amount\":10.00}";

        mockMvc.perform(post("/api/v1/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Saldo insuficiente"));
    }
}
