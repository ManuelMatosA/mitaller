package com.mitaller.repository;

import com.mitaller.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByUsuarioIdOrderByFechaDescHoraDesc(Long usuarioId);
    List<Cita> findByTallerIdOrderByFechaDescHoraDesc(Long tallerId);
    List<Cita> findAllByOrderByFechaCreacionDesc();
}
