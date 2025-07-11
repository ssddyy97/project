package com.example.homework.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "podcast_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "podcast_episode_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PodcastLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "podcast_episode_id", nullable = false)
    private PodcastEpisode podcastEpisode;
}
