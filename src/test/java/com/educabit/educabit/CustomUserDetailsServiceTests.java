package com.educabit.educabit;

import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Enums.UserStatus;
import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.UsuarioRepository;
import com.educabit.educabit.Services.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CustomUserDetailsServiceTests {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        Usuario admin = new Usuario();
        admin.setUsername("admin_test");
        admin.setEmail("admin_uds@test.com");
        admin.setPassword("admin123");
        admin.setType("ADMIN");
        admin.setRole(Role.ADMIN);
        admin.setStatus(UserStatus.ACTIVE);
        usuarioRepository.save(admin);

        Usuario user = new Usuario();
        user.setUsername("user_test");
        user.setEmail("user_uds@test.com");
        user.setPassword("user123");
        user.setType("USER");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        usuarioRepository.save(user);

        // Usuário sem role definida (legacy)
        Usuario legacy = new Usuario();
        legacy.setUsername("legacy_user");
        legacy.setEmail("legacy_uds@test.com");
        legacy.setPassword("legacy123");
        legacy.setType("CREATOR");
        legacy.setRole(null); // sem role definida
        legacy.setStatus(UserStatus.ACTIVE);
        usuarioRepository.save(legacy);
    }

    @Test
    @DisplayName("Deve carregar usuário ADMIN com role correta")
    void carregarAdmin() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin_test");

        assertNotNull(userDetails);
        assertEquals("admin_test", userDetails.getUsername());
        assertEquals("admin123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Deve carregar usuário USER com role correta")
    void carregarUser() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("user_test");

        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Deve usar fallback para type quando role é null (compatibilidade legada)")
    void fallbackParaTypeLegado() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("legacy_user");

        assertNotNull(userDetails);
        // Deve usar o type "CREATOR" como fallback
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR")));
    }

    @Test
    @DisplayName("Deve lançar exceção para usuário inexistente")
    void usuarioInexistente() {
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nao_existe"));
    }
}
