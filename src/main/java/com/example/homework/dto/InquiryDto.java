package com.example.homework.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class InquiryDto {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력 항목입니다.")
        private String content;

        public CreateRequest(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public CreateRequest() {
        } // Lombok @Data will generate this, but explicit for clarity
    }
}
