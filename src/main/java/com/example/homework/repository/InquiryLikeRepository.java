package com.example.homework.repository;

import com.example.homework.domain.InquiryLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquiryLikeRepository extends JpaRepository<InquiryLike, Long> {
    Optional<InquiryLike> findByUserIdAndInquiryId(Long userId, Long inquiryId);
}
