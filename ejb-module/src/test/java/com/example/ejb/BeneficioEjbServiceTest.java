package com.example.ejb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ejb.entity.Beneficio;
import com.example.ejb.exception.SaldoInsuficienteException;
import com.example.ejb.exception.TransferenciaInvalidaException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BeneficioEjbServiceTest {

    @Mock
    private EntityManager entityManager;

    private BeneficioEjbService beneficioEjbService;

    @BeforeEach
    void setup() throws Exception {
        beneficioEjbService = new BeneficioEjbService();
        Field emField = BeneficioEjbService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(beneficioEjbService, entityManager);
    }

    @Test
    void deveRejeitarTransferenciaComValorMenorOuIgualAZero() {
        assertThrows(
                TransferenciaInvalidaException.class,
                () -> beneficioEjbService.transfer(1L, 2L, BigDecimal.ZERO)
        );
    }

    @Test
    void deveRejeitarSaldoInsuficiente() {
        Beneficio from = novoBeneficio(1L, "10.00");
        Beneficio to = novoBeneficio(2L, "5.00");

        when(entityManager.find(Beneficio.class, 1L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(from);
        when(entityManager.find(Beneficio.class, 2L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(to);

        assertThrows(
                SaldoInsuficienteException.class,
                () -> beneficioEjbService.transfer(1L, 2L, new BigDecimal("20.00"))
        );
    }

    @Test
    void deveTransferirComSucessoQuandoSaldoForSuficiente() {
        Beneficio from = novoBeneficio(1L, "100.00");
        Beneficio to = novoBeneficio(2L, "50.00");

        when(entityManager.find(Beneficio.class, 1L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(from);
        when(entityManager.find(Beneficio.class, 2L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(to);

        beneficioEjbService.transfer(1L, 2L, new BigDecimal("30.00"));

        assertEquals(0, from.getValor().compareTo(new BigDecimal("70.00")));
        assertEquals(0, to.getValor().compareTo(new BigDecimal("80.00")));
        verify(entityManager, times(1)).merge(from);
        verify(entityManager, times(1)).merge(to);
    }

    private Beneficio novoBeneficio(Long id, String valor) {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(id);
        beneficio.setNome("Teste " + id);
        beneficio.setDescricao("Desc");
        beneficio.setValor(new BigDecimal(valor));
        beneficio.setAtivo(Boolean.TRUE);
        return beneficio;
    }
}

