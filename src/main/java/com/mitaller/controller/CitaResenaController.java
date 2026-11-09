package com.mitaller.controller;

import com.mitaller.model.*;
import com.mitaller.repository.*;
import com.mitaller.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class CitaResenaController {

    private final TallerRepository tallerRepository;
    private final CitaRepository citaRepository;
    private final ResenaRepository resenaRepository;
    private final UsuarioService usuarioService;

    public CitaResenaController(TallerRepository tallerRepository, CitaRepository citaRepository,
                                 ResenaRepository resenaRepository, UsuarioService usuarioService) {
        this.tallerRepository = tallerRepository;
        this.citaRepository = citaRepository;
        this.resenaRepository = resenaRepository;
        this.usuarioService = usuarioService;
    }

    // ── RESEÑAS ──

    @PostMapping("/talleres/{id}/resena")
    public String crearResena(@PathVariable Long id,
                              @RequestParam int puntuacion,
                              @RequestParam String comentario,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.findByUsername(userDetails.getUsername());
        Taller taller = tallerRepository.findById(id).orElseThrow();

        if (resenaRepository.existsByUsuarioIdAndTallerId(usuario.getId(), id)) {
            redirectAttributes.addFlashAttribute("error", "Ya has dejado una reseña en este taller");
            return "redirect:/talleres/" + id;
        }

        Resena resena = new Resena(usuario, taller, puntuacion, comentario);
        resenaRepository.save(resena);
        redirectAttributes.addFlashAttribute("mensaje", "¡Reseña publicada!");
        return "redirect:/talleres/" + id;
    }

    // ── CITAS ──

    @GetMapping("/talleres/{id}/cita")
    public String formularioCita(@PathVariable Long id, Model model) {
        Taller taller = tallerRepository.findById(id).orElseThrow();
        model.addAttribute("taller", taller);
        model.addAttribute("fechaMinima", LocalDate.now().plusDays(1).toString());
        return "citas/nueva";
    }

    @PostMapping("/talleres/{id}/cita")
    public String crearCita(@PathVariable Long id,
                            @RequestParam String fecha,
                            @RequestParam String hora,
                            @RequestParam String tipoServicio,
                            @RequestParam(required = false) String descripcionProblema,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.findByUsername(userDetails.getUsername());
        Taller taller = tallerRepository.findById(id).orElseThrow();

        Cita cita = new Cita(usuario, taller, LocalDate.parse(fecha), LocalTime.parse(hora),
                tipoServicio, descripcionProblema);
        citaRepository.save(cita);

        redirectAttributes.addFlashAttribute("mensaje", "¡Cita solicitada! El taller la confirmará pronto.");
        return "redirect:/mis-citas";
    }

    @GetMapping("/mis-citas")
    public String misCitas(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Usuario usuario = usuarioService.findByUsername(userDetails.getUsername());
        model.addAttribute("citas", citaRepository.findByUsuarioIdOrderByFechaDescHoraDesc(usuario.getId()));
        return "citas/mis-citas";
    }

    // ── ADMIN: Gestión de citas ──

    @GetMapping("/admin/citas")
    public String adminCitas(Model model) {
        model.addAttribute("citas", citaRepository.findAllByOrderByFechaCreacionDesc());
        return "admin/citas";
    }

    @PostMapping("/admin/citas/{id}/estado")
    public String cambiarEstadoCita(@PathVariable Long id,
                                    @RequestParam String estado,
                                    RedirectAttributes redirectAttributes) {
        Cita cita = citaRepository.findById(id).orElseThrow();
        cita.setEstado(Cita.EstadoCita.valueOf(estado));
        citaRepository.save(cita);
        redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado a " + cita.getEstado().getDisplayName());
        return "redirect:/admin/citas";
    }
}
