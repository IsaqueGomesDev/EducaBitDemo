package com.educabit.educabit;

import com.educabit.educabit.Enums.ContentStatus;
import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Enums.UserStatus;
import com.educabit.educabit.Model.Conteudo;
import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.ConteudoRepository;
import com.educabit.educabit.Services.ConteudoService;
import com.educabit.educabit.dtos.ContentDTO;
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
class ConteudoServiceTests {

    @Autowired
    private ConteudoService conteudoService;

    @Autowired
    private ConteudoRepository conteudoRepository;

    @Autowired
    private com.educabit.educabit.Repositores.UsuarioRepository usuarioRepository;

    private Usuario criarUsuarioTeste(String username, Role role) {
        Usuario user = new Usuario();
        user.setUsername(username);
        user.setEmail(username + "@test.com");
        user.setPassword("senha123");
        user.setType("USER");
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        return usuarioRepository.save(user);
    }

    @Test
    @DisplayName("Conteúdo criado por ADMIN deve ser automaticamente APPROVED")
    void conteudoCriadoPorAdminDeveSerApproved() {
        Usuario admin = criarUsuarioTeste("admin_test", Role.ADMIN);
        ContentDTO dto = new ContentDTO("Título Admin", "Corpo do conteúdo", true);

        Conteudo conteudo = conteudoService.CriarConteudo(dto, admin);

        assertNotNull(conteudo.getId());
        assertEquals(ContentStatus.APPROVED, conteudo.getStatus());
        assertEquals("Título Admin", conteudo.getTitle());
    }

    @Test
    @DisplayName("Conteúdo criado por USER deve ficar PENDING")
    void conteudoCriadoPorUserDeveSerPending() {
        Usuario user = criarUsuarioTeste("user_test", Role.USER);
        ContentDTO dto = new ContentDTO("Título User", "Corpo do conteúdo", false);

        Conteudo conteudo = conteudoService.CriarConteudo(dto, user);

        assertNotNull(conteudo.getId());
        assertEquals(ContentStatus.PENDING, conteudo.getStatus());
    }

    @Test
    @DisplayName("ContentDTO.isPublic() deve retornar valor correto (fix bug #1)")
    void contentDtoIsPublicDeveRetornarValorCorreto() {
        ContentDTO dtoPublico = new ContentDTO("Título", "Corpo", true);
        ContentDTO dtoPrivado = new ContentDTO("Título", "Corpo", false);

        assertTrue(dtoPublico.isPublic(), "isPublic deveria retornar true quando definido como true");
        assertFalse(dtoPrivado.isPublic(), "isPublic deveria retornar false quando definido como false");
    }

    @Test
    @DisplayName("Conteúdo público deve ser visível por todos (fix bug #1)")
    void conteudoPublicoDeveSerVisivel() {
        Usuario creator = criarUsuarioTeste("creator_vis", Role.ADMIN);
        ContentDTO dtoPublico = new ContentDTO("Conteúdo Público", "Visível para todos", true);

        Conteudo conteudo = conteudoService.CriarConteudo(dtoPublico, creator);

        assertTrue(conteudo.isPublic(), "Conteúdo deveria ser público");
    }
}
