package com.educabit.demo.controller;

import com.educabit.educabit.Model.Video;
import com.educabit.educabit.Service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<Void> salvarVideo(@RequestBody Video video){
        videoService.salvarVideo(video);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Video> buscarVideoPorTitulo_video(@RequestParam String titulo_video){
        return ResponseEntity.ok(videoService.buscarVideoPorTitulo_video(titulo_video));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarVideoPorTitulo_video(@RequestParam String titulo_video){
        videoService.deletarVideoPorTitulo_video(titulo_video);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarVideoPorTitulo_video(@RequestBody Video video){
        videoService.atualizarVideoPorTitulo(video);
        return ResponseEntity.ok().build();
    }
}
