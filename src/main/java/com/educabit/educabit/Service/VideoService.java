package com.educabit.demo.business;

import com.educabit.demo.infrastructure.entitys.Video;
import com.educabit.demo.infrastructure.repository.VideoRepository;
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
        return videoRepository.findByTitulo_video(titulo_video)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado"));
    }

    public void deletarVideoPorTitulo_video(String titulo_video) {
        videoRepository.deleteByTitulo_video(titulo_video);
    }

    public void atualizarVideoPorTitulo(Video video) {
        Video videoExistente = videoRepository.findByTitulo_video(video.getTitulo_video())
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado"));

        videoExistente.setDescricao_video(video.getDescricao_video());
        videoExistente.setPilar_pc(video.getPilar_pc());
        videoRepository.saveAndFlush(videoExistente);
    }
}
