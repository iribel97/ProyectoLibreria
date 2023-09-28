/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.springLibrary.controladores;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.ModelMap;

import com.egg.springLibrary.entidades.UserEn;
import com.egg.springLibrary.excepciones.MyException;
import com.egg.springLibrary.servicios.UserService;

/**
 *
 * @author irina
 */
@Controller
@RequestMapping("/")
public class PortalController {
    
    @Autowired
    private UserService uService;
    
    @GetMapping("/")
    public String index() {
        return "index.html"; //devolver la vista
    }

    @GetMapping("/register")
    public String register() {
        return "registerUser.html"; //devolver la vista
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap model) {
        try {
            uService.registrar(name, email, password, password2);
        } catch (MyException e) {
            model.put("error", e.getMessage());
            model.put("name", name);
            model.put("email", email);
            return "registerUser.html";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", "Email o contraseña incorrectos");
        }
        return "login.html"; //devolver la vista
    }
    /*

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        
        UserEn logueado = (UserEn) session.getAttribute("usuariosession");
        
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        
           return "inicio.html";
    }
    */
}
