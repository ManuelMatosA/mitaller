package com.mitaller.controller;

import com.mitaller.model.Especialidad.TipoEspecialidad;
import com.mitaller.model.Taller;
import com.mitaller.model.Usuario;
import com.mitaller.repository.TallerRepository;
import com.mitaller.repository.ResenaRepository;
import com.mitaller.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final TallerRepository tallerRepository;
    private final ResenaRepository resenaRepository;
    private final UsuarioService usuarioService;

    public HomeController(TallerRepository tallerRepository, ResenaRepository resenaRepository,
                          UsuarioService usuarioService) {
        this.tallerRepository = tallerRepository;
        this.resenaRepository = resenaRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Taller> todos = tallerRepository.findByActivoTrue();
        model.addAttribute("localidades", tallerRepository.findLocalidadesDistintas());
        model.addAttribute("especialidades", TipoEspecialidad.values());
        model.addAttribute("talleresDestacados", todos.stream().limit(6).toList());
        return "home";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String localidad,
                         @RequestParam(required = false) String especialidad,
                         @RequestParam(required = false) String q,
                         Model model) {
        List<Taller> resultados;

        boolean hasLocalidad = localidad != null && !localidad.isBlank();
        boolean hasEspecialidad = especialidad != null && !especialidad.isBlank();
        boolean hasQuery = q != null && !q.isBlank();

        if (hasQuery) {
            resultados = tallerRepository.buscar(q.trim());
        } else if (hasLocalidad && hasEspecialidad) {
            resultados = tallerRepository.findByLocalidadAndEspecialidad(localidad, TipoEspecialidad.valueOf(especialidad));
        } else if (hasLocalidad) {
            resultados = tallerRepository.findByLocalidadIgnoreCaseAndActivoTrue(localidad);
        } else if (hasEspecialidad) {
            resultados = tallerRepository.findByEspecialidad(TipoEspecialidad.valueOf(especialidad));
        } else {
            resultados = tallerRepository.findByActivoTrue();
        }

        model.addAttribute("talleres", resultados);
        model.addAttribute("localidades", tallerRepository.findLocalidadesDistintas());
        model.addAttribute("especialidades", TipoEspecialidad.values());
        model.addAttribute("localidadSeleccionada", localidad);
        model.addAttribute("especialidadSeleccionada", especialidad);
        model.addAttribute("query", q);
        return "talleres/buscar";
    }

    @GetMapping("/talleres/{id}")
    public String fichaTaller(@PathVariable Long id, Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Taller> optional = tallerRepository.findById(id);

        if (optional.isEmpty()) {
            throw new RuntimeException("Taller no encontrado");
        }
        
        Taller taller = optional.get();

        model.addAttribute("taller", taller);
        model.addAttribute("resenas", resenaRepository.findByTallerIdOrderByFechaDesc(id));

        if (userDetails != null) {
            Usuario usuario = usuarioService.findByUsername(userDetails.getUsername());
            boolean yaReseno = resenaRepository.existsByUsuarioIdAndTallerId(usuario.getId(), id);
            model.addAttribute("yaReseno", yaReseno);
            model.addAttribute("usuarioLogueado", true);
        } else {
            model.addAttribute("yaReseno", false);
            model.addAttribute("usuarioLogueado", false);
        }

        return "talleres/ficha";
    }
}
