package com.mitaller.controller;

import com.mitaller.model.Especialidad;
import com.mitaller.model.Especialidad.TipoEspecialidad;
import com.mitaller.model.Taller;
import com.mitaller.repository.EspecialidadRepository;
import com.mitaller.repository.TallerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TallerRepository tallerRepository;
    private final EspecialidadRepository especialidadRepository;

    public AdminController(TallerRepository tallerRepository, EspecialidadRepository especialidadRepository) {
        this.tallerRepository = tallerRepository;
        this.especialidadRepository = especialidadRepository;
    }

    // ── Listar todos los talleres ──
    @GetMapping("/talleres")
    public String listarTalleres(Model model) {
        model.addAttribute("talleres", tallerRepository.findAll());
        return "admin/talleres";
    }

    // ── Formulario nuevo taller ──
    @GetMapping("/talleres/nuevo")
    public String nuevoTallerForm(Model model) {
        model.addAttribute("taller", new Taller());
        model.addAttribute("especialidades", TipoEspecialidad.values());
        return "admin/taller-form";
    }

    // ── Guardar nuevo taller ──
    @PostMapping("/talleres/guardar")
    public String guardarTaller(@RequestParam String nombre,
                                @RequestParam String descripcion,
                                @RequestParam String direccion,
                                @RequestParam String localidad,
                                @RequestParam(required = false) String codigoPostal,
                                @RequestParam(required = false) String telefono,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String horario,
                                @RequestParam(required = false) List<String> especialidades,
                                RedirectAttributes redirectAttributes) {
        Taller taller = new Taller(nombre, descripcion, direccion, localidad,
                codigoPostal, telefono, email, horario);

        if (especialidades != null) {
            Set<Especialidad> esps = new HashSet<>();
            for (String esp : especialidades) {
                especialidadRepository.findByNombre(TipoEspecialidad.valueOf(esp))
                        .ifPresent(esps::add);
            }
            taller.setEspecialidades(esps);
        }

        tallerRepository.save(taller);
        redirectAttributes.addFlashAttribute("mensaje", "Taller '" + nombre + "' creado correctamente");
        return "redirect:/admin/talleres";
    }

    // ── Formulario editar taller ──
    @GetMapping("/talleres/{id}/editar")
    public String editarTallerForm(@PathVariable Long id, Model model) {
        Taller taller = tallerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado"));
        model.addAttribute("taller", taller);
        model.addAttribute("especialidades", TipoEspecialidad.values());
        return "admin/taller-form";
    }

    // ── Actualizar taller existente ──
    @PostMapping("/talleres/{id}/actualizar")
    public String actualizarTaller(@PathVariable Long id,
                                   @RequestParam String nombre,
                                   @RequestParam String descripcion,
                                   @RequestParam String direccion,
                                   @RequestParam String localidad,
                                   @RequestParam(required = false) String codigoPostal,
                                   @RequestParam(required = false) String telefono,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String horario,
                                   @RequestParam(required = false) List<String> especialidades,
                                   RedirectAttributes redirectAttributes) {
        Taller taller = tallerRepository.findById(id).orElseThrow();
        taller.setNombre(nombre);
        taller.setDescripcion(descripcion);
        taller.setDireccion(direccion);
        taller.setLocalidad(localidad);
        taller.setCodigoPostal(codigoPostal);
        taller.setTelefono(telefono);
        taller.setEmail(email);
        taller.setHorario(horario);

        Set<Especialidad> esps = new HashSet<>();
        if (especialidades != null) {
            for (String esp : especialidades) {
                especialidadRepository.findByNombre(TipoEspecialidad.valueOf(esp))
                        .ifPresent(esps::add);
            }
        }
        taller.setEspecialidades(esps);

        tallerRepository.save(taller);
        redirectAttributes.addFlashAttribute("mensaje", "Taller actualizado correctamente");
        return "redirect:/admin/talleres";
    }

    // ── Activar/desactivar taller ──
    @PostMapping("/talleres/{id}/toggle")
    public String toggleTaller(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Taller taller = tallerRepository.findById(id).orElseThrow();
        taller.setActivo(!taller.isActivo());
        tallerRepository.save(taller);
        redirectAttributes.addFlashAttribute("mensaje",
                "Taller " + (taller.isActivo() ? "activado" : "desactivado"));
        return "redirect:/admin/talleres";
    }
}
