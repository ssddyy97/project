package com.example.homework.repository;

import com.example.homework.domain.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    @EntityGraph(attributePaths = "author")
    Page<Inquiry> findByTitleContainingIgnoreCaseOrAuthor_UsernameContainingIgnoreCase(String title, String authorUsername, Pageable pageable);

    @EntityGraph(attributePaths = "author")
    Page<Inquiry> findAll(Pageable pageable);
}