package com.example.homework.controller;

import com.example.homework.dto.NoticeDto;
import com.example.homework.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 목록 페이지
    @GetMapping
    public String getNotices(Model model,
                             @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NoticeDto.Response> notices = noticeService.getAllNotices(pageable);
        model.addAttribute("notices", notices);
        return "notices"; // notices.html 템플릿 반환
    }

    // 공지사항 상세 페이지
    @GetMapping("/{id}")
    public String getNoticeDetail(@PathVariable Long id, Model model) {
        try {
            NoticeDto.Response notice = noticeService.getNoticeById(id);
            model.addAttribute("notice", notice);
            return "notice-detail"; // notice-detail.html 템플릿 반환
        } catch (NoSuchElementException e) {
            return "error/404"; // 404 에러 페이지 반환
        }
    }

    // 공지사항 작성 폼 페이지 (관리자용)
    @GetMapping("/new")
    public String createNoticeForm(Model model) {
        model.addAttribute("notice", new NoticeDto.Request());
        return "notice-form"; // notice-form.html 템플릿 반환
    }

    // 공지사항 작성 처리 (관리자용)
    @PostMapping
    public String createNotice(@ModelAttribute NoticeDto.Request request) {
        noticeService.createNotice(request);
        return "redirect:/notices";
    }

    // 공지사항 수정 폼 페이지 (관리자용)
    @GetMapping("/{id}/edit")
    public String editNoticeForm(@PathVariable Long id, Model model) {
        try {
            NoticeDto.Response notice = noticeService.getNoticeById(id);
            model.addAttribute("notice", notice);
            return "notice-form"; // notice-form.html 템플릿 재사용
        } catch (NoSuchElementException e) {
            return "error/404";
        }
    }

    // 공지사항 수정 처리 (관리자용)
    @PostMapping("/{id}")
    public String updateNotice(@PathVariable Long id, @ModelAttribute NoticeDto.Request request) {
        noticeService.updateNotice(id, request);
        return "redirect:/notices";
    }

    // 공지사항 삭제 처리 (관리자용)
    @PostMapping("/{id}/delete")
    public String deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notices";
    }
}
