package com.educabit.educabit;

import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Enums.UserStatus;
import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("POST /educabit/usuario deve criar usuário com type default 'USER' (fix #3)")
    void criarUsuarioComTypeDefault() throws Exception {
        // Enviar sem campo 'type'
        String json = """
                {
                    "username": "novousuario",
                    "email": "novo@test.com",
                    "password": "senha123",
                    "cpf": "11111111111"
                }
                """;

        mockMvc.perform(post("/educabit/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("USER"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("POST /educabit/usuario deve forçar Role.USER mesmo se type=CREATOR")
    void criarUsuarioCreatorDeveFocarRoleUser() throws Exception {
        String json = """
                {
                    "username": "wannabecreator",
                    "email": "creator@test.com",
                    "password": "senha123",
                    "cpf": "22222222222",
                    "type": "CREATOR"
                }
                """;

        mockMvc.perform(post("/educabit/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /educabit/usuario/{id} com ID existente deve retornar usuario")
    void buscarUsuarioPorIdExistente() throws Exception {
        Usuario user = new Usuario();
        user.setUsername("testget");
        user.setEmail("testget@test.com");
        user.setPassword("senha123");
        user.setType("USER");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        user = usuarioRepository.save(user);

        mockMvc.perform(get("/educabit/usuario/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testget"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /educabit/usuario/{id} com ID inexistente deve retornar 404")
    void buscarUsuarioPorIdInexistente() throws Exception {
        mockMvc.perform(get("/educabit/usuario/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /educabit/usuario/{id} deve remover o usuario")
    void removerUsuario() throws Exception {
        Usuario user = new Usuario();
        user.setUsername("testdelete");
        user.setEmail("testdelete@test.com");
        user.setPassword("senha123");
        user.setType("USER");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        user = usuarioRepository.save(user);

        mockMvc.perform(delete("/educabit/usuario/" + user.getId()))
                .andExpect(status().isOk());

        // Verificar que foi removido
        assertTrue(usuarioRepository.findById(user.getId()).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /educabit/usuario/{id} deve atualizar campos fornecidos")
    void atualizarUsuario() throws Exception {
        Usuario user = new Usuario();
        user.setUsername("testupdate");
        user.setEmail("testupdate@test.com");
        user.setPassword("senha123");
        user.setType("USER");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        user = usuarioRepository.save(user);

        String json = """
                {
                    "username": "updatedusuario",
                    "email": "updated@test.com"
                }
                """;

        mockMvc.perform(put("/educabit/usuario/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedusuario"))
                .andExpect(jsonPath("$.email").value("updated@test.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /educabit/usuario deve listar todos os usuarios")
    void listarTodosUsuarios() throws Exception {
        Usuario user = new Usuario();
        user.setUsername("testlist");
        user.setEmail("testlist@test.com");
        user.setPassword("senha123");
        user.setType("USER");
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        usuarioRepository.save(user);

        mockMvc.perform(get("/educabit/usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
