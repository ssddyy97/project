package com.example.homework.controller;

import com.example.homework.domain.PodcastEpisode;
import com.example.homework.service.PodcastEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/podcast")
@RequiredArgsConstructor
public class PodcastEpisodeController {

    private final PodcastEpisodeService podcastEpisodeService;

    @GetMapping
    public String getPodcastList(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String searchKeyword,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PodcastEpisode> podcastPage;
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            podcastPage = podcastEpisodeService.searchPodcastEpisodes(searchKeyword, pageable);
        } else {
            podcastPage = podcastEpisodeService.getAllPodcastEpisodes(pageable);
        }
        model.addAttribute("podcastPage", podcastPage);
        model.addAttribute("searchKeyword", searchKeyword);
        return "podcast";
    }

    @GetMapping("/edit/{id}")
    public String showEditPodcastForm(@PathVariable("id") Long id, Model model) {
        PodcastEpisode episode = podcastEpisodeService.getPodcastEpisodeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid podcast episode Id:" + id));
        model.addAttribute("podcastEpisode", episode);
        return "podcast-form";
    }

    @PostMapping("/edit/{id}")
    public String updatePodcast(@PathVariable("id") Long id,
                                @ModelAttribute PodcastEpisode podcastEpisode,
                                RedirectAttributes redirectAttributes) {
        podcastEpisodeService.updatePodcastEpisode(id, podcastEpisode);
        redirectAttributes.addFlashAttribute("message", "팟캐스트 에피소드가 성공적으로 수정되었습니다.");
        return "redirect:/podcast/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deletePodcast(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        podcastEpisodeService.deletePodcastEpisode(id);
        redirectAttributes.addFlashAttribute("message", "팟캐스트 에피소드가 성공적으로 삭제되었습니다.");
        return "redirect:/podcast";
    }

    @GetMapping("/new")
    public String showPodcastForm(Model model) {
        model.addAttribute("podcastEpisode", new PodcastEpisode());
        return "podcast-form";
    }

    @PostMapping("/new")
    public String createPodcast(@ModelAttribute PodcastEpisode podcastEpisode,
                                RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        podcastEpisode.setAuthor(username);
        podcastEpisodeService.savePodcastEpisode(podcastEpisode);
        redirectAttributes.addFlashAttribute("message", "팟캐스트 에피소드가 성공적으로 등록되었습니다.");
        return "redirect:/podcast";
    }

    @GetMapping("/{id}")
    public String getPodcastDetail(@PathVariable("id") Long id, Model model) {
        Optional<PodcastEpisode> episodeOptional = podcastEpisodeService.getPodcastEpisodeById(id);
        if (episodeOptional.isPresent()) {
            model.addAttribute("episode", episodeOptional.get());
            return "podcast-detail";
        } else {
            return "redirect:/podcast"; // Or an error page
        }
    }

    @PostMapping("/{id}/like")
    public String likePodcast(@PathVariable("id") Long id) {
        podcastEpisodeService.incrementLikes(id);
        return "redirect:/podcast/" + id;
    }
}