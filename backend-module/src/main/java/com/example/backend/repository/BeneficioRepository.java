package com.example.backend.repository;

import com.example.backend.entity.Beneficio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {

    List<Beneficio> findByAtivoTrue();

    Optional<Beneficio> findByIdAndAtivoTrue(Long id);
}

