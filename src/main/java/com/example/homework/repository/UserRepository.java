package com.example.homework.repository;

import com.example.homework.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 이름으로 사용자를 찾는 커스텀 메소드
    // Spring Data JPA가 메소드 이름을 분석하여 자동으로 쿼리를 생성합니다.
    Optional<User> findByUsername(String username);

    // 이메일로 사용자를 찾는 커스텀 메소드
    Optional<User> findByEmail(String email);
}
