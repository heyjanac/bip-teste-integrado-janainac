package com.example.backend.service;

import com.example.ejb.BeneficioEjbService;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferenciaService {

    private final BeneficioEjbService beneficioEjbService;

    public TransferenciaService(BeneficioEjbService beneficioEjbService) {
        this.beneficioEjbService = beneficioEjbService;
    }

    @Transactional
    public void transferir(Long fromId, Long toId, BigDecimal amount) {
        beneficioEjbService.transfer(fromId, toId, amount);
    }
}

