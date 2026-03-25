package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.backend.dto.BeneficioCreateDTO;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @InjectMocks
    private BeneficioService beneficioService;

    @Test
    void deveListarSomenteBeneficiosAtivos() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome("Beneficio A");
        beneficio.setDescricao("Descricao A");
        beneficio.setValor(new BigDecimal("100.00"));
        beneficio.setAtivo(Boolean.TRUE);

        when(beneficioRepository.findByAtivoTrue()).thenReturn(List.of(beneficio));

        List<BeneficioDTO> resultado = beneficioService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Beneficio A", resultado.get(0).nome());
    }

    @Test
    void deveLancarEntityNotFoundQuandoIdNaoExiste() {
        when(beneficioRepository.findByIdAndAtivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> beneficioService.buscarPorId(99L));
    }

    @Test
    void deveCriarBeneficioAtivoPorPadrao() {
        BeneficioCreateDTO request = new BeneficioCreateDTO("Novo", "Desc", new BigDecimal("50.00"));
        Beneficio salvo = new Beneficio();
        salvo.setId(7L);
        salvo.setNome(request.nome());
        salvo.setDescricao(request.descricao());
        salvo.setValor(request.valor());
        salvo.setAtivo(Boolean.TRUE);

        when(beneficioRepository.save(any(Beneficio.class))).thenReturn(salvo);

        BeneficioDTO resposta = beneficioService.criar(request);

        assertEquals(7L, resposta.id());
        assertEquals(Boolean.TRUE, resposta.ativo());
    }

    @Test
    void deveDesativarBeneficioComSoftDelete() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(3L);
        beneficio.setAtivo(Boolean.TRUE);

        when(beneficioRepository.findByIdAndAtivoTrue(3L)).thenReturn(Optional.of(beneficio));

        beneficioService.desativar(3L);

        assertFalse(beneficio.getAtivo());
        verify(beneficioRepository).save(beneficio);
    }
}

