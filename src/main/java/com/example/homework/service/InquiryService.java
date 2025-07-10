package com.example.homework.service;

import com.example.homework.domain.Inquiry;
import com.example.homework.domain.User;
import com.example.homework.repository.InquiryRepository;
import com.example.homework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Inquiry createInquiry(String title, String content) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Inquiry inquiry = new Inquiry();
        inquiry.setTitle(title);
        inquiry.setContent(content);
        inquiry.setAuthor(author);
        inquiry.setViews(0);
        inquiry.setLikes(0);

        return inquiryRepository.save(inquiry);
    }

    public Page<Inquiry> getAllInquiries(Pageable pageable) {
        return inquiryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Inquiry> searchInquiries(String keyword, Pageable pageable) {
        return inquiryRepository.findByTitleContainingIgnoreCaseOrAuthor_UsernameContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Transactional
    public Optional<Inquiry> getInquiryById(Long id) {
        Optional<Inquiry> inquiryOptional = inquiryRepository.findById(id);
        inquiryOptional.ifPresent(inquiry -> {
            inquiry.setViews(inquiry.getViews() + 1);
            inquiryRepository.save(inquiry);
        });
        return inquiryOptional;
    }

    @Transactional
    public Inquiry incrementLikes(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));
        inquiry.setLikes(inquiry.getLikes() + 1);
        return inquiryRepository.save(inquiry);
    }

    @Transactional
    public Inquiry updateInquiry(Long id, String title, String content) {
        Inquiry existingInquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));
        existingInquiry.setTitle(title);
        existingInquiry.setContent(content);
        return inquiryRepository.save(existingInquiry);
    }

    @Transactional
    public void deleteInquiry(Long id) {
        inquiryRepository.deleteById(id);
    }
}
