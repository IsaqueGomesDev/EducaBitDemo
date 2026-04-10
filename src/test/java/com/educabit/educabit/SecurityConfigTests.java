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

    // --- Endpoints públicos ---

    @Test
    @DisplayName("POST /educabit/usuario deve ser acessível sem autenticação (registro)")
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
    @DisplayName("Páginas HTML estáticas devem ser acessíveis sem autenticação")
    void paginasEstaticasPublicas() throws Exception {
        mockMvc.perform(get("/tela_inicial.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Página de seleção de login deve ser acessível")
    void paginaSelecaoLogin() throws Exception {
        mockMvc.perform(get("/selecao_login.html"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Página de cadastro deve ser acessível")
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
    @DisplayName("Endpoint /educabit/usuario/me deve ser acessível com autenticação")
    void endpointMeComAuth() throws Exception {
        // O usuário pode não existir no banco, mas o endpoint deve processar (não redirecionar)
        mockMvc.perform(get("/educabit/usuario/me"))
                .andExpect(status().isNotFound()); // usuário mock não existe no DB
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
    @DisplayName("/api/admin/** deve ser acessível para ADMIN")
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
