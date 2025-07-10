package com.example.homework.dto;

import com.example.homework.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

    /**
     * 회원가입 요청 DTO
     */
    @Data
    public static class SignUpRequest {
        @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
        @Size(min = 2, max = 20, message = "사용자 이름은 2자 이상 20자 이하로 입력해주세요.")
        private String username;

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        private String password;

        @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
        private String confirmPassword;

        @Pattern(regexp = "^[0-9]{10,11}$", message = "유효한 전화번호 형식을 입력해주세요 (예: 01012345678).")
        private String phoneNumber;

        // DTO를 User 엔티티로 변환하는 메소드
        // 비밀번호 암호화는 서비스 계층에서 처리하는 것이 더 일반적입니다.
        public User toEntity() {
            User user = new User();
            user.setUsername(this.username);
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setPhoneNumber(this.phoneNumber);
            return user;
        }
    }

    /**
     * API 응답 DTO
     */
    @Getter
    @AllArgsConstructor
    public static class Response {
        private final Long id;
        private final String username;
        private final String email;

        // User 엔티티를 DTO로 변환하는 정적 팩토리 메소드
        public static Response fromEntity(User user) {
            return new Response(user.getId(), user.getUsername(), user.getEmail());
        }
    }
}
