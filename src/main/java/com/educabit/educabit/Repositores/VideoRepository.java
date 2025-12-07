package com.educabit.demo.infrastructure.repository;


import com.educabit.educabit.Model.Video;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Integer> {

    Optional<Video> findByTitulo_video(String titulo_video);

    @Transactional
    void deleteByTitulo_video(String titulo_video);
}
