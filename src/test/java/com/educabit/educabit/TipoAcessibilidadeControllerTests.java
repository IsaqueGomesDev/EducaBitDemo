package com.educabit.educabit;

import com.educabit.educabit.Model.TipoAcessibilidade;
import com.educabit.educabit.Repositores.TipoAcessibilidadeRepository;
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
class TipoAcessibilidadeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TipoAcessibilidadeRepository tipoAcessibilidadeRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /educabit/tipoAcessibilidade deve criar tipo de acessibilidade")
    void criarTipoAcessibilidade() throws Exception {
        String json = """
                {
                    "nome": "Deficiência Visual",
                    "descricao": "Atividades adaptadas para pessoas com deficiência visual"
                }
                """;

        mockMvc.perform(post("/educabit/tipoAcessibilidade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Deficiência Visual"))
                .andExpect(jsonPath("$.descricao").value("Atividades adaptadas para pessoas com deficiência visual"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /educabit/tipoAcessibilidade deve listar todos")
    void listarTodosTipos() throws Exception {
        TipoAcessibilidade tipo = new TipoAcessibilidade("Visual", "Descrição teste");
        tipoAcessibilidadeRepository.save(tipo);

        mockMvc.perform(get("/educabit/tipoAcessibilidade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /educabit/tipoAcessibilidade/{id} deve buscar por ID existente")
    void buscarPorIdExistente() throws Exception {
        TipoAcessibilidade tipo = new TipoAcessibilidade("Auditiva", "Desc auditiva");
        tipo = tipoAcessibilidadeRepository.save(tipo);

        mockMvc.perform(get("/educabit/tipoAcessibilidade/" + tipo.getIdTipoAcessibilidade()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Auditiva"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /educabit/tipoAcessibilidade/{id} com ID inexistente deve retornar 404")
    void buscarPorIdInexistente() throws Exception {
        mockMvc.perform(get("/educabit/tipoAcessibilidade/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /educabit/tipoAcessibilidade/{id} deve atualizar")
    void atualizarTipoAcessibilidade() throws Exception {
        TipoAcessibilidade tipo = new TipoAcessibilidade("Original", "Desc original");
        tipo = tipoAcessibilidadeRepository.save(tipo);

        String json = """
                {
                    "nome": "Atualizado",
                    "descricao": "Desc atualizada"
                }
                """;

        mockMvc.perform(put("/educabit/tipoAcessibilidade/" + tipo.getIdTipoAcessibilidade())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"))
                .andExpect(jsonPath("$.descricao").value("Desc atualizada"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /educabit/tipoAcessibilidade/{id} com ID inexistente deve retornar 404")
    void atualizarTipoInexistente() throws Exception {
        String json = """
                {
                    "nome": "Teste",
                    "descricao": "Desc"
                }
                """;

        mockMvc.perform(put("/educabit/tipoAcessibilidade/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /educabit/tipoAcessibilidade/{id} deve remover")
    void removerTipoAcessibilidade() throws Exception {
        TipoAcessibilidade tipo = new TipoAcessibilidade("Para Deletar", "Desc deletar");
        tipo = tipoAcessibilidadeRepository.save(tipo);

        mockMvc.perform(delete("/educabit/tipoAcessibilidade/" + tipo.getIdTipoAcessibilidade()))
                .andExpect(status().isOk());

        assertFalse(tipoAcessibilidadeRepository.findById(tipo.getIdTipoAcessibilidade()).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /educabit/tipoAcessibilidade/{id} com ID inexistente deve retornar 404")
    void removerTipoInexistente() throws Exception {
        mockMvc.perform(delete("/educabit/tipoAcessibilidade/99999"))
                .andExpect(status().isNotFound());
    }
}
