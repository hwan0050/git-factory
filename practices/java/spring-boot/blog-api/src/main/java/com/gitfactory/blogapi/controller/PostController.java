package com.gitfactory.blogapi.controller;

import com.gitfactory.blogapi.dto.PostRequest;
import com.gitfactory.blogapi.dto.PostResponse;
import com.gitfactory.blogapi.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시글 관리 REST API 컨트롤러
 */
@Tag(name = "게시글 API", description = "게시글 CRUD 및 검색 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        PostResponse post = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequest request
    ) {
        PostResponse post = postService.updatePost(id, request);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam String keyword) {
        List<PostResponse> posts = postService.searchByTitle(keyword);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<PostResponse>> getPostsByAuthor(@PathVariable String author) {
        List<PostResponse> posts = postService.getPostsByAuthor(author);
        return ResponseEntity.ok(posts);
    }
}