package com.example.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.backend.entity.Beneficio;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BeneficioRepositoryTest {

    @Autowired
    private BeneficioRepository beneficioRepository;

    @BeforeEach
    void limparDados() {
        beneficioRepository.deleteAll();
    }

    @Test
    void deveRetornarSomenteAtivosNoFindByAtivoTrue() {
        Beneficio ativo = new Beneficio();
        ativo.setNome("Ativo");
        ativo.setDescricao("A");
        ativo.setValor(new BigDecimal("10.00"));
        ativo.setAtivo(Boolean.TRUE);

        Beneficio inativo = new Beneficio();
        inativo.setNome("Inativo");
        inativo.setDescricao("I");
        inativo.setValor(new BigDecimal("20.00"));
        inativo.setAtivo(Boolean.FALSE);

        beneficioRepository.saveAll(List.of(ativo, inativo));

        List<Beneficio> ativos = beneficioRepository.findByAtivoTrue();

        assertEquals(1, ativos.size());
        assertEquals("Ativo", ativos.get(0).getNome());
    }

    @Test
    void deveRespeitarAtivoNoFindByIdAndAtivoTrue() {
        Beneficio ativo = new Beneficio();
        ativo.setNome("Ativo");
        ativo.setDescricao("A");
        ativo.setValor(new BigDecimal("30.00"));
        ativo.setAtivo(Boolean.TRUE);

        Beneficio salvo = beneficioRepository.save(ativo);

        Optional<Beneficio> resultado = beneficioRepository.findByIdAndAtivoTrue(salvo.getId());

        assertTrue(resultado.isPresent());
        assertEquals(salvo.getId(), resultado.get().getId());
    }
}
