package com.example.homework.repository;

import com.example.homework.domain.PodcastLike;
import com.example.homework.domain.User;
import com.example.homework.domain.PodcastEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PodcastLikeRepository extends JpaRepository<PodcastLike, Long> {
    Optional<PodcastLike> findByUserAndPodcastEpisode(User user, PodcastEpisode podcastEpisode);
}
