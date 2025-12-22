package com.gitfactory.blogapi.dto;

import com.gitfactory.blogapi.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 게시글 생성/수정 요청 DTO
 */
@Schema(description = "게시글 생성/수정 요청")
public record PostRequest(
        @Schema(description = "게시글 제목", example = "Spring Boot 학습하기", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "게시글 내용", example = "Spring Boot는 Java 기반의 웹 프레임워크입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
        String content,

        @Schema(description = "작성자", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
        String author
) {
    /**
     * PostRequest DTO를 Post Entity로 변환
     *
     * @return Post Entity
     */
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}