package com.example.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.dto.BeneficioCreateDTO;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.exception.GlobalExceptionHandler;
import com.example.backend.service.BeneficioService;
import com.example.ejb.BeneficioEjbService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BeneficioController.class)
@Import(GlobalExceptionHandler.class)
class BeneficioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeneficioService beneficioService;

    @MockBean
    private BeneficioEjbService beneficioEjbService;

    @Test
    void deveListarBeneficios() throws Exception {
        when(beneficioService.listarTodos()).thenReturn(List.of(
                new BeneficioDTO(1L, "Beneficio A", "Desc", new BigDecimal("100.00"), true)
        ));

        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Beneficio A"));
    }

    @Test
    void deveBuscarPorId() throws Exception {
        when(beneficioService.buscarPorId(1L))
                .thenReturn(new BeneficioDTO(1L, "Beneficio A", "Desc", new BigDecimal("100.00"), true));

        mockMvc.perform(get("/api/v1/beneficios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveCriarBeneficio() throws Exception {
        BeneficioCreateDTO request = new BeneficioCreateDTO("Novo", "Descricao", new BigDecimal("50.00"));
        when(beneficioService.criar(any(BeneficioCreateDTO.class)))
                .thenReturn(new BeneficioDTO(10L, "Novo", "Descricao", new BigDecimal("50.00"), true));

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void deveRetornar404QuandoBeneficioNaoExiste() throws Exception {
        doThrow(new EntityNotFoundException("Benefício não encontrado com ID: 99"))
                .when(beneficioService)
                .buscarPorId(99L);

        mockMvc.perform(get("/api/v1/beneficios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"));
    }

    @Test
    void deveRetornar400QuandoPayloadInvalidoNoCreate() throws Exception {
        String payloadInvalido = "{\"nome\":\"\",\"descricao\":\"ok\",\"valor\":0}";

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Campos inválidos"));
    }

    @Test
    void deveAtualizarBeneficio() throws Exception {
        BeneficioCreateDTO request = new BeneficioCreateDTO("Atualizado", "Descricao", new BigDecimal("80.00"));
        when(beneficioService.atualizar(any(Long.class), any(BeneficioCreateDTO.class)))
                .thenReturn(new BeneficioDTO(1L, "Atualizado", "Descricao", new BigDecimal("80.00"), true));

        mockMvc.perform(put("/api/v1/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }

    @Test
    void deveDesativarBeneficio() throws Exception {
        mockMvc.perform(delete("/api/v1/beneficios/1"))
                .andExpect(status().isNoContent());
    }
}
