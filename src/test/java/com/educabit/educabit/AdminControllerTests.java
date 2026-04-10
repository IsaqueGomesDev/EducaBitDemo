package com.educabit.educabit;

import com.educabit.educabit.enums.Role;
import com.educabit.educabit.enums.UserStatus;
import com.educabit.educabit.model.Usuario;
import com.educabit.educabit.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuarioEmAnalise() {
        Usuario user = new Usuario();
        user.setUsername("candidato_criador");
        user.setEmail("candidato@test.com");
        user.setPassword("senha123");
        user.setType("USER");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.IN_REVIEW);
        user.setLinkedinUrl("https://linkedin.com/in/candidato");
        return usuarioRepository.save(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/admin/criadores/pendentes deve listar solicitaÃƒÂ§ÃƒÂµes EM ANÃƒÂLISE")
    void listarSolicitacoesPendentes() throws Exception {
        criarUsuarioEmAnalise();

        mockMvc.perform(get("/api/admin/criadores/pendentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value("candidato_criador"))
                .andExpect(jsonPath("$[0].status").value("IN_REVIEW"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/admin/criadores/pendentes NÃƒÆ’O deve listar usuÃƒÂ¡rios com status PENDING")
    void naoDeveListarUsuariosPending() throws Exception {
        Usuario userPending = new Usuario();
        userPending.setUsername("pending_user");
        userPending.setEmail("pending@test.com");
        userPending.setPassword("senha123");
        userPending.setType("USER");
        userPending.setRole(Role.USER);
        userPending.setStatus(UserStatus.PENDING);
        usuarioRepository.save(userPending);

        mockMvc.perform(get("/api/admin/criadores/pendentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/admin/criadores/aprovar deve aprovar e promover para CREATOR (fix #7)")
    void aprovarCriador() throws Exception {
        Usuario candidato = criarUsuarioEmAnalise();

        String json = """
                {
                    "userId": %d,
                    "approved": true,
                    "reason": null
                }
                """.formatted(candidato.getId());

        mockMvc.perform(post("/api/admin/criadores/aprovar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        // Verificar que o usuÃƒÂ¡rio foi promovido
        Usuario updated = usuarioRepository.findById(candidato.getId()).orElseThrow();
        assertEquals(UserStatus.ACTIVE, updated.getStatus());
        assertEquals(Role.CREATOR, updated.getRole(), "Role deveria ser CREATOR apÃƒÂ³s aprovaÃƒÂ§ÃƒÂ£o");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/admin/criadores/aprovar deve rejeitar e bloquear")
    void rejeitarCriador() throws Exception {
        Usuario candidato = criarUsuarioEmAnalise();

        String json = """
                {
                    "userId": %d,
                    "approved": false,
                    "reason": "Documentos invÃƒÂ¡lidos"
                }
                """.formatted(candidato.getId());

        mockMvc.perform(post("/api/admin/criadores/aprovar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        Usuario updated = usuarioRepository.findById(candidato.getId()).orElseThrow();
        assertEquals(UserStatus.BLOCKED, updated.getStatus());
        assertEquals(Role.USER, updated.getRole(), "Role deveria permanecer USER apÃƒÂ³s rejeiÃƒÂ§ÃƒÂ£o");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /api/admin/ deve ser negado para USER (apenas ADMIN)")
    void acessoNegadoParaUser() throws Exception {
        mockMvc.perform(get("/api/admin/criadores/pendentes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/admin/ sem autenticaÃƒÂ§ÃƒÂ£o deve redirecionar para login")
    void acessoSemAuthRedireciona() throws Exception {
        mockMvc.perform(get("/api/admin/criadores/pendentes"))
                .andExpect(status().is3xxRedirection());
    }
}


