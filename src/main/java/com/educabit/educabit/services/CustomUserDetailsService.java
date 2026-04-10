package com.educabit.educabit.services;

import com.educabit.educabit.model.Usuario;
import com.educabit.educabit.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("UsuÃ¡rio nÃ£o encontrado: " + username);
        }

        com.educabit.educabit.enums.Role roleEnum = usuario.getRole();
        String roleName;
        if (roleEnum != null) {
            roleName = roleEnum.name();
        } else {
            // Fallback to old String type
            roleName = usuario.getType() != null ? usuario.getType().toUpperCase() : "USER";
        }

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(roleName)
                .build();
    }
}


