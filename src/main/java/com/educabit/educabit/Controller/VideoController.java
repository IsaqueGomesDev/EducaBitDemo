package com.educabit.educabit.Controller;

import com.educabit.educabit.Service.VideoService;
import com.educabit.educabit.Model.Video;
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
    public ResponseEntity<Video> buscarVideoPorTituloVideo(@RequestParam String tituloVideo){
        return ResponseEntity.ok(videoService.buscarVideoPorTitulo_video(tituloVideo));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarVideoPorTituloVideo(@RequestParam String tituloVideo){
        videoService.deletarVideoPorTitulo_video(tituloVideo);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarVideoPorTituloVideo(@RequestBody Video video){
        videoService.atualizarVideoPorTitulo(video);
        return ResponseEntity.ok().build();
    }
}
