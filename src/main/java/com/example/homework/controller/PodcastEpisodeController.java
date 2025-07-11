package com.example.homework.controller;

import com.example.homework.domain.PodcastEpisode;
import com.example.homework.service.PodcastEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
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
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditPodcastForm(@PathVariable("id") Long id, Model model) {
        PodcastEpisode episode = podcastEpisodeService.getPodcastEpisodeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid podcast episode Id:" + id));
        model.addAttribute("podcastEpisode", episode);
        return "podcast-form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updatePodcast(@PathVariable("id") Long id,
                                @ModelAttribute PodcastEpisode podcastEpisode,
                                RedirectAttributes redirectAttributes) {
        podcastEpisodeService.updatePodcastEpisode(id, podcastEpisode);
        redirectAttributes.addFlashAttribute("message", "팟캐스트 에피소드가 성공적으로 수정되었습니다.");
        return "redirect:/podcast/" + id;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePodcast(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        podcastEpisodeService.deletePodcastEpisode(id);
        redirectAttributes.addFlashAttribute("message", "팟캐스트 에피소드가 성공적으로 삭제되었습니다.");
        return "redirect:/podcast";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showPodcastForm(Model model) {
        model.addAttribute("podcastEpisode", new PodcastEpisode());
        return "podcast-form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
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
        podcastEpisodeService.incrementViews(id); // 조회수 증가
        Optional<PodcastEpisode> episodeOptional = podcastEpisodeService.getPodcastEpisodeById(id);
        if (episodeOptional.isPresent()) {
            model.addAttribute("episode", episodeOptional.get());
            return "podcast-detail";
        } else {
            return "redirect:/podcast"; // Or an error page
        }
    }

    @PostMapping("/{id}/like")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likePodcast(@PathVariable("id") Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> response = new HashMap<>();
        try {
            if (podcastEpisodeService.incrementLikes(id, username)) {
                response.put("liked", true);
                response.put("newLikes", podcastEpisodeService.getPodcastEpisodeById(id).get().getLikes()); // 업데이트된 좋아요 수 반환
                return ResponseEntity.ok(response);
            } else {
                response.put("liked", false);
                return ResponseEntity.ok(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "좋아요 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}