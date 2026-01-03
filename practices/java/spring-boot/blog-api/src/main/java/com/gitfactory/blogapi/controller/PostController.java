package com.gitfactory.blogapi.controller;

import com.gitfactory.blogapi.dto.PostRequest;
import com.gitfactory.blogapi.dto.PostResponse;
import com.gitfactory.blogapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시글 REST API 컨트롤러
 */
@Tag(name = "Post", description = "게시글 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "모든 게시글 조회", description = "등록된 모든 게시글을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 상세 조회", description = "ID로 특정 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        PostResponse createdPost = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable Long id,
            @RequestBody PostRequest request) {
        PostResponse updatedPost = postService.updatePost(id, request);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "제목으로 게시글 검색", description = "제목에 키워드가 포함된 게시글을 검색합니다.")
    @GetMapping("/search/title")
    public ResponseEntity<List<PostResponse>> searchByTitle(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword) {
        List<PostResponse> posts = postService.searchByTitle(keyword);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "작성자로 게시글 검색", description = "작성자 이름에 키워드가 포함된 게시글을 검색합니다.")
    @GetMapping("/search/author")
    public ResponseEntity<List<PostResponse>> searchByAuthor(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword) {
        List<PostResponse> posts = postService.getPostsByAuthor(keyword);
        return ResponseEntity.ok(posts);
    }
}