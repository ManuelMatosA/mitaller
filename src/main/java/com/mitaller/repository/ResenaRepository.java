package com.mitaller.repository;

import com.mitaller.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByTallerIdOrderByFechaDesc(Long tallerId);
    Optional<Resena> findByUsuarioIdAndTallerId(Long usuarioId, Long tallerId);
    boolean existsByUsuarioIdAndTallerId(Long usuarioId, Long tallerId);
}
