package com.gitfactory.blogapi.dto;

import com.gitfactory.blogapi.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 게시글 응답 DTO
 */
@Schema(description = "게시글 응답")
public record PostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,

        @Schema(description = "게시글 제목", example = "Spring Boot 학습하기")
        String title,

        @Schema(description = "게시글 내용", example = "Spring Boot는 Java 기반의 웹 프레임워크입니다.")
        String content,

        @Schema(description = "작성자", example = "홍길동")
        String author,

        @Schema(description = "생성일시", example = "2025-12-06T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "수정일시", example = "2025-12-06T15:30:00")
        LocalDateTime updatedAt
) {
    /**
     * Post Entity를 PostResponse DTO로 변환
     *
     * @param post Post Entity
     * @return PostResponse DTO
     */
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}