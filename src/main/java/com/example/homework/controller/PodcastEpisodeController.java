package com.example.homework.controller;

import com.example.homework.domain.PodcastEpisode;
import com.example.homework.service.PodcastEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/podcasts")
@RequiredArgsConstructor
public class PodcastEpisodeController {

    private final PodcastEpisodeService podcastEpisodeService;

    @PostMapping
    public ResponseEntity<PodcastEpisode> createPodcastEpisode(@RequestBody PodcastEpisode podcastEpisode) {
        PodcastEpisode savedEpisode = podcastEpisodeService.savePodcastEpisode(podcastEpisode);
        return new ResponseEntity<>(savedEpisode, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PodcastEpisode>> getAllPodcastEpisodes(
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PodcastEpisode> episodes = podcastEpisodeService.getAllPodcastEpisodes(pageable);
        return ResponseEntity.ok(episodes);
    }
}
