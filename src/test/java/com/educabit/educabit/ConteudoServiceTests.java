package com.educabit.educabit;

import com.educabit.educabit.enums.ContentStatus;
import com.educabit.educabit.enums.Role;
import com.educabit.educabit.enums.UserStatus;
import com.educabit.educabit.model.Conteudo;
import com.educabit.educabit.model.Usuario;
import com.educabit.educabit.repositories.ConteudoRepository;
import com.educabit.educabit.services.ConteudoService;
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
    private com.educabit.educabit.repositories.UsuarioRepository usuarioRepository;

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
    @DisplayName("ConteÃƒÂºdo criado por ADMIN deve ser automaticamente APPROVED")
    void conteudoCriadoPorAdminDeveSerApproved() {
        Usuario admin = criarUsuarioTeste("admin_test", Role.ADMIN);
        ContentDTO dto = new ContentDTO("TÃƒÂ­tulo Admin", "Corpo do conteÃƒÂºdo", true);

        Conteudo conteudo = conteudoService.criarConteudo(dto, admin);

        assertNotNull(conteudo.getId());
        assertEquals(ContentStatus.APPROVED, conteudo.getStatus());
        assertEquals("TÃƒÂ­tulo Admin", conteudo.getTitle());
    }

    @Test
    @DisplayName("ConteÃƒÂºdo criado por USER deve ficar PENDING")
    void conteudoCriadoPorUserDeveSerPending() {
        Usuario user = criarUsuarioTeste("user_test", Role.USER);
        ContentDTO dto = new ContentDTO("TÃƒÂ­tulo User", "Corpo do conteÃƒÂºdo", false);

        Conteudo conteudo = conteudoService.criarConteudo(dto, user);

        assertNotNull(conteudo.getId());
        assertEquals(ContentStatus.PENDING, conteudo.getStatus());
    }

    @Test
    @DisplayName("ContentDTO.isPublic() deve retornar valor correto (fix bug #1)")
    void contentDtoIsPublicDeveRetornarValorCorreto() {
        ContentDTO dtoPublico = new ContentDTO("TÃƒÂ­tulo", "Corpo", true);
        ContentDTO dtoPrivado = new ContentDTO("TÃƒÂ­tulo", "Corpo", false);

        assertTrue(dtoPublico.isPublic(), "isPublic deveria retornar true quando definido como true");
        assertFalse(dtoPrivado.isPublic(), "isPublic deveria retornar false quando definido como false");
    }

    @Test
    @DisplayName("ConteÃƒÂºdo pÃƒÂºblico deve ser visÃƒÂ­vel por todos (fix bug #1)")
    void conteudoPublicoDeveSerVisivel() {
        Usuario creator = criarUsuarioTeste("creator_vis", Role.ADMIN);
        ContentDTO dtoPublico = new ContentDTO("ConteÃƒÂºdo PÃƒÂºblico", "VisÃƒÂ­vel para todos", true);

        Conteudo conteudo = conteudoService.criarConteudo(dtoPublico, creator);

        assertTrue(conteudo.isPublic(), "ConteÃƒÂºdo deveria ser pÃƒÂºblico");
    }
}


