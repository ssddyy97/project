package com.example.homework.repository;

import com.example.homework.domain.PodcastEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PodcastEpisodeRepository extends JpaRepository<PodcastEpisode, Long> {
}
