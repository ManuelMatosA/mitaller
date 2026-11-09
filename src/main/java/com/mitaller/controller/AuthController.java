package com.mitaller.controller;

import com.mitaller.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) model.addAttribute("error", "Usuario o contraseña incorrectos");
        if (logout != null) model.addAttribute("mensaje", "Has cerrado sesión correctamente");
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String username, @RequestParam String email,
                                   @RequestParam String password, @RequestParam String nombre,
                                   @RequestParam(required = false) String apellidos,
                                   RedirectAttributes redirectAttributes) {
        try {
            usuarioService.registrar(username, email, password, nombre, apellidos);
            redirectAttributes.addFlashAttribute("mensaje", "¡Cuenta creada! Ya puedes iniciar sesión");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }
}
