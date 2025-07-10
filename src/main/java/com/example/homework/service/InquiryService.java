package com.example.homework.service;

import com.example.homework.domain.Inquiry;
import com.example.homework.domain.InquiryLike;
import com.example.homework.domain.User;
import com.example.homework.repository.InquiryLikeRepository;
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
    private final InquiryLikeRepository inquiryLikeRepository;

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));

        Optional<InquiryLike> existingLike = inquiryLikeRepository.findByUserIdAndInquiryId(user.getId(), id);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }

        InquiryLike inquiryLike = new InquiryLike();
        inquiryLike.setUser(user);
        inquiryLike.setInquiry(inquiry);
        inquiryLikeRepository.save(inquiryLike);

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
