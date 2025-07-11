package com.example.homework.service;

import com.example.homework.domain.PodcastEpisode;
import com.example.homework.domain.PodcastLike;
import com.example.homework.domain.User;
import com.example.homework.repository.PodcastEpisodeRepository;
import com.example.homework.repository.PodcastLikeRepository;
import com.example.homework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PodcastEpisodeService {

    private final PodcastEpisodeRepository podcastEpisodeRepository;
    private final UserRepository userRepository;
    private final PodcastLikeRepository podcastLikeRepository;

    @Transactional
    public PodcastEpisode savePodcastEpisode(PodcastEpisode podcastEpisode) {
        return podcastEpisodeRepository.save(podcastEpisode);
    }

    @Transactional(readOnly = true)
    public Page<PodcastEpisode> getAllPodcastEpisodes(Pageable pageable) {
        return podcastEpisodeRepository.findAll(pageable);
    }

    @Transactional
    public Optional<PodcastEpisode> getPodcastEpisodeById(Long id) {
        Optional<PodcastEpisode> episodeOptional = podcastEpisodeRepository.findById(id);
        return episodeOptional;
    }

    @Transactional
    public void incrementViews(Long id) {
        PodcastEpisode episode = podcastEpisodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Podcast episode not found"));
        episode.setViews(episode.getViews() + 1);
        podcastEpisodeRepository.save(episode);
    }

    @Transactional(readOnly = true)
    public Page<PodcastEpisode> searchPodcastEpisodes(String keyword, Pageable pageable) {
        return podcastEpisodeRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Transactional
    public boolean incrementLikes(Long episodeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        PodcastEpisode episode = podcastEpisodeRepository.findById(episodeId)
                .orElseThrow(() -> new IllegalArgumentException("Podcast episode not found"));

        if (podcastLikeRepository.findByUserAndPodcastEpisode(user, episode).isPresent()) {
            return false; // 이미 좋아요를 눌렀음
        }

        // 좋아요 기록
        PodcastLike podcastLike = PodcastLike.builder()
                .user(user)
                .podcastEpisode(episode)
                .build();
        podcastLikeRepository.save(podcastLike);

        // 좋아요 수 증가
        episode.setLikes(episode.getLikes() + 1);
        podcastEpisodeRepository.save(episode);
        return true;
    }

    @Transactional
    public PodcastEpisode updatePodcastEpisode(Long id, PodcastEpisode updatedEpisode) {
        PodcastEpisode existingEpisode = podcastEpisodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Podcast episode not found"));
        existingEpisode.setTitle(updatedEpisode.getTitle());
        existingEpisode.setDescription(updatedEpisode.getDescription());
        existingEpisode.setSpotifyUrl(updatedEpisode.getSpotifyUrl());
        return podcastEpisodeRepository.save(existingEpisode);
    }

    @Transactional
    public void deletePodcastEpisode(Long id) {
        podcastEpisodeRepository.deleteById(id);
    }
}