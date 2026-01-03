package com.gitfactory.blogapi.service;

import com.gitfactory.blogapi.dto.PostRequest;
import com.gitfactory.blogapi.dto.PostResponse;
import com.gitfactory.blogapi.entity.Post;
import com.gitfactory.blogapi.entity.User;
import com.gitfactory.blogapi.exception.ResourceNotFoundException;
import com.gitfactory.blogapi.repository.PostRepository;
import com.gitfactory.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시글 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;  // ✨ 추가

    /**
     * 모든 게시글 조회
     */
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .toList();
    }

    /**
     * ID로 게시글 조회
     */
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return PostResponse.from(post);
    }

    /**
     * 게시글 생성
     */
    @Transactional
    public PostResponse createPost(PostRequest request) {
        // ✨ User 조회
        User author = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        // ✨ Post 생성 시 User 전달
        Post post = request.toEntity(author);
        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        // ✨ update 메서드는 title, content만 수정 (작성자는 변경 불가)
        post.update(request.title(), request.content());

        Post updatedPost = postRepository.save(post);
        return PostResponse.from(updatedPost);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        postRepository.delete(post);
    }

    /**
     * 제목으로 게시글 검색
     */
    public List<PostResponse> searchByTitle(String keyword) {
        return postRepository.findByTitleContaining(keyword).stream()
                .map(PostResponse::from)
                .toList();
    }

    /**
     * 작성자 이름으로 게시글 검색
     */
    public List<PostResponse> getPostsByAuthor(String keyword) {
        // ✨ 연관 관계 쿼리 메서드 사용
        return postRepository.findByAuthor_UsernameContaining(keyword).stream()
                .map(PostResponse::from)
                .toList();
    }
}