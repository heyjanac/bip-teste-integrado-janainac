package com.example.ejb;

import com.example.ejb.entity.Beneficio;
import com.example.ejb.exception.BeneficioNotFoundException;
import com.example.ejb.exception.SaldoInsuficienteException;
import com.example.ejb.exception.TransferenciaInvalidaException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Transfere valor entre dois benefícios com validação completa e locking.
     *
     * Correções aplicadas (bugs originais):
     * 1. Validação de amount > 0 e fromId != toId
     * 2. Validação de nulls (IDs inexistentes)
     * 3. Pessimistic locking (PESSIMISTIC_WRITE) para evitar lost update
     * 4. Validação de saldo suficiente
     * 5. @TransactionAttribute(REQUIRED) explícito para rollback automático
     *
     * Locking em ordem crescente de ID para evitar deadlock.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        // 1. Validação de parâmetros
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferenciaInvalidaException(
                    "Valor da transferência deve ser maior que zero");
        }

        if (fromId == null || toId == null) {
            throw new TransferenciaInvalidaException(
                    "IDs de origem e destino são obrigatórios");
        }

        if (fromId.equals(toId)) {
            throw new TransferenciaInvalidaException(
                    "Origem e destino não podem ser o mesmo benefício");
        }

        // 2. Pessimistic locking em ordem crescente de ID (evita deadlock)
        Long firstId = Math.min(fromId, toId);
        Long secondId = Math.max(fromId, toId);

        Beneficio first = em.find(Beneficio.class, firstId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio second = em.find(Beneficio.class, secondId, LockModeType.PESSIMISTIC_WRITE);

        // 3. Validação de existência
        if (first == null) {
            throw new BeneficioNotFoundException(firstId);
        }
        if (second == null) {
            throw new BeneficioNotFoundException(secondId);
        }

        // Resolver quem é from e quem é to após o locking ordenado
        Beneficio from = firstId.equals(fromId) ? first : second;
        Beneficio to = firstId.equals(toId) ? first : second;

        // 4. Validação de saldo
        if (from.getValor().compareTo(amount) < 0) {
            throw new SaldoInsuficienteException(fromId, from.getValor(), amount);
        }

        // 5. Execução da transferência
        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }
}
