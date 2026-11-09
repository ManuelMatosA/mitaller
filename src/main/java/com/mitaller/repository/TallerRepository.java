package com.mitaller.repository;

import com.mitaller.model.Taller;
import com.mitaller.model.Especialidad.TipoEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TallerRepository extends JpaRepository<Taller, Long> {

    List<Taller> findByActivoTrue();

    List<Taller> findByLocalidadIgnoreCaseAndActivoTrue(String localidad);

    @Query("SELECT DISTINCT t FROM Taller t JOIN t.especialidades e WHERE e.nombre = :especialidad AND t.activo = true")
    List<Taller> findByEspecialidad(@Param("especialidad") TipoEspecialidad especialidad);

    @Query("SELECT DISTINCT t FROM Taller t JOIN t.especialidades e WHERE LOWER(t.localidad) = LOWER(:localidad) AND e.nombre = :especialidad AND t.activo = true")
    List<Taller> findByLocalidadAndEspecialidad(@Param("localidad") String localidad,
                                                 @Param("especialidad") TipoEspecialidad especialidad);

    @Query("SELECT DISTINCT t FROM Taller t WHERE t.activo = true AND (LOWER(t.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(t.localidad) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Taller> buscar(@Param("query") String query);

    @Query("SELECT DISTINCT t.localidad FROM Taller t WHERE t.activo = true ORDER BY t.localidad")
    List<String> findLocalidadesDistintas();

    @Query("SELECT t FROM Taller t LEFT JOIN FETCH t.especialidades LEFT JOIN FETCH t.resenas WHERE t.id = :id")
    Optional<Taller> findByIdWithDetails(@Param("id") Long id);
}
