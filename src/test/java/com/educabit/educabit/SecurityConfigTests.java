package com.educabit.educabit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    // --- Endpoints pÃºblicos ---

    @Test
    @DisplayName("POST /educabit/usuario deve ser acessÃ­vel sem autenticaÃ§Ã£o (registro)")
    void registroPublico() throws Exception {
        String json = """
                {
                    "username": "sectest",
                    "email": "sectest@test.com",
                    "password": "senha123",
                    "cpf": "99999999999"
                }
                """;

        mockMvc.perform(post("/educabit/usuario")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PÃ¡ginas HTML estÃ¡ticas devem ser acessÃ­veis sem autenticaÃ§Ã£o")
    void paginasEstaticasPublicas() throws Exception {
        mockMvc.perform(get("/tela_inicial.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PÃ¡gina de seleÃ§Ã£o de login deve ser acessÃ­vel")
    void paginaSelecaoLogin() throws Exception {
        mockMvc.perform(get("/selecao_login.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PÃ¡gina de cadastro deve ser acessÃ­vel")
    void paginaCadastro() throws Exception {
        mockMvc.perform(get("/cadastrar_user.html"))
                .andExpect(status().isOk());
    }

    // --- Endpoints protegidos ---

    @Test
    @DisplayName("Endpoints protegidos sem auth devem redirecionar para login")
    void endpointProtegidoSemAuth() throws Exception {
        mockMvc.perform(get("/educabit/usuario/me"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Endpoint /educabit/usuario/me deve ser acessÃ­vel com autenticaÃ§Ã£o")
    void endpointMeComAuth() throws Exception {
        // O usuÃ¡rio pode nÃ£o existir no banco, mas o endpoint deve processar (nÃ£o redirecionar)
        mockMvc.perform(get("/educabit/usuario/me"))
                .andExpect(status().isNotFound()); // usuÃ¡rio mock nÃ£o existe no DB
    }

    // --- Admin endpoints ---

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("/api/admin/** deve ser negado para USER")
    void adminEndpointNegadoParaUser() throws Exception {
        mockMvc.perform(get("/api/admin/criadores/pendentes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("/api/admin/** deve ser acessÃ­vel para ADMIN")
    void adminEndpointPermitidoParaAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/criadores/pendentes"))
                .andExpect(status().isOk());
    }

    // --- CORS ---

    @Test
    @DisplayName("CORS preflight deve ser aceito")
    void corsPreflightAceito() throws Exception {
        mockMvc.perform(options("/educabit/usuario")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk());
    }
}
