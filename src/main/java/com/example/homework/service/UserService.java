package com.example.homework.service;

import com.example.homework.domain.User;
import com.example.homework.dto.UserDto;
import com.example.homework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger; // Added
import org.slf4j.LoggerFactory; // Added

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성 (생성자 주입)
@Transactional(readOnly = true) // 클래스 전체에 읽기 전용 트랜잭션 적용
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // Added

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 사용자 생성 (회원가입)
     */
    @Transactional // 쓰기 작업이므로 readOnly=false 트랜잭션 적용
    public User createUser(UserDto.SignUpRequest requestDto) {
        logger.info("사용자 생성 시도: {}", requestDto.getUsername()); // Added log
        User user = requestDto.toEntity();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        user.setRole("ROLE_USER"); // Set default role
        validateDuplicateUser(user);
        User savedUser = userRepository.save(user);
        logger.info("사용자 저장 완료: {}", savedUser.getUsername()); // Added log
        return savedUser;
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(u -> {
                    logger.warn("중복 사용자 이름 감지: {}", user.getUsername()); // Added log
                    throw new IllegalStateException("이미 존재하는 사용자 이름입니다.");
                });

        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    logger.warn("중복 이메일 감지: {}", user.getEmail()); // Added log
                    throw new IllegalStateException("이미 사용 중인 이메일입니다.");
                });
    }

    /**
     * 전체 사용자 조회
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * ID로 사용자 조회
     */
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * 사용자 이름으로 조회
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    public User updateUser(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 예시: 비밀번호 변경
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
