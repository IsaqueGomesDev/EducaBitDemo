package com.educabit.educabit.config;

import com.educabit.educabit.enums.Role;
import com.educabit.educabit.model.Usuario;
import com.educabit.educabit.repositories.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Usuario user = usuarioRepository.findByUsername(authentication.getName());
        if (user != null && user.getRole() == Role.ADMIN) {
            response.sendRedirect("/educabit/admin/dashboard");
        } else {
            response.sendRedirect("/educabit/home");
        }
    }
}
