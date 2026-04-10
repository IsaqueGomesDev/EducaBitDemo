package com.educabit.educabit;

import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Enums.UserStatus;
import com.educabit.educabit.Model.Conteudo;
import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.ConteudoRepository;
import com.educabit.educabit.Repositores.UsuarioRepository;
import com.educabit.educabit.Enums.ContentStatus;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ConteudoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConteudoRepository conteudoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario autorTeste;

    @BeforeEach
    void setUp() {
        autorTeste = new Usuario();
        autorTeste.setUsername("autor_conteudo");
        autorTeste.setEmail("autor_conteudo@test.com");
        autorTeste.setPassword("senha123");
        autorTeste.setType("CREATOR");
        autorTeste.setRole(Role.CREATOR);
        autorTeste.setStatus(UserStatus.ACTIVE);
        autorTeste = usuarioRepository.save(autorTeste);
    }

    @Test
    @DisplayName("GET /educabit/conteudo/publico deve retornar conteúdos públicos sem autenticação")
    void listarConteudoPublicoSemAuth() throws Exception {
        // Criar conteúdo público e aprovado
        Conteudo conteudo = new Conteudo();
        conteudo.setTitle("Conteúdo Público");
        conteudo.setBody("Este conteúdo é público");
        conteudo.setPublic(true);
        conteudo.setStatus(ContentStatus.APPROVED);
        conteudo.setAuthor(autorTeste);
        conteudoRepository.save(conteudo);

        mockMvc.perform(get("/educabit/conteudo/publico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Conteúdo Público"));
    }

    @Test
    @DisplayName("GET /educabit/conteudo/publico NÃO deve retornar conteúdos privados")
    void publicoNaoDeveRetornarPrivado() throws Exception {
        Conteudo conteudoPrivado = new Conteudo();
        conteudoPrivado.setTitle("Conteúdo Privado");
        conteudoPrivado.setBody("Este conteúdo é privado");
        conteudoPrivado.setPublic(false);
        conteudoPrivado.setStatus(ContentStatus.APPROVED);
        conteudoPrivado.setAuthor(autorTeste);
        conteudoRepository.save(conteudoPrivado);

        mockMvc.perform(get("/educabit/conteudo/publico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("GET /educabit/conteudo/publico NÃO deve retornar conteúdos PENDING")
    void publicoNaoDeveRetornarPending() throws Exception {
        Conteudo conteudoPending = new Conteudo();
        conteudoPending.setTitle("Conteúdo Pendente");
        conteudoPending.setBody("Este conteúdo espera aprovação");
        conteudoPending.setPublic(true);
        conteudoPending.setStatus(ContentStatus.PENDING);
        conteudoPending.setAuthor(autorTeste);
        conteudoRepository.save(conteudoPending);

        mockMvc.perform(get("/educabit/conteudo/publico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @WithMockUser(username = "user_sub", roles = "USER")
    @DisplayName("GET /educabit/conteudo deve retornar conteúdos aprovados para usuário logado")
    void listarConteudoParaSubscriber() throws Exception {
        // Conteúdo aprovado (privado mas aprovado, deve aparecer para subscriber)
        Conteudo conteudo = new Conteudo();
        conteudo.setTitle("Conteúdo Subscriber");
        conteudo.setBody("Conteúdo para assinantes");
        conteudo.setPublic(false);
        conteudo.setStatus(ContentStatus.APPROVED);
        conteudo.setAuthor(autorTeste);
        conteudoRepository.save(conteudo);

        mockMvc.perform(get("/educabit/conteudo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Conteúdo Subscriber"));
    }

    @Test
    @DisplayName("GET /educabit/conteudo sem autenticação deve redirecionar para login")
    void conteudoSemAuthDeveRedirecionar() throws Exception {
        mockMvc.perform(get("/educabit/conteudo"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "autor_conteudo", roles = "CREATOR")
    @DisplayName("POST /educabit/conteudo deve criar conteúdo como CREATOR")
    void criarConteudoComoCriador() throws Exception {
        String json = """
                {
                    "title": "Novo Conteúdo Criador",
                    "body": "Corpo do conteúdo criado",
                    "isPublic": true
                }
                """;

        mockMvc.perform(post("/educabit/conteudo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Novo Conteúdo Criador"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser(username = "user_sem_permissao", roles = "USER")
    @DisplayName("POST /educabit/conteudo deve ser negado para USER (sem role CREATOR)")
    void criarConteudoComoUserDeveSerNegado() throws Exception {
        String json = """
                {
                    "title": "Tentativa",
                    "body": "Corpo",
                    "isPublic": false
                }
                """;

        mockMvc.perform(post("/educabit/conteudo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
    }
}
