package com.gitfactory.blogapi.repository;

import com.gitfactory.blogapi.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("PostRepository 테스트")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 저장 테스트")
    void save() {
        // Given
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .author("테스트 작성자")
                .build();

        // When
        Post savedPost = postRepository.save(post);

        // Then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("테스트 제목");
        assertThat(savedPost.getContent()).isEqualTo("테스트 내용");
        assertThat(savedPost.getAuthor()).isEqualTo("테스트 작성자");
    }

    @Test
    @DisplayName("ID로 게시글 조회 테스트")
    void findById() {
        // Given
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .author("테스트 작성자")
                .build();
        Post savedPost = postRepository.save(post);

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
        Post post1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .author("작성자1")
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .author("작성자2")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        // When
        List<Post> posts = postRepository.findAll();

        // Then
        assertThat(posts).hasSize(2);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void update() {
        // Given
        Post post = Post.builder()
                .title("원본 제목")
                .content("원본 내용")
                .author("원본 작성자")
                .build();
        Post savedPost = postRepository.save(post);

        // When - 3개 파라미터로 수정 (title, content, author)
        savedPost.update("수정된 제목", "수정된 내용", "수정된 작성자");
        Post updatedPost = postRepository.save(savedPost);

        // Then
        assertThat(updatedPost.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedPost.getContent()).isEqualTo("수정된 내용");
        assertThat(updatedPost.getAuthor()).isEqualTo("수정된 작성자");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void delete() {
        // Given
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .author("테스트 작성자")
                .build();
        Post savedPost = postRepository.save(post);

        // When
        postRepository.delete(savedPost);

        // Then
        Optional<Post> deletedPost = postRepository.findById(savedPost.getId());
        assertThat(deletedPost).isEmpty();
    }

    @Test
    @DisplayName("제목으로 게시글 검색 테스트")
    void findByTitleContaining() {
        // Given
        Post post1 = Post.builder()
                .title("Spring Boot 학습")
                .content("내용1")
                .author("작성자1")
                .build();
        Post post2 = Post.builder()
                .title("JPA 학습")
                .content("내용2")
                .author("작성자2")
                .build();
        Post post3 = Post.builder()
                .title("Spring Security")
                .content("내용3")
                .author("작성자3")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // When
        List<Post> posts = postRepository.findByTitleContaining("Spring");

        // Then
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getTitle)
                .containsExactlyInAnyOrder("Spring Boot 학습", "Spring Security");
    }

    @Test
    @DisplayName("작성자로 게시글 검색 테스트")
    void findByAuthor() {
        // Given
        Post post1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .author("홍길동")
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .author("홍길동")
                .build();
        Post post3 = Post.builder()
                .title("제목3")
                .content("내용3")
                .author("김철수")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // When
        List<Post> posts = postRepository.findByAuthor("홍길동");

        // Then
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getAuthor)
                .containsOnly("홍길동");
    }
}