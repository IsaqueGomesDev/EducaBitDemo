package com.educabit.educabit.repositories;

import com.educabit.educabit.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    Optional<Video> findByTituloVideo(String tituloVideo);
    void deleteByTituloVideo(String tituloVideo);
}
