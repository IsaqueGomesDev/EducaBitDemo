package com.educabit.educabit;

import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Enums.UserStatus;
import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.UsuarioRepository;
import com.educabit.educabit.Services.RegistroCriadorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RegistroCriadorServiceTests {

    @Autowired
    private RegistroCriadorService registroCriadorService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuarioTeste() {
        Usuario user = new Usuario();
        user.setUsername("testuser_criador");
        user.setEmail("testcriador@test.com");
        user.setPassword("senha123");
        user.setType("USER");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.PENDING);
        return usuarioRepository.save(user);
    }

    @Test
    @DisplayName("Aprovar criador deve mudar status para ACTIVE e role para CREATOR (fix bug #7)")
    void aprovarCriadorDevePromoverRole() {
        Usuario user = criarUsuarioTeste();
        Integer userId = user.getId();

        registroCriadorService.AprovarCriador(userId, true, null);

        Usuario updated = usuarioRepository.findById(userId).orElseThrow();
        assertEquals(UserStatus.ACTIVE, updated.getStatus());
        assertEquals(Role.CREATOR, updated.getRole(), "Role deveria ser CREATOR após aprovação");
    }

    @Test
    @DisplayName("Rejeitar criador deve mudar status para BLOCKED e manter role USER")
    void rejeitarCriadorDeveBloquear() {
        Usuario user = criarUsuarioTeste();
        Integer userId = user.getId();

        registroCriadorService.AprovarCriador(userId, false, "Documentos inválidos");

        Usuario updated = usuarioRepository.findById(userId).orElseThrow();
        assertEquals(UserStatus.BLOCKED, updated.getStatus());
        assertEquals(Role.USER, updated.getRole(), "Role deveria permanecer USER após rejeição");
    }
}
