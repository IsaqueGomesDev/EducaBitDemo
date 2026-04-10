package com.educabit.educabit.controllers;

import com.educabit.educabit.model.Video;
import com.educabit.educabit.services.VideoService;
import com.educabit.educabit.dtos.VideoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {

    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public ResponseEntity<?> salvarVideo(@RequestBody @NonNull VideoDto videoDto) {
        logger.info("Tentando salvar vÃ­deo: {}", videoDto.tituloVideo());
        try {
            Video video = new Video();
            video.setTituloVideo(videoDto.tituloVideo());
            video.setDescricaoVideo(videoDto.descricaoVideo());
            video.setPilar_pc(videoDto.pilar_pc());

            videoService.salvarVideo(video);
            logger.info("VÃ­deo salvo com sucesso: {}", videoDto.tituloVideo());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Erro ao salvar vÃ­deo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar vÃ­deo: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Video>> listarVideos() {
        return ResponseEntity.ok(videoService.listarTodos());
    }

    @GetMapping
    public ResponseEntity<Video> buscarVideoPorTituloVideo(@RequestParam String tituloVideo) {
        try {
            return ResponseEntity.ok(videoService.buscarVideoPorTitulo_video(tituloVideo));
        } catch (RuntimeException e) {
            logger.warn("VÃ­deo nÃ£o encontrado para busca: {}", tituloVideo);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarVideoPorTituloVideo(@RequestParam String tituloVideo) {
        try {
            videoService.deletarVideoPorTitulo_video(tituloVideo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.warn("Erro ao deletar vÃ­deo: {} - {}", tituloVideo, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<?> atualizarVideoPorTituloVideo(@RequestBody @NonNull VideoDto videoDto) {
        logger.info("Tentando atualizar vÃ­deo: {}", videoDto.tituloVideo());
        try {
            Video video = new Video();
            video.setTituloVideo(videoDto.tituloVideo());
            video.setDescricaoVideo(videoDto.descricaoVideo());
            video.setPilar_pc(videoDto.pilar_pc());

            videoService.atualizarVideoPorTitulo(video);
            logger.info("VÃ­deo atualizado com sucesso: {}", videoDto.tituloVideo());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Erro ao atualizar vÃ­deo", e);
            return ResponseEntity.notFound().build();
        }
    }
}

