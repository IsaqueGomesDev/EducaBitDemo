package com.educabit.educabit.Service;

import com.educabit.educabit.Model.Video;
import com.educabit.educabit.Repositores.VideoRepository;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void salvarVideo(Video video) {
        videoRepository.saveAndFlush(video);
    }

    public Video buscarVideoPorTitulo_video(String titulo_video) {
        return videoRepository.findByTituloVideo(titulo_video)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado"));
    }

    public void deletarVideoPorTitulo_video(String titulo_video) {
        videoRepository.deleteByTituloVideo(titulo_video);
    }

    public void atualizarVideoPorTitulo(Video video) {
        Video videoExistente = videoRepository.findByTituloVideo(video.getTituloVideo())
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado"));

        videoExistente.setDescricaoVideo(video.getDescricaoVideo());
        videoExistente.setPilar_pc(video.getPilar_pc());
        videoRepository.saveAndFlush(videoExistente);
    }
}
