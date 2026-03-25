package com.example.ejb.exception;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {

    private final Long beneficioId;
    private final BigDecimal saldoAtual;
    private final BigDecimal valorSolicitado;

    public SaldoInsuficienteException(Long beneficioId, BigDecimal saldoAtual, BigDecimal valorSolicitado) {
        super(String.format(
                "Saldo insuficiente no benefício ID %d: saldo atual R$ %s, valor solicitado R$ %s",
                beneficioId, saldoAtual.toPlainString(), valorSolicitado.toPlainString()
        ));
        this.beneficioId = beneficioId;
        this.saldoAtual = saldoAtual;
        this.valorSolicitado = valorSolicitado;
    }

    public Long getBeneficioId() {
        return beneficioId;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }
}
