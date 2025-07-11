package com.example.homework.dto;

import com.example.homework.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NoticeDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long id;
        private String title;
        private String content;

        public Notice toEntity() {
            return Notice.builder()
                    .title(title)
                    .content(content)
                    .viewCount(0L) // 초기 조회수 0
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private Long viewCount;

        public static Response from(Notice notice) {
            return Response.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .createdAt(notice.getCreatedAt())
                    .viewCount(notice.getViewCount())
                    .build();
        }
    }
}
