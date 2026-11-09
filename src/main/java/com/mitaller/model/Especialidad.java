package com.mitaller.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "especialidades")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TipoEspecialidad nombre;

    @ManyToMany(mappedBy = "especialidades")
    private Set<Taller> talleres = new HashSet<>();

    public enum TipoEspecialidad {
        MECANICA_GENERAL("Mecánica general"),
        CHAPA_PINTURA("Chapa y pintura"),
        ITV("Pre-ITV / ITV"),
        ELECTRICIDAD("Electricidad del automóvil"),
        NEUMATICOS("Neumáticos"),
        DIAGNOSIS("Diagnosis electrónica"),
        CAMBIO_ACEITE("Cambio de aceite y filtros"),
        FRENOS("Frenos y suspensión"),
        AIRE_ACONDICIONADO("Aire acondicionado");

        private final String displayName;

        TipoEspecialidad(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() { return displayName; }
    }

    public Especialidad() {}

    public Especialidad(TipoEspecialidad nombre) {
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TipoEspecialidad getNombre() { return nombre; }
    public void setNombre(TipoEspecialidad nombre) { this.nombre = nombre; }
    public Set<Taller> getTalleres() { return talleres; }
    public void setTalleres(Set<Taller> talleres) { this.talleres = talleres; }
}
