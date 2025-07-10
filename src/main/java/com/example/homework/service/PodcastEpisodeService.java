package com.example.homework.service;

import com.example.homework.domain.PodcastEpisode;
import com.example.homework.repository.PodcastEpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
