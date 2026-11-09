package com.mitaller.repository;

import com.mitaller.model.Especialidad;
import com.mitaller.model.Especialidad.TipoEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    Optional<Especialidad> findByNombre(TipoEspecialidad nombre);
}
