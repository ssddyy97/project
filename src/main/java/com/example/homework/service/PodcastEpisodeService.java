package com.example.homework.service;

import com.example.homework.domain.PodcastEpisode;
import com.example.homework.repository.PodcastEpisodeRepository;
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
        episodeOptional.ifPresent(episode -> {
            episode.setViews(episode.getViews() + 1);
            podcastEpisodeRepository.save(episode);
        });
        return episodeOptional;
    }

    @Transactional(readOnly = true)
    public Page<PodcastEpisode> searchPodcastEpisodes(String keyword, Pageable pageable) {
        return podcastEpisodeRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Transactional
    public PodcastEpisode incrementLikes(Long id) {
        PodcastEpisode episode = podcastEpisodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Podcast episode not found"));
        episode.setLikes(episode.getLikes() + 1);
        return podcastEpisodeRepository.save(episode);
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