package com.mitaller.config;

import com.mitaller.model.*;
import com.mitaller.model.Especialidad.TipoEspecialidad;
import com.mitaller.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TallerRepository tallerRepository;
    private final EspecialidadRepository especialidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(TallerRepository tallerRepository, EspecialidadRepository especialidadRepository,
                           UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.tallerRepository = tallerRepository;
        this.especialidadRepository = especialidadRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (especialidadRepository.count() == 0) seedEspecialidades();
        if (usuarioRepository.count() == 0) seedUsuarios();
        if (tallerRepository.count() == 0) seedTalleres();
    }

    private void seedEspecialidades() {
        for (TipoEspecialidad tipo : TipoEspecialidad.values()) {
            Especialidad especialidad = new Especialidad(tipo);
            especialidadRepository.save(especialidad);
        }
        System.out.println("✓ " + TipoEspecialidad.values().length + " especialidades cargadas");
    }

    private void seedUsuarios() {
        Usuario admin = new Usuario("admin", "admin@mitaller.com",
                passwordEncoder.encode("admin123"), "Administrador", "Sistema");
        admin.setRol(Usuario.Rol.ADMIN);
        usuarioRepository.save(admin);

        Usuario cliente = new Usuario("manuel", "manuel@email.com",
                passwordEncoder.encode("123456"), "Manuel", "Matosa");
        usuarioRepository.save(cliente);

        Usuario cliente2 = new Usuario("laura", "laura@email.com",
                passwordEncoder.encode("123456"), "Laura", "García");
        usuarioRepository.save(cliente2);

        System.out.println("✓ Usuarios creados (admin/admin123, manuel/123456, laura/123456)");
    }

    private void seedTalleres() {
        Especialidad mecGen = especialidadRepository.findByNombre(TipoEspecialidad.MECANICA_GENERAL).get();
        Especialidad chapa = especialidadRepository.findByNombre(TipoEspecialidad.CHAPA_PINTURA).get();
        Especialidad itv = especialidadRepository.findByNombre(TipoEspecialidad.ITV).get();
        Especialidad elec = especialidadRepository.findByNombre(TipoEspecialidad.ELECTRICIDAD).get();
        Especialidad neum = especialidadRepository.findByNombre(TipoEspecialidad.NEUMATICOS).get();
        Especialidad diag = especialidadRepository.findByNombre(TipoEspecialidad.DIAGNOSIS).get();
        Especialidad aceite = especialidadRepository.findByNombre(TipoEspecialidad.CAMBIO_ACEITE).get();
        Especialidad frenos = especialidadRepository.findByNombre(TipoEspecialidad.FRENOS).get();
        Especialidad aire = especialidadRepository.findByNombre(TipoEspecialidad.AIRE_ACONDICIONADO).get();

        crearTaller("AutoPro Móstoles", "Taller multimarca con más de 15 años de experiencia. Especialistas en vehículos europeos y asiáticos.",
                "C/ Libertad 23", "Móstoles", "28933", "916 456 789", "info@autopromostoles.com",
                "L-V 8:30-18:30, S 9:00-14:00", Set.of(mecGen, aceite, frenos, diag));

        crearTaller("Talleres Hernández", "Taller familiar de confianza. Reparaciones generales y preparación para ITV.",
                "Avda. de Portugal 45", "Móstoles", "28935", "916 789 012", "hernandez@taller.com",
                "L-V 9:00-19:00", Set.of(mecGen, itv, aceite));

        crearTaller("ChapaMaster Sur", "Especialistas en chapa, pintura y reparación de carrocería. Trabajamos con todas las aseguradoras.",
                "Pol. Ind. El Soto, Nave 12", "Getafe", "28901", "916 234 567", "info@chapamaster.com",
                "L-V 8:00-17:00", Set.of(chapa));

        crearTaller("ElectroAuto Madrid", "Centro especializado en electricidad y electrónica del automóvil. Diagnosis computerizada.",
                "C/ Alcalá 234", "Madrid", "28027", "912 345 678", "contacto@electroauto.com",
                "L-V 9:00-18:00, S 10:00-13:00", Set.of(elec, diag));

        crearTaller("Neumáticos Express", "Venta y montaje de neumáticos de todas las marcas. Equilibrado y alineación.",
                "C/ Gran Vía de Hortaleza 89", "Madrid", "28043", "913 456 789", "info@neumaticosexpress.com",
                "L-S 8:00-20:00", Set.of(neum, frenos));

        crearTaller("Taller García e Hijos", "Taller de barrio de toda la vida. Mecánica general y mantenimientos.",
                "C/ Princesa 112", "Madrid", "28008", "914 567 890", "garcia.hijos@gmail.com",
                "L-V 8:30-17:30", Set.of(mecGen, aceite, frenos));

        crearTaller("AutoClima Sur", "Especialistas en climatización del automóvil. Carga de aire acondicionado y calefacción.",
                "C/ del Comercio 8", "Leganés", "28914", "916 345 678", "info@autoclimasur.com",
                "L-V 9:00-18:00", Set.of(aire, elec));

        crearTaller("ITV Plus Getafe", "Centro de preparación para ITV. Pre-inspección gratuita y reparaciones necesarias.",
                "Avda. Juan de la Cierva 15", "Getafe", "28902", "916 543 210", "info@itvplusgetafe.com",
                "L-V 8:00-19:00, S 9:00-14:00", Set.of(itv, mecGen, aceite));

        crearTaller("Racing Mechanics", "Taller premium para vehículos deportivos y de alta gama. BMW, Mercedes, Audi.",
                "C/ Serrano 88", "Madrid", "28006", "915 678 901", "info@racingmechanics.com",
                "L-V 9:00-19:00", Set.of(mecGen, diag, elec, frenos));

        crearTaller("Talleres Rápidos Alcorcón", "Servicio rápido sin cita: aceite, frenos, neumáticos. En 1 hora.",
                "C/ Mayor 56", "Alcorcón", "28921", "916 678 901", "rapidos@alcorcon.com",
                "L-S 8:00-21:00", Set.of(aceite, neum, frenos));

        crearTaller("CarroFix Fuenlabrada", "Taller integral con servicio de grúa 24h. Todas las marcas.",
                "Pol. Ind. Cobo Calleja, Nave 45", "Fuenlabrada", "28947", "916 789 123", "info@carrofix.com",
                "L-V 8:00-18:00", Set.of(mecGen, chapa, elec, diag, aceite));

        crearTaller("Motor Sur Parla", "Mecánica general y electromecánica. Especialistas en vehículos híbridos.",
                "Avda. de Madrid 67", "Parla", "28980", "916 890 234", "contacto@motorsurparla.com",
                "L-V 9:00-18:30", Set.of(mecGen, elec, diag));

        System.out.println("✓ 12 talleres cargados en la base de datos");
    }

    private void crearTaller(String nombre, String descripcion, String direccion, String localidad,
                             String codigoPostal, String telefono, String email, String horario,
                             Set<Especialidad> especialidades) {
        Taller taller = new Taller(nombre, descripcion, direccion, localidad, codigoPostal, telefono, email, horario);
        taller.setEspecialidades(especialidades);
        tallerRepository.save(taller);
    }
}
