package com.example.homework.controller;

import com.example.homework.domain.Inquiry;
import com.example.homework.dto.InquiryDto;
import com.example.homework.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private static final Logger logger = LoggerFactory.getLogger(InquiryController.class);

    private final InquiryService inquiryService;

    @GetMapping
    public String getInquiryList(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String searchKeyword,
                                 Model model) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Inquiry> inquiryPage;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                inquiryPage = inquiryService.searchInquiries(searchKeyword, pageable);
            } else {
                inquiryPage = inquiryService.getAllInquiries(pageable);
            }
            model.addAttribute("inquiryPage", inquiryPage);
            model.addAttribute("searchKeyword", searchKeyword);
            return "inquiries";
        } catch (Exception e) {
            logger.error("Error fetching inquiry list: ", e);
            model.addAttribute("errorMessage", "문의 목록을 불러오는 중 오류가 발생했습니다.");
            return "error"; // You might want to create a dedicated error page
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditInquiryForm(@PathVariable("id") Long id, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inquiry Id:" + id));
        model.addAttribute("inquiryDto", new InquiryDto.CreateRequest(inquiry.getTitle(), inquiry.getContent()));
        model.addAttribute("inquiryId", id);
        return "inquiry-form";
    }

    @PostMapping("/edit/{id}")
    public String updateInquiry(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("inquiryDto") InquiryDto.CreateRequest inquiryDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inquiry-form";
        }
        try {
            inquiryService.updateInquiry(id, inquiryDto.getTitle(), inquiryDto.getContent());
            redirectAttributes.addFlashAttribute("message", "문의글이 성공적으로 수정되었습니다.");
            return "redirect:/inquiries/" + id;
        } catch (Exception e) {
            bindingResult.reject("updateError", "문의글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "inquiry-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteInquiry(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        inquiryService.deleteInquiry(id);
        redirectAttributes.addFlashAttribute("message", "문의글이 성공적으로 삭제되었습니다.");
        return "redirect:/inquiries";
    }

    @GetMapping("/new")
    public String showInquiryForm(Model model) {
        model.addAttribute("inquiryDto", new InquiryDto.CreateRequest());
        return "inquiry-form";
    }

    @PostMapping("/new")
    public String createInquiry(@Valid @ModelAttribute("inquiryDto") InquiryDto.CreateRequest inquiryDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inquiry-form";
        }
        try {
            inquiryService.createInquiry(inquiryDto.getTitle(), inquiryDto.getContent());
            redirectAttributes.addFlashAttribute("message", "문의글이 성공적으로 등록되었습니다.");
            return "redirect:/inquiries";
        } catch (Exception e) {
            bindingResult.reject("creationError", "문의글 등록 중 오류가 발생했습니다: " + e.getMessage());
            return "inquiry-form";
        }
    }

    @GetMapping("/{id}")
    public String getInquiryDetail(@PathVariable("id") Long id, Model model) {
        Optional<Inquiry> inquiryOptional = inquiryService.getInquiryById(id);
        if (inquiryOptional.isPresent()) {
            model.addAttribute("inquiry", inquiryOptional.get());
            return "inquiry-detail";
        } else {
            return "redirect:/inquiries"; // Or an error page
        }
    }

    @PostMapping("/{id}/like")
    public String likeInquiry(@PathVariable("id") Long id) {
        inquiryService.incrementLikes(id);
        return "redirect:/inquiries/" + id;
    }
}
