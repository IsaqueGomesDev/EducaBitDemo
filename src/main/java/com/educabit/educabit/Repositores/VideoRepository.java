package com.educabit.educabit.Repositores;

import com.educabit.educabit.Model.Video;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Integer> {

    Optional<Video> findByTituloVideo(String tituloVideo);

    @Transactional
    void deleteByTituloVideo(String tituloVideo);
}