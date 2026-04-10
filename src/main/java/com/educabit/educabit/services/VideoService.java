package com.educabit.educabit.services;

import com.educabit.educabit.model.Video;
import com.educabit.educabit.repositories.VideoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void salvarVideo(@NonNull Video video) {
        videoRepository.saveAndFlush(video);
    }

    public Video buscarVideoPorTitulo_video(String tituloVideo) {
        return videoRepository.findByTituloVideo(tituloVideo)
                .orElseThrow(() -> new RuntimeException("VÃ­deo nÃ£o encontrado"));
    }

    public void deletarVideoPorTitulo_video(String tituloVideo) {
        videoRepository.deleteByTituloVideo(tituloVideo);
    }

    public void atualizarVideoPorTitulo(Video video) {
        Video videoExistente = videoRepository.findByTituloVideo(video.getTituloVideo())
                .orElseThrow(() -> new RuntimeException("VÃ­deo nÃ£o encontrado"));

        videoExistente.setDescricaoVideo(video.getDescricaoVideo());
        videoExistente.setPilar_pc(video.getPilar_pc());
        videoRepository.saveAndFlush(videoExistente);
    }

    public List<Video> listarTodos() {
        return videoRepository.findAll();
    }
}


