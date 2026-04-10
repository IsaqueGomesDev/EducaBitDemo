package com.educabit.educabit;

import com.educabit.educabit.model.Video;
import com.educabit.educabit.repositories.VideoRepository;
import com.educabit.educabit.services.VideoService;
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
    @DisplayName("POST /video com VideoDto deve criar vÃ­deo (fix #8, usa DTO em vez de entidade)")
    void criarVideoComDto() throws Exception {
        String json = """
                {
                    "tituloVideo": "IntroduÃ§Ã£o ao Pensamento Computacional",
                    "descricaoVideo": "VÃ­deo introdutÃ³rio sobre PC",
                    "pilar_pc": "AbstraÃ§Ã£o"
                }
                """;

        mockMvc.perform(post("/video")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        // Verificar que o vÃ­deo foi salvo
        assertTrue(videoRepository.findByTituloVideo("IntroduÃ§Ã£o ao Pensamento Computacional").isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /video/all deve listar todos os vÃ­deos")
    void listarTodosVideos() throws Exception {
        Video video = new Video();
        video.setTituloVideo("Video Test");
        video.setDescricaoVideo("DescriÃ§Ã£o");
        video.setPilar_pc("DecomposiÃ§Ã£o");
        videoRepository.save(video);

        mockMvc.perform(get("/video/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tituloVideo").value("Video Test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /video?tituloVideo= deve encontrar vÃ­deo pelo tÃ­tulo")
    void buscarVideoPorTitulo() throws Exception {
        Video video = new Video();
        video.setTituloVideo("Video Busca Test");
        video.setDescricaoVideo("DescriÃ§Ã£o Busca");
        video.setPilar_pc("Reconhecimento de PadrÃµes");
        videoRepository.save(video);

        mockMvc.perform(get("/video").param("tituloVideo", "Video Busca Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tituloVideo").value("Video Busca Test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /video?tituloVideo= com tÃ­tulo inexistente deve retornar 404")
    void buscarVideoInexistente() throws Exception {
        mockMvc.perform(get("/video").param("tituloVideo", "NÃ£o Existe"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /video com VideoDto deve atualizar vÃ­deo (fix #8)")
    void atualizarVideoComDto() throws Exception {
        Video video = new Video();
        video.setTituloVideo("Video Update Test");
        video.setDescricaoVideo("DescriÃ§Ã£o original");
        video.setPilar_pc("Algoritmos");
        videoRepository.save(video);

        String json = """
                {
                    "tituloVideo": "Video Update Test",
                    "descricaoVideo": "DescriÃ§Ã£o atualizada",
                    "pilar_pc": "AbstraÃ§Ã£o"
                }
                """;

        mockMvc.perform(put("/video")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        Video updated = videoRepository.findByTituloVideo("Video Update Test").orElseThrow();
        assertEquals("DescriÃ§Ã£o atualizada", updated.getDescricaoVideo());
        assertEquals("AbstraÃ§Ã£o", updated.getPilar_pc());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /video?tituloVideo= deve remover vÃ­deo")
    void deletarVideo() throws Exception {
        Video video = new Video();
        video.setTituloVideo("Video Delete Test");
        video.setDescricaoVideo("DescriÃ§Ã£o");
        video.setPilar_pc("DecomposiÃ§Ã£o");
        videoRepository.save(video);

        mockMvc.perform(delete("/video").param("tituloVideo", "Video Delete Test"))
                .andExpect(status().isOk());

        assertTrue(videoRepository.findByTituloVideo("Video Delete Test").isEmpty());
    }
}


