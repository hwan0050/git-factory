package com.gitfactory.blogapi.repository;

import com.gitfactory.blogapi.entity.Post;
import com.gitfactory.blogapi.entity.User;
import com.gitfactory.blogapi.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("PostRepository 테스트")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;  // ✨ 추가

    private User testUser;  // ✨ 추가
    private Post testPost;

    @BeforeEach
    void setUp() {
        // ✨ User 먼저 생성
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role(UserRole.USER)
                .build();
        testUser = userRepository.save(testUser);

        // ✨ Post는 User와 함께 생성
        testPost = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .author(testUser)  // User 객체
                .build();
    }

    @Test
    @DisplayName("게시글 저장 테스트")
    void save() {
        // When
        Post savedPost = postRepository.save(testPost);

        // Then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("테스트 제목");
        assertThat(savedPost.getContent()).isEqualTo("테스트 내용");
        assertThat(savedPost.getAuthorName()).isEqualTo("testuser");  // ✨
        assertThat(savedPost.getCreatedAt()).isNotNull();
        assertThat(savedPost.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("ID로 게시글 조회 테스트")
    void findById() {
        // Given
        Post savedPost = postRepository.save(testPost);

        // When
        Optional<Post> foundPost = postRepository.findById(savedPost.getId());

        // Then
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo("테스트 제목");
    }

    @Test
    @DisplayName("모든 게시글 조회 테스트")
    void findAll() {
        // Given
        postRepository.save(testPost);

        // When
        List<Post> posts = postRepository.findAll();

        // Then
        assertThat(posts).isNotEmpty();
        assertThat(posts).hasSize(1);
    }

    @Test
    @DisplayName("제목으로 검색 테스트")
    void findByTitleContaining() {
        // Given
        postRepository.save(testPost);

        // When
        List<Post> posts = postRepository.findByTitleContaining("테스트");

        // Then
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("테스트 제목");
    }

    @Test
    @DisplayName("작성자 이름으로 검색 테스트")
    void findByAuthor_UsernameContaining() {
        // Given
        postRepository.save(testPost);

        // When
        List<Post> posts = postRepository.findByAuthor_UsernameContaining("testuser");  // ✨

        // Then
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getAuthorName()).isEqualTo("testuser");  // ✨
    }

    @Test
    @DisplayName("작성자로 게시글 조회 테스트")
    void findByAuthor() {
        // Given
        postRepository.save(testPost);

        // When
        List<Post> posts = postRepository.findByAuthor(testUser);  // ✨ User 객체로 검색

        // Then
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getAuthorName()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("작성자 ID로 게시글 조회 테스트")
    void findByAuthorId() {
        // Given
        postRepository.save(testPost);

        // When
        List<Post> posts = postRepository.findByAuthorId(testUser.getId());  // ✨

        // Then
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getAuthorName()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void update() {
        // Given
        Post savedPost = postRepository.save(testPost);

        // When
        savedPost.update("수정된 제목", "수정된 내용");
        Post updatedPost = postRepository.save(savedPost);

        // Then
        assertThat(updatedPost.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedPost.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void delete() {
        // Given
        Post savedPost = postRepository.save(testPost);
        Long postId = savedPost.getId();

        // When
        postRepository.delete(savedPost);

        // Then
        Optional<Post> deletedPost = postRepository.findById(postId);
        assertThat(deletedPost).isEmpty();
    }
}