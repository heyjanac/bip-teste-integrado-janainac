package com.example.backend.service;

import com.example.backend.dto.BeneficioCreateDTO;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;

    public BeneficioService(BeneficioRepository beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
    }

    @Transactional(readOnly = true)
    public List<BeneficioDTO> listarTodos() {
        return beneficioRepository.findByAtivoTrue().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BeneficioDTO buscarPorId(Long id) {
        Beneficio beneficio = beneficioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado com ID: " + id));
        return toDto(beneficio);
    }

    @Transactional
    public BeneficioDTO criar(BeneficioCreateDTO dto) {
        Beneficio beneficio = new Beneficio();
        beneficio.setNome(dto.nome());
        beneficio.setDescricao(dto.descricao());
        beneficio.setValor(dto.valor());
        beneficio.setAtivo(true);

        return toDto(beneficioRepository.save(beneficio));
    }

    @Transactional
    public BeneficioDTO atualizar(Long id, BeneficioCreateDTO dto) {
        Beneficio beneficio = beneficioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado com ID: " + id));

        beneficio.setNome(dto.nome());
        beneficio.setDescricao(dto.descricao());
        beneficio.setValor(dto.valor());

        return toDto(beneficioRepository.save(beneficio));
    }

    @Transactional
    public void desativar(Long id) {
        Beneficio beneficio = beneficioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado com ID: " + id));

        beneficio.setAtivo(false);
        beneficioRepository.save(beneficio);
    }

    private BeneficioDTO toDto(Beneficio beneficio) {
        return new BeneficioDTO(
                beneficio.getId(),
                beneficio.getNome(),
                beneficio.getDescricao(),
                beneficio.getValor(),
                beneficio.getAtivo()
        );
    }
}

