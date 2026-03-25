package com.example.ejb.exception;

public class BeneficioNotFoundException extends RuntimeException {

    private final Long beneficioId;

    public BeneficioNotFoundException(Long beneficioId) {
        super("Benefício não encontrado com ID: " + beneficioId);
        this.beneficioId = beneficioId;
    }

    public Long getBeneficioId() {
        return beneficioId;
    }
}
