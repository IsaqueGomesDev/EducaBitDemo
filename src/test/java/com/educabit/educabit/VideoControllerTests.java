package com.educabit.educabit;

import com.educabit.educabit.Model.Video;
import com.educabit.educabit.Repositores.VideoRepository;
import com.educabit.educabit.Services.VideoService;
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
class VideoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoService videoService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /video com VideoDto deve criar vídeo (fix #8, usa DTO em vez de entidade)")
    void criarVideoComDto() throws Exception {
        String json = """
                {
                    "tituloVideo": "Introdução ao Pensamento Computacional",
                    "descricaoVideo": "Vídeo introdutório sobre PC",
                    "pilar_pc": "Abstração"
                }
                """;

        mockMvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        // Verificar que o vídeo foi salvo
        assertTrue(videoRepository.findByTituloVideo("Introdução ao Pensamento Computacional").isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /video/all deve listar todos os vídeos")
    void listarTodosVideos() throws Exception {
        Video video = new Video();
        video.setTituloVideo("Video Test");
        video.setDescricaoVideo("Descrição");
        video.setPilar_pc("Decomposição");
        videoRepository.save(video);

        mockMvc.perform(get("/video/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tituloVideo").value("Video Test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /video?tituloVideo= deve encontrar vídeo pelo título")
    void buscarVideoPorTitulo() throws Exception {
        Video video = new Video();
        video.setTituloVideo("Video Busca Test");
        video.setDescricaoVideo("Descrição Busca");
        video.setPilar_pc("Reconhecimento de Padrões");
        videoRepository.save(video);

        mockMvc.perform(get("/video").param("tituloVideo", "Video Busca Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tituloVideo").value("Video Busca Test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /video?tituloVideo= com título inexistente deve retornar 404")
    void buscarVideoInexistente() throws Exception {
        mockMvc.perform(get("/video").param("tituloVideo", "Não Existe"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /video com VideoDto deve atualizar vídeo (fix #8)")
    void atualizarVideoComDto() throws Exception {
        Video video = new Video();
        video.setTituloVideo("Video Update Test");
        video.setDescricaoVideo("Descrição original");
        video.setPilar_pc("Algoritmos");
        videoRepository.save(video);

        String json = """
                {
                    "tituloVideo": "Video Update Test",
                    "descricaoVideo": "Descrição atualizada",
                    "pilar_pc": "Abstração"
                }
                """;

        mockMvc.perform(put("/video")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        Video updated = videoRepository.findByTituloVideo("Video Update Test").orElseThrow();
        assertEquals("Descrição atualizada", updated.getDescricaoVideo());
        assertEquals("Abstração", updated.getPilar_pc());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /video?tituloVideo= deve remover vídeo")
    void deletarVideo() throws Exception {
        Video video = new Video();
        video.setTituloVideo("Video Delete Test");
        video.setDescricaoVideo("Descrição");
        video.setPilar_pc("Decomposição");
        videoRepository.save(video);

        mockMvc.perform(delete("/video").param("tituloVideo", "Video Delete Test"))
                .andExpect(status().isOk());

        assertTrue(videoRepository.findByTituloVideo("Video Delete Test").isEmpty());
    }
}
